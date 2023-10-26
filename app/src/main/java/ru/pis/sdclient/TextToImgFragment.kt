package ru.pis.sdclient

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Base64
import android.widget.*
import androidx.activity.OnBackPressedCallback
import java.util.*

class TextToImgFragment : Fragment() {

    private lateinit var promptEditText: EditText
    private lateinit var negativeEditText: EditText
    private lateinit var stepsSeekBar: SeekBar
    private lateinit var stepsValue: TextView
    private lateinit var samplerSpinner: Spinner
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_txt2img, container, false)
        progressBar = view.findViewById(R.id.progress_bar)

        promptEditText = view.findViewById(R.id.prompt_edit_text)
        negativeEditText = view.findViewById(R.id.negative_edit_text)
        stepsSeekBar = view.findViewById(R.id.steps_slider)
        stepsValue = view.findViewById(R.id.steps_value)
        samplerSpinner = view.findViewById(R.id.sampler_spinner)

        if (defaultPreferences.getBoolean("save_prompt_switch", false)) {
            promptEditText.setText(sharedPreferences.getString("txt_prompt", ""))
            negativeEditText.setText(sharedPreferences.getString("txt_negative_prompt", ""))
            stepsSeekBar.progress = sharedPreferences.getInt("txt_steps", 10)
            samplerSpinner.setSelection(sharedPreferences.getInt("txt_sampler", 0))
        }

        stepsValue.text = stepsSeekBar.progress.toString()
        stepsSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                stepsValue.text = stepsSeekBar.progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        val generateButton: Button = view.findViewById(R.id.generate_button)
        generateButton.setOnClickListener {
            val promptText: String = promptEditText.text.toString()
            val negativeText: String = negativeEditText.text.toString()
            val steps = if (stepsSeekBar.progress == 0) 10 else stepsSeekBar.progress
            val sampler = samplerSpinner.selectedItem.toString()
            val width = 512
            val height = 512

            val payload = Txt2ImgPayload(
                prompt = promptText,
                negative_prompt = negativeText,
                steps = steps,
                sampler_name = sampler,
                width = width,
                height = height
            )
            saveTxt2imgPrompt(payload)
            startUpdatingProgress()
            api.createImage(payload).enqueue(object : Callback<ResponseBody> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val bundle = Bundle()
                    stopUpdatingProgress()

                    if (response.isSuccessful) {
                        val jsonResponse = response.body()?.string()?.let { JSONObject(it) }
                        val imgArray = jsonResponse?.getJSONArray("images")
                        val base64Img = imgArray?.getString(0)

                        val decodedBytes = Base64.decode(base64Img, Base64.DEFAULT)
                        val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

                        val file = saveBitmapToFile(decodedBitmap, requireContext())

                        bundle.putString("imageFilePath", file?.absolutePath)
                        bundle.putString("prompt", payload.prompt)
                        bundle.putString("negative", payload.negative_prompt)
                        bundle.putInt("steps", payload.steps)
                        bundle.putString("sampler", payload.sampler_name)
                        bundle.putInt("width", payload.width)
                        bundle.putInt("height", payload.height)

                        saveImageToHistory(requireContext(), bundle)
                        switchToFragment(bundle, requireActivity(), ImageViewFragment())
                    } else {
                        Toast.makeText(context, getString(R.string.toast_gen_error), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    stopUpdatingProgress()
                    throw t
                }
            })

        }

        return view
    }

    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 3000L
    private val updateProgressRunnable = object : Runnable {
        override fun run() {
            api.getProgress().enqueue(object : Callback<ResponseBody> {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val p = response.body()?.string()?.let { JSONObject(it) }?.getString("progress")
                    if (p != null) {
                        progressBar.progress = (p.toDouble() * 100.0).toInt()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}
            })

            handler.postDelayed(this, updateInterval)
        }
    }
    private fun startUpdatingProgress() {
        progressBar.visibility = View.VISIBLE
        handler.postDelayed(updateProgressRunnable, updateInterval)
    }

    private fun stopUpdatingProgress() {
        progressBar.visibility = View.GONE
        progressBar.progress = 0
        handler.removeCallbacks(updateProgressRunnable)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = object : OnBackPressedCallback(
            true
        ) {
            override fun handleOnBackPressed() {
                switchToFragment(savedInstanceState, requireActivity(), SDMenuFragment())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }
}
