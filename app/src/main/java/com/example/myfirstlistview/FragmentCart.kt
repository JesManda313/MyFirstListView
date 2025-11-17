package com.example.myfirstlistview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfirstlistview.databinding.FragmentCartBinding

class FragmentCart : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefManager: PrefManager
    private lateinit var adapterCart: CartAdapter
    private var listCart = mutableListOf<Bahan>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefManager = PrefManager(requireContext())
        listCart = prefManager.loadData("dt_cart")

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapterCart = CartAdapter(listCart) { item, position ->
            Toast.makeText(requireContext(), "Item dibeli: ${item.nama}", Toast.LENGTH_SHORT).show()
        }

        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterCart
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}