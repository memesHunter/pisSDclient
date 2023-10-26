package ru.pis.sdclient

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.InputStream
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class ImgToImgFragment : Fragment() {

    private lateinit var selectImageButton: ImageView
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var promptEditText: EditText
    private lateinit var negativeEditText: EditText
    private lateinit var stepsSeekBar: SeekBar
    private lateinit var stepsValue: TextView
    private lateinit var keepSizeSwitch: SwitchCompat

    private lateinit var denoisingSeekBar: SeekBar
    private lateinit var denoisingValue: TextView
    private lateinit var samplerSpinner: Spinner

    private lateinit var selectedImageUri: Uri
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the launcher
        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    selectedImageUri = result.data?.data!!
                    selectImageButton.setImageURI(selectedImageUri)
                }
            }

        val callback = object : OnBackPressedCallback(
            true
        ) {
            override fun handleOnBackPressed() {
                switchToFragment(savedInstanceState, requireActivity(), SDMenuFragment())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this, // LifecycleOwner
            callback
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_img2img, container, false)
        progressBar = view.findViewById(R.id.progress_bar)

        selectImageButton = view.findViewById(R.id.select_image_view)

        selectImageButton.setOnClickListener {
            val imagePickerIntent = Intent(Intent.ACTION_PICK)
            imagePickerIntent.type = "image/*"
            imagePickerLauncher.launch(imagePickerIntent)
            view.findViewById<TextView>(R.id.select_image_text).visibility = View.GONE
        }

        promptEditText = view.findViewById(R.id.prompt_edit_text)
        negativeEditText = view.findViewById(R.id.negative_edit_text)

        stepsSeekBar = view.findViewById(R.id.steps_slider)
        stepsValue = view.findViewById(R.id.steps_value)

        denoisingSeekBar = view.findViewById(R.id.denoising_slider)
        denoisingValue = view.findViewById(R.id.denoising_value)

        samplerSpinner = view.findViewById(R.id.sampler_spinner)

        if (defaultPreferences.getBoolean("save_prompt_switch", false)) {
            promptEditText.setText(sharedPreferences.getString("img_prompt", ""))
            negativeEditText.setText(sharedPreferences.getString("img_negative_prompt", ""))
            stepsSeekBar.progress = sharedPreferences.getInt("img_steps", 0)
            samplerSpinner.setSelection(sharedPreferences.getInt("img_sampler", 0))
            denoisingSeekBar.progress = sharedPreferences.getInt("img_denoise", 0)
        }

        stepsValue.text = stepsSeekBar.progress.toString()
        stepsSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                stepsValue.text = stepsSeekBar.progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        denoisingValue.text = denoisingSeekBar.progress.toString()
        denoisingSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                denoisingValue.text = (denoisingSeekBar.progress / 100.0).toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        keepSizeSwitch = view.findViewById(R.id.keep_size_switch)

        val generateButton: Button = view.findViewById(R.id.generate_button)
        generateButton.setOnClickListener {
            val (base64Image, w, h) = imageToBase64AndSize(selectedImageUri)
            val initImage: List<String> = listOf(base64Image)
            val promptText: String = promptEditText.text.toString()
            val negativeText: String = negativeEditText.text.toString()
            val steps = if (stepsSeekBar.progress == 0) 10 else stepsSeekBar.progress
            val denoisingStrength: Double = denoisingSeekBar.progress / 100.0
            val sampler = samplerSpinner.selectedItem.toString()
            val width = if (keepSizeSwitch.isChecked) w else 512
            val height = if (keepSizeSwitch.isChecked) h else 512

            val payload = Img2ImgPayload(
                init_images = initImage,
                prompt = promptText,
                negative_prompt = negativeText,
                steps = steps,
                denoising_strength = denoisingStrength,
                sampler_name = sampler,
                width = width,
                height = height
            )
            saveImg2imgPrompt(payload)
            startUpdatingProgress()
            api.createImageFromImage(payload).enqueue(object : Callback<ResponseBody> {
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

    private fun imageToBase64AndSize(uri: Uri): Triple<String, Int, Int> {
        val inputStream: InputStream? = context?.contentResolver?.openInputStream(uri)
        val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()

        return Triple(Base64.encodeToString(byteArray, Base64.DEFAULT), bitmap.width, bitmap.height)
    }
}
