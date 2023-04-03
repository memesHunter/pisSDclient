package ru.pis.sdclient

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(
    private val images: ArrayList<ImageInfo>,
    private val listener: HistoryFragment.OnItemClickListener
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_history, null, false)
        )
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(images[position], listener)
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon: ImageView = itemView.findViewById(R.id.image_view)
        fun bind(imageInfo: ImageInfo, listener: HistoryFragment.OnItemClickListener) {
            icon.setImageResource(imageInfo.icon)
            itemView.setOnClickListener {view ->
                listener.onItemClick(imageInfo)
            }
        }
    }
}
