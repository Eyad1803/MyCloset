package com.example.mycloset.ui.item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycloset.R
import com.example.mycloset.data.model.ClosetItem

class ItemsAdapter(
    private val onClick: (ClosetItem) -> Unit = {}   // ✅ ברירת מחדל
) : ListAdapter<ClosetItem, ItemsAdapter.ItemVH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ClosetItem>() {
            override fun areItemsTheSame(oldItem: ClosetItem, newItem: ClosetItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ClosetItem, newItem: ClosetItem): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_closet, parent, false)
        return ItemVH(v)
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName = itemView.findViewById<TextView>(R.id.tvItemName)
        private val tvMeta = itemView.findViewById<TextView>(R.id.tvItemMeta)
        private val img = itemView.findViewById<ImageView>(R.id.ivItem)

        fun bind(item: ClosetItem) {
            tvName.text = item.name
            tvMeta.text = "${item.type} • ${item.color} • ${item.season}"

            if (item.imageUrl.isNotBlank()) {
                Glide.with(itemView)
                    .load(item.imageUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(img)
            } else {
                img.setImageResource(R.drawable.ic_launcher_foreground)
            }

            itemView.setOnClickListener { onClick(item) }
        }
    }
}
