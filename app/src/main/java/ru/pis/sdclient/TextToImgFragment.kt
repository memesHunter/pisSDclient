package ru.pis.sdclient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

class TextToImgFragment : Fragment() {

    private lateinit var promptEditText: EditText
    private lateinit var negativeEditText: EditText
//    private lateinit var stepsSeepBar: SeekBar
//    private lateinit var samplerSpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_txt2img, container, false)

        // TODO: Set up spinner adapter and listener

        promptEditText = view.findViewById(R.id.prompt_edit_text)
        negativeEditText = view.findViewById(R.id.negative_edit_text)

        val generateButton: Button = view.findViewById(R.id.generate_button)
        generateButton.setOnClickListener {
            val promptText: String = promptEditText.text.toString()
            val negativeText: String = negativeEditText.text.toString()
//            val steps = stepsSeekBar ...
//            val sampler = samplerSpinner ...

            val mockupInfo = ImageInfo(
                R.drawable.img1,
                promptText,
                negativeText
            )

            val bundle = Bundle()
            bundle.putInt("icon",mockupInfo.icon)
            bundle.putString("prompt",mockupInfo.prompt)
            bundle.putString("negative",mockupInfo.negative)
            switchToFragment(bundle, requireActivity(), ImageViewFragment())
        }

        return view
    }
}
