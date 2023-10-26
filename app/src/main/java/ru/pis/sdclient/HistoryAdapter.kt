package ru.pis.sdclient

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class HistoryAdapter(
    private val images: List<HistoryImage>,
    private val listener: HistoryFragment.OnItemClickListener
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        )
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bindImageToImageView(images[position], listener)
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon: ImageView = itemView.findViewById(R.id.image_view)
        private val saveButton: Button = itemView.findViewById(R.id.save_button)
        private val shareButton: Button = itemView.findViewById(R.id.share_button)
        private val deleteButton: Button = itemView.findViewById(R.id.delete_button)
        fun bindImageToImageView(
            imageInfo: HistoryImage,
            listener: HistoryFragment.OnItemClickListener
        ) {
            icon.setImageURI(Uri.parse(imageInfo.filename))
            itemView.setOnClickListener {
                listener.onItemClick(imageInfo)
            }
            saveButton.setOnClickListener {
                listener.onSaveButtonClick(imageInfo, itemView.context)
            }

            shareButton.setOnClickListener {
                listener.onShareButtonClick(imageInfo)
            }

            deleteButton.setOnClickListener {
                listener.onDeleteButtonClick(imageInfo, itemView.context)
            }
        }
    }
}
