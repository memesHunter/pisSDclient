package ru.pis.sdclient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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

                val bundle = Bundle()
                bundle.putString("address_connected", address)

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
        return address == "127.0.0.1:7860" || address == "pis"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addressEditText = view.findViewById(R.id.address_edit_text)

    }
}