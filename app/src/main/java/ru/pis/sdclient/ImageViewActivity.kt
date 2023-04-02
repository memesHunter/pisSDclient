package ru.pis.sdclient

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

/*
chibi, 1girl, solo, brown hair, red eyes, hoodie, long hair, guitar, <lora:bocchiEdStyleLora_bocchiEdStyle:1>, masterpiece, best quality, highres,

lowres, bad anatomy, bad hands, text, error, missing fingers, extra digit, fewer digits, cropped, worst quality, low quality, normal quality, jpeg artifacts, signature, watermark, username, blurry,

Steps: 20, Sampler: Euler a, CFG scale: 8, Seed: 556257202, Size: 512x512, Model hash: f773383dbc, Model: anything-v4.5-pruned-fp16, Clip skip: 2
*/

class ImageViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

//        val imageView = findViewById<ImageView>(R.id.image)
//        imageView.setImageResource(R.drawable.sample_image)
    }
}
