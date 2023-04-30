package ru.pis.sdclient

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ConnectFragment : Fragment() {

    private lateinit var addressEditText: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_connect, container, false)

        // Get reference to the login button
        val connectButton = view.findViewById<Button>(R.id.connect_button)

        // Set OnClickListener on login button
        connectButton.setOnClickListener {
            val address: String = addressEditText.text.toString()
            if (tryConnect(address)) {
                // Login successful

                // Save the address in shared preferences
                savePrefString("address", address)
                savePrefString("session", "key:12345")

                val bundle = Bundle()
                bundle.putString("address", address)

                // Change the title of the nav bar menu
                changeNavBarTitle(requireActivity(), R.id.action_connect, "Session")



                switchToFragment(bundle, requireActivity(), SDMenuFragment())
            } else {
                // Login failed
                Toast.makeText(view.context, "Invalid address", Toast.LENGTH_SHORT).show()
            }

            /*when (tryConnect(address)) {
            *  ... ->
            *  ... ->
            *  ... ->
            *  else ->
            * }*/
        }

        return view
    }

    private fun tryConnect(address: String): Boolean {
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addressEditText = view.findViewById(R.id.address_edit_text)
        // Set the address if it is already saved
        val savedAddress = sharedPreferences.getString("address", "")
        if (savedAddress != "") {
            addressEditText.setText(savedAddress)
        }
    }
}