package ru.pis.sdclient

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SDMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val txt2img = findViewById<Button>(R.id.txt2img_button)
        txt2img.setOnClickListener {
            val intent = Intent(this, TextToImgActivity::class.java)
            startActivity(intent)
        }

//        val img2img = findViewById<Button>(R.id.img2img_button)
//        img2img.setOnClickListener {
//            val intent = Intent(this, ImgToImgActivity::class.java)
//            startActivity(intent)
//        }

        val disconnect = findViewById<Button>(R.id.disconnect_button)
        disconnect.setOnClickListener {
            val intent = Intent(this, ConnectActivity::class.java)
            startActivity(intent)
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
    }
}
