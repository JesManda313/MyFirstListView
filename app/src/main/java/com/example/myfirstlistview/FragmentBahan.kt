package com.example.myfirstlistview

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.myfirstlistview.databinding.FragmentBahanBinding
import kotlin.apply
import kotlin.let
import kotlin.text.isNotEmpty
import kotlin.text.trim

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BahanFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentBahan : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentBahanBinding? = null
    private val binding get() = _binding!!

    var data = mutableListOf<Bahan>()

    private fun showAddDialog(adapter: ArrayAdapter<Bahan>) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("tambah bahan baru")

        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(40, 30, 40, 8)

        val etNama = EditText(requireContext())
        etNama.hint = "Masukkan nama bahan"
        layout.addView(etNama)

        val etKategori = EditText(requireContext())
        etKategori.hint = "Masukkan kategori"
        layout.addView(etKategori)

        builder.setView(layout)

        builder.setPositiveButton("Save") { dialog, _ ->
            val nama = etNama.text.toString().trim()
            val kategori = etKategori.text.toString().trim()

            if (nama.isNotEmpty() && kategori.isNotEmpty()) {
                data.add(Bahan(nama, kategori))
                adapter.notifyDataSetChanged()
                Toast.makeText(
                    requireContext(),
                    "Bahan '$nama' ditambahkan",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Nama dan Kategori tidak boleh kosong",
                    Toast.LENGTH_SHORT
                ).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBahanBinding.inflate(inflater, container, false)
        return binding.root

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (data.isEmpty()){
            data.addAll(
                listOf(
                    Bahan("Keju", "Nabati"),
                    Bahan("Sapi", "Protein"),
                    Bahan("Nasi", "Karbohidrat")
                )
            )
        }

        val lvAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            data

        )

        val lvBahan = view.findViewById<ListView>(R.id.lvBahan)
        lvBahan.adapter = lvAdapter

        lvBahan.setOnItemClickListener {
            parent, view, position, id ->
            Toast.makeText(requireContext(),
                data[position].toString(),
                Toast.LENGTH_SHORT
            ).show()

        }

        val btnTambah = view.findViewById<Button>(R.id.btnTambahBahan)
        btnTambah.setOnClickListener {
            showAddDialog(lvAdapter)
        }

        val gestureDetector = GestureDetector(
            requireContext(),
            object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    val position = lvBahan.pointToPosition(
                        e.x.toInt(),
                        e.y.toInt()
                    )
                    if (position != ListView.INVALID_POSITION) {
                        val selectedItem = data[position]
                        showActionDialog(position, selectedItem, data, lvAdapter)
                    }
                    return true
                }
            }
        )
        lvBahan.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }
    }
    private fun showActionDialog(
        position: Int,
        itemTerpilih: Bahan,
        data: MutableList<Bahan>,
        adapter: ArrayAdapter<Bahan>
    ) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Item:  ${itemTerpilih.nama}")
        builder.setMessage("Pilih yang ingin dilakukan")

        builder.setPositiveButton("Update Kategori") { _, _ ->

            showUpdateCategoryDialog(position, itemTerpilih, data, adapter)
        }
        builder.setNegativeButton("Delete") { _, _ ->
            data.removeAt(position)
            adapter.notifyDataSetChanged()
            Toast.makeText(
                requireContext(),
                "Delete Item ${itemTerpilih.nama}",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNeutralButton("Batal") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun showUpdateCategoryDialog(
        position: Int,
        oldValue: Bahan,
        data: MutableList<Bahan>,
        adapter: ArrayAdapter<Bahan>
    ) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Update Kategori")

        val layout = LinearLayout(requireContext())
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)


        val tvOld = TextView(requireContext())
        tvOld.text = "Nama Bahan: ${oldValue.nama}"
        tvOld.textSize = 16f


        val etNew = EditText(requireContext())
        etNew.hint = "Masukkan Kategori Baru"
        etNew.setText(oldValue.kategori)

        layout.addView(tvOld)
        layout.addView(etNew)

        builder.setView(layout)

        builder.setPositiveButton("Save") { dialog, _ ->
            val newValue = etNew.text.toString().trim()
            if (newValue.isNotEmpty()) {
                data[position].kategori = newValue
                adapter.notifyDataSetChanged()
                Toast.makeText(
                    requireContext(),
                    "Kategori sudah diupdate: $newValue",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Kategori baru ga boleh kosong",
                    Toast.LENGTH_SHORT
                ).show()
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BahanFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentBahan().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}