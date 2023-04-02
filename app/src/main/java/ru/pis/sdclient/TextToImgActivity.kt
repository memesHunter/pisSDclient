package ru.pis.sdclient

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TextToImgActivity : AppCompatActivity() {

    private lateinit var promptEditText: EditText
    private lateinit var negativeEditText: EditText
//    private lateinit var stepsCount: SeekBar
//    private lateinit var sampler: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_txt2img)

        // TODO: Set up spinner adapter and listener

        // TODO: Generate image based on user input

        promptEditText = findViewById(R.id.prompt_edit_text)
        negativeEditText = findViewById(R.id.negative_edit_text)

        val connectButton: Button = findViewById(R.id.generate_button)
        connectButton.setOnClickListener {
            val promptText: String = promptEditText.text.toString()
            val negativeText: String = negativeEditText.text.toString()
//            val steps = stepsCount ...

            val intent = Intent(this, ImageViewActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}

