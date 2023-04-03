package ru.pis.sdclient

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity


class SDMenuFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_sdmenu, container, false)

        val txt2img = view.findViewById<Button>(R.id.txt2img_button)
        txt2img.setOnClickListener {
            switchToFragment(null, requireActivity(), TextToImgFragment())
        }

//        val img2img = findViewById<Button>(R.id.img2img_button)
//        img2img.setOnClickListener {
//            val intent = Intent(this, ImgToImgActivity::class.java)
//            startActivity(intent)
//        }

        val disconnect = view.findViewById<Button>(R.id.disconnect_button)
        disconnect.setOnClickListener {
            switchToFragment(null, requireActivity(), ConnectFragment())
        }

//        val pngInfo = findViewById<Button>(R.id.png_info_button)
//        pngInfo.setOnClickListener {
//            val intent = Intent(this, PngInfoActivity::class.java)
//            startActivity(intent)
//        }

//        val tbd = findViewById<Button>(R.id.tbd_button)
//        To Be Done
//        tbd.setOnClickListener {
//            val intent = Intent(this, Activity4::class.java)
//            startActivity(intent)
//        }

        return view
    }
}
