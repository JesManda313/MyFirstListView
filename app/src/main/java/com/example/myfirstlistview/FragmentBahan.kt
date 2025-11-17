package com.example.myfirstlistview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfirstlistview.databinding.FragmentBahanBinding

class FragmentBahan : Fragment() {

    private var _binding: FragmentBahanBinding? = null
    private val binding get() = _binding!!

    var data = mutableListOf<Bahan>()

    private lateinit var adapterBahan: BahanAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBahanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (data.isEmpty()) {
            val namaArray = resources.getStringArray(R.array.data_nama_bahan)
            val kategoriArray = resources.getStringArray(R.array.data_kategori_bahan)
            val urlArray = resources.getStringArray(R.array.data_url_bahan)

            for (i in namaArray.indices) {
                data.add(
                    Bahan(
                        nama = namaArray[i],
                        kategori = kategoriArray[i],
                        imageUrl = urlArray[i]
                    )
                )
            }
        }
        setupRecyclerView()

        val btnTambah = view.findViewById<Button>(R.id.btnTambahBahan)
        btnTambah.setOnClickListener {
            showAddDialog()
        }
    }

    private fun setupRecyclerView() {
        adapterBahan = BahanAdapter(
            data,
            onCartClick = { item ->
                Toast.makeText(requireContext(), "Masuk Cart: ${item.nama}", Toast.LENGTH_SHORT).show()
            },
            onItemClick = { item, position ->
                showActionDialog(position, item)
            }
        )

        binding.rvBahan.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterBahan
        }
    }

    private fun showAddDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Tambah Bahan Baru")

        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(40, 30, 40, 8)

        val etNama = EditText(requireContext())
        etNama.hint = "Masukkan nama bahan"
        layout.addView(etNama)

        val etKategori = EditText(requireContext())
        etKategori.hint = "Masukkan kategori"
        layout.addView(etKategori)

        val etUrl = EditText(requireContext())
        etUrl.hint = "URL Gambar (http://...)"
        layout.addView(etUrl)

        builder.setView(layout)

        builder.setPositiveButton("Save") { dialog, _ ->
            val nama = etNama.text.toString().trim()
            val kategori = etKategori.text.toString().trim()
            val url = etUrl.text.toString().trim()

            if (nama.isNotEmpty() && kategori.isNotEmpty()) {
                data.add(Bahan(nama, kategori, url))
                adapterBahan.notifyDataSetChanged()

                Toast.makeText(requireContext(), "Bahan '$nama' ditambahkan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Nama & Kategori tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }

    private fun showActionDialog(position: Int, itemTerpilih: Bahan) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Item: ${itemTerpilih.nama}")
        builder.setMessage("Pilih tindakan yang ingin dilakukan")

        builder.setPositiveButton("Update Kategori") { _, _ ->
            showUpdateCategoryDialog(position, itemTerpilih)
        }

        builder.setNegativeButton("Delete") { _, _ ->
            data.removeAt(position)

            adapterBahan.notifyItemRemoved(position)
            adapterBahan.notifyItemRangeChanged(position, data.size)

            Toast.makeText(requireContext(), "Delete Item ${itemTerpilih.nama}", Toast.LENGTH_SHORT).show()
        }

        builder.setNeutralButton("Batal") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }

    private fun showUpdateCategoryDialog(position: Int, oldValue: Bahan) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Update Kategori")

        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        val tvOld = TextView(requireContext())
        tvOld.text = "Nama Bahan: ${oldValue.nama}"
        tvOld.textSize = 16f

        val etNewKategori = EditText(requireContext())
        etNewKategori.hint = "Masukkan Kategori Baru"
        etNewKategori.setText(oldValue.kategori)

        layout.addView(tvOld)
        layout.addView(etNewKategori)

        builder.setView(layout)

        builder.setPositiveButton("Save") { dialog, _ ->
            val newValue = etNewKategori.text.toString().trim()
            if (newValue.isNotEmpty()) {
                data[position].kategori = newValue
                adapterBahan.notifyItemChanged(position)

                Toast.makeText(requireContext(), "Kategori sudah diupdate: $newValue", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Kategori baru ga boleh kosong", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}