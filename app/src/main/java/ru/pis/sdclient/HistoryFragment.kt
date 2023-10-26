package ru.pis.sdclient

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import ru.pis.sdclient.databinding.FragmentHistoryBinding
import java.io.*


class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        binding.rv.adapter = HistoryAdapter(getImages(), OnItemClickListener())
        binding.rv.layoutManager = LinearLayoutManager(requireContext())

        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (!isGranted) {
                    Toast.makeText(
                        context,
                        getString(R.string.toast_fail_permission),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        return binding.root
    }

    inner class OnItemClickListener {
        fun onItemClick(image: HistoryImage) {
            val bundle = Bundle()
            bundle.putString("imageFilePath", image.filename)
            bundle.putString("prompt", image.prompt)
            bundle.putString("negative", image.negative)
            bundle.putInt("steps", image.steps)
            bundle.putString("sampler", image.sampler)
            bundle.putInt("width", image.width)
            bundle.putInt("height", image.height)
            switchToFragment(bundle, requireActivity(), ImageViewFragment())
        }

        fun onSaveButtonClick(imageInfo: HistoryImage, context: Context) {
            if (imageInfo.filename == null) {
                Toast.makeText(context, getString(R.string.toast_no_source_img), Toast.LENGTH_SHORT).show()
                return
            }

            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) -> {
                    trySaveFile(imageInfo.filename, context)
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
        }

        fun onShareButtonClick(imageInfo: HistoryImage) {
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, Uri.parse(imageInfo.filename))
                type = "image/png"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_title)))
        }

        fun onDeleteButtonClick(imageInfo: HistoryImage, context: Context) {
            if (imageInfo.filename == null) {
                Toast.makeText(context, getString(R.string.toast_no_source_img), Toast.LENGTH_SHORT).show()
                return
            }
            try {
                val toDelete = File(imageInfo.filename)

                toDelete.delete()
                AppDatabase.getInstance(context).imageDao().delete(imageInfo)
                reloadFragment()
            } catch (e: IOException) {
                Toast.makeText(context, getString(R.string.toast_delete_error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getImages(): List<HistoryImage> {
        return AppDatabase.getInstance(requireContext()).imageDao().getAll().reversed()
    }

    private fun reloadFragment() {
        switchToFragment(null, requireActivity(), HistoryFragment())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = object : OnBackPressedCallback(
            true
        ) {
            override fun handleOnBackPressed() {}
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }
}