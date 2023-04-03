package ru.pis.sdclient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class ImageViewFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var descriptionTextView: TextView
    private lateinit var anotherStringTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_image_view, container, false)

        imageView = view.findViewById(R.id.image_view)
        descriptionTextView = view.findViewById(R.id.prompt_text_view)
        anotherStringTextView = view.findViewById(R.id.negative_text_view)

        val args = arguments
        if (args != null) {
            val imageResourceId = args.getInt(IMAGE_RESOURCE_ID_ARG)
            val description = args.getString(PROMPT_ARG)
            val anotherString = args.getString(NEGATIVE_ARG)

            imageView.setImageResource(imageResourceId)
            descriptionTextView.text = description
            anotherStringTextView.text = anotherString
        }

        return view
    }

    companion object {
        const val IMAGE_RESOURCE_ID_ARG = "icon"
        const val PROMPT_ARG = "prompt"
        const val NEGATIVE_ARG = "negative"

        fun newInstance(imageResourceId: Int, prompt: String, negative: String): ImageViewFragment {
            val args = Bundle()
            args.putInt(IMAGE_RESOURCE_ID_ARG, imageResourceId)
            args.putString(PROMPT_ARG, prompt)
            args.putString(NEGATIVE_ARG, negative)

            val fragment = ImageViewFragment()
            fragment.arguments = args

            return fragment
        }
    }
}
