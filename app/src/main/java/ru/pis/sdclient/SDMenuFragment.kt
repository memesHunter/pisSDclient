package ru.pis.sdclient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment


class SDMenuFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_sdmenu, container, false)

        val small = view.findViewById<TextView>(R.id.small_text)
        small.text = defaultPreferences.getString("default_address", "")

        val txt2img = view.findViewById<Button>(R.id.txt2img_button)
        txt2img.setOnClickListener {
            switchToFragment(null, requireActivity(), TextToImgFragment())
        }

        val img2img = view.findViewById<Button>(R.id.img2img_button)
        img2img.setOnClickListener {
            switchToFragment(null, requireActivity(), ImgToImgFragment())
        }

        val disconnect = view.findViewById<Button>(R.id.disconnect_button)
        disconnect.setOnClickListener {
            changeNavBarTitle(requireActivity(), R.id.action_connect, getString(R.string.connect_button_text), R.drawable.ic_connect_image)
            sharedPreferences.edit().remove("session").apply()
            switchToFragment(null, requireActivity(), ConnectFragment())
        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = object : OnBackPressedCallback(
            true
        ) {
            override fun handleOnBackPressed() {
                changeNavBarTitle(requireActivity(), R.id.action_connect, getString(R.string.connect_button_text), R.drawable.ic_connect_image)
                sharedPreferences.edit().remove("session").apply()
                switchToFragment(null, requireActivity(), ConnectFragment())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }
}
