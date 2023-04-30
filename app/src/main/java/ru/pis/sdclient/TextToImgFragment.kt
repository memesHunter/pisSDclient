package ru.pis.sdclient

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import android.util.Base64

class TextToImgFragment : Fragment() {

    private lateinit var promptEditText: EditText
    private lateinit var negativeEditText: EditText
    private lateinit var stepsSeekBar: SeekBar
    private lateinit var samplerSpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_txt2img, container, false)

        promptEditText = view.findViewById(R.id.prompt_edit_text)
        negativeEditText = view.findViewById(R.id.negative_edit_text)
        stepsSeekBar = view.findViewById(R.id.steps_slider)
        samplerSpinner = view.findViewById(R.id.sampler_spinner)

//        retrofit = Retrofit.Builder()
//            .baseUrl("http://${sharedPreferences.getString("address", "")}/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()

        val generateButton: Button = view.findViewById(R.id.generate_button)
        generateButton.setOnClickListener {
            val promptText: String = promptEditText.text.toString()
            val negativeText: String = negativeEditText.text.toString()
            val steps = stepsSeekBar.progress
            val sampler = samplerSpinner.selectedItem.toString()

            val payload = Txt2ImgPayload(
                prompt = promptText,
                negative_prompt = negativeText,
                steps = 20,
                sampler_name = sampler
            )

            api.createImage(payload).enqueue(object : Callback<ResponseBody> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val bundle = Bundle()

                    if (response.isSuccessful) {
                        val jsonResponse = response.body()?.string()?.let { JSONObject(it) }
                        val imgArray = jsonResponse?.getJSONArray("images")
                        val base64Img = imgArray?.getString(0)?.substringAfter(',')
                        System.err.println(base64Img)

                        val decodedBytes = Base64.decode(base64Img, Base64.DEFAULT)
                        val decodedString = String(decodedBytes, Charsets.UTF_8)
                        val decodedInt = decodedString.toInt()

                        bundle.putInt("icon", decodedInt)
                    } else {
                        bundle.putInt("icon", R.drawable.img1)
                    }

                    bundle.putString("prompt",payload.prompt)
                    bundle.putString("negative",payload.negative_prompt)
                    switchToFragment(bundle, requireActivity(), ImageViewFragment())
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    throw t
                }
            })

//            val bundle = Bundle()
//            bundle.putInt("icon",mockupInfo.icon)
//            bundle.putString("prompt",mockupInfo.prompt)
//            bundle.putString("negative",mockupInfo.negative)
//            switchToFragment(bundle, requireActivity(), ImageViewFragment())
        }

        return view
    }

    data class Txt2ImgPayload(
        val prompt: String,
        val negative_prompt: String,

        val seed: Int = -1,
        val steps: Int = 20,
        val batch_size: Int = 1,
        val sampler_name: String = "Euler a",

        val width: Int = 512,
        val height: Int = 512,

//        val override_settings: JsonObject,
//        val override_settings_restore_afterwards: Boolean = true,
    )

//    private lateinit var retrofit: Retrofit

    private val retrofit: Retrofit = Retrofit.Builder()
//    .baseUrl("http://${sharedPreferences.getString("address", "")}/")
    .baseUrl("http://192.168.1.9:7860/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

    private val api = retrofit.create(SDRetrofitAPI::class.java)
    interface SDRetrofitAPI {
        @POST("sdapi/v1/txt2img")
        fun createImage(@Body data: Txt2ImgPayload): Call<ResponseBody>
    }
}
