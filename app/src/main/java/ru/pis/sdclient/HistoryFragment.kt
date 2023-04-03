package ru.pis.sdclient

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.forEach
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import ru.pis.sdclient.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private lateinit var binding : FragmentHistoryBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        binding.rv.adapter = HistoryAdapter(getImages(), OnItemClickListener())
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }
    inner class OnItemClickListener {
        fun onItemClick(image: ImageInfo) {
            val bundle = Bundle()
            bundle.putInt("icon",image.icon)
            bundle.putString("prompt",image.prompt)
            bundle.putString("negative",image.negative)
            switchToFragment(bundle, requireActivity(), ImageViewFragment())
        }
    }

    fun getImages(): ArrayList<ImageInfo> {
        return arrayListOf(
            ImageInfo(
                R.drawable.img1,
                "chibi, 1girl, solo, brown hair, red eyes, hoodie, long hair, guitar, <lora:bocchiEdStyleLora_bocchiEdStyle:1>, masterpiece, best quality, highres,",
                "lowres, bad anatomy, bad hands, text, error, missing fingers, extra digit, fewer digits, cropped, worst quality, low quality, normal quality, jpeg artifacts, signature, watermark, username, blurry,"
            ),
            ImageInfo(
                R.drawable.img2,
                "masterpiece, best quality, highres, outdoors, from afar, wallpaper, street, buildings, <lora:LORAFlatColor_flatColor:1>",
                "lowres, text, error, cropped, worst quality, low quality, normal quality, jpeg artifacts, signature, watermark, username, blurry,"
            ),
            ImageInfo(
                R.drawable.img3,
                "masterpiece, best quality, highres, absurdres, detailed, cherry blossoms, trees, meadow, landscape, phone wallpaper, evening, sunset, rain, clouds, sky, no light, <lora:LORAFlatColor_flatColor:1>",
                "lowres, error, cropped, worst quality, low quality, normal quality, jpeg artifacts, signature, watermark, username, blurry,"
            )
        )
    }
}