package ru.pis.sdclient

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.io.File

class ImageViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_image_view, container, false)

        val imageView = view.findViewById<ImageView>(R.id.image_view)
        val promptTextView = view.findViewById<TextView>(R.id.prompt_text_view)
        val negativeTextView = view.findViewById<TextView>(R.id.negative_text_view)
        val stepsTextView = view.findViewById<TextView>(R.id.steps_text_view)
        val samplerTextView = view.findViewById<TextView>(R.id.sampler_name_text_view)
        val sizeTextView = view.findViewById<TextView>(R.id.size_text_view)

        val bundle = arguments
        if (bundle != null) {
            val filePath = bundle.getString("imageFilePath")

            if (filePath != null) {
                val file = File(filePath)
                if (file.exists()) {
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    imageView.setImageBitmap(bitmap)
                }
            }

            var tmpstr = "${promptTextView.text}: ${bundle.getString("prompt")}"
            promptTextView.text = tmpstr
            tmpstr = "${negativeTextView.text}: ${bundle.getString("negative")}"
            negativeTextView.text = tmpstr
            tmpstr = "${stepsTextView.text}: ${bundle.getInt("steps")}"
            stepsTextView.text = tmpstr
            tmpstr = "${samplerTextView.text}: ${bundle.getString("sampler")}"
            samplerTextView.text = tmpstr
            tmpstr = "${sizeTextView.text}: ${bundle.getInt("width")}x${bundle.getInt("height")}"
            sizeTextView.text = tmpstr
        }

        return view
    }
}
