package com.example.myfirstlistview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirstlistview.databinding.ItemCartBinding
import com.squareup.picasso.Picasso

class CartAdapter(
    private val listCart: MutableList<Bahan>,
    private val onCheckClick: (Bahan, Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bahan: Bahan) {
            binding.tvNama.text = bahan.nama
            binding.tvKategori.text = bahan.kategori

            if (bahan.imageUrl.isNotEmpty()) {
                Picasso.get().load(bahan.imageUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(binding.ivGambar)
            } else {
                binding.ivGambar.setImageResource(android.R.drawable.ic_menu_gallery)
            }
            binding.btnCheck.setOnClickListener {
                onCheckClick(bahan, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listCart[position])
    }

    override fun getItemCount() = listCart.size
}