package com.example.mycloset.ui.outfit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycloset.R
import com.example.mycloset.data.model.ClosetItem

class PickItemsAdapter(
    private val onToggle: (ClosetItem) -> Unit
) : RecyclerView.Adapter<PickItemsAdapter.VH>() {

    private val items = mutableListOf<ClosetItem>()
    private val selectedIds = mutableSetOf<String>()

    fun submitList(newItems: List<ClosetItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun getSelectedIds(): List<String> = selectedIds.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_closet, parent, false)
        return VH(v)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName = itemView.findViewById<TextView>(R.id.tvItemName)
        private val tvMeta = itemView.findViewById<TextView>(R.id.tvItemMeta)
        private val img = itemView.findViewById<ImageView>(R.id.ivItem)

        fun bind(item: ClosetItem) {
            tvName.text = item.name
            tvMeta.text = "${item.type} • ${item.color} • ${item.season}"

            if (item.imageUrl.isNotBlank()) {
                Glide.with(itemView).load(item.imageUrl).into(img)
            } else {
                img.setImageResource(R.drawable.ic_launcher_foreground)
            }

            // סימון ויזואלי פשוט
            itemView.alpha = if (selectedIds.contains(item.id)) 0.6f else 1.0f

            itemView.setOnClickListener {
                if (selectedIds.contains(item.id)) selectedIds.remove(item.id) else selectedIds.add(item.id)
                notifyItemChanged(bindingAdapterPosition)
                onToggle(item)
            }
        }
    }
}
