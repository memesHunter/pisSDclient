package ru.pis.sdclient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ConnectFragment : Fragment() {

    private lateinit var addressEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_connect, container, false)

        val lowTimeoutClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()

        addressEditText = view.findViewById(R.id.address_edit_text)
        // Set the address if it is already saved
        val savedAddress = defaultPreferences.getString("default_address", "")
        if (defaultPreferences.getBoolean("save_address_switch", false) && savedAddress != "") {
            addressEditText.setText(savedAddress)
        }

        val connectButton = view.findViewById<Button>(R.id.connect_button)
        connectButton.setOnClickListener {
            val address: String = addressEditText.text.toString()

            try {
                retrofit = Retrofit.Builder()
                    .baseUrl("http://$address/")
                    .client(lowTimeoutClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                api = retrofit.create(SDRetrofitAPI::class.java)
                api.checkConnection().enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            // Login successful

                            // Save the address in shared preferences
                            defaultPreferences.edit().putString("default_address", address).apply()
                            savePrefString("session", "key:12345")

                            retrofit = Retrofit.Builder()
                                .baseUrl("http://$address/")
                                .client(okHttpClient)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build()
                            api = retrofit.create(SDRetrofitAPI::class.java)

                            // Change the title of the nav bar menu
                            changeNavBarTitle(requireActivity(), R.id.action_connect, getString(R.string.connected_button_text), R.drawable.ic_connected_image)


                            switchToFragment(null, requireActivity(), SDMenuFragment())
                        } else {
                            Toast.makeText(
                                view.context,
                                getString(R.string.toast_fail_ping),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // Handle network errors
                        Toast.makeText(
                            view.context,
                            getString(R.string.toast_fail_connect),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })

            } catch (e: java.lang.IllegalArgumentException) {
                Toast.makeText(view.context, getString(R.string.toast_invalid_address)+address, Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = object : OnBackPressedCallback(
            true
        ) {
            override fun handleOnBackPressed() {}
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

}