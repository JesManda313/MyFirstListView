package com.example.myfirstlistview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstlistview.databinding.ItemBahanBinding
import com.squareup.picasso.Picasso

class BahanAdapter(
    private val listBahan: MutableList<Bahan>,
    private val onCartClick: (Bahan) -> Unit,
    private val onItemClick: (Bahan, Int) -> Unit
) : RecyclerView.Adapter<BahanAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemBahanBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bahan: Bahan) {
            binding.tvNama.text = bahan.nama
            binding.tvKategori.text = bahan.kategori

            if (bahan.imageUrl.isNotEmpty()) {
                Picasso.get()
                    .load(bahan.imageUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.stat_notify_error)
                    .into(binding.ivGambar)
            } else {
                binding.ivGambar.setImageResource(android.R.drawable.ic_menu_gallery)
            }

            binding.btnCart.setOnClickListener {
                onCartClick(bahan)
            }

            binding.root.setOnClickListener {
                onItemClick(bahan, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBahanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listBahan[position])
    }

    override fun getItemCount(): Int = listBahan.size
}