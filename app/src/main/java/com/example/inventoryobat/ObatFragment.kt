package com.example.inventoryobat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.inventoryobat.databinding.FragmentObatBinding

class ObatFragment : Fragment() {

    private var _binding: FragmentObatBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ObatAdapter
    private lateinit var viewModel: MainViewModel
    private var jenisFilter: String? = null

    companion object {
        fun newInstance(jenis: String): ObatFragment {
            return ObatFragment().apply {
                arguments = Bundle().apply {
                    putString("jenis", jenis)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentObatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvObat.layoutManager = GridLayoutManager(context, 2)

        viewModel = MainViewModel()

        jenisFilter = arguments?.getString("jenis")

        adapter = ObatAdapter(requireContext(), emptyList())
        binding.rvObat.adapter = adapter

        viewModel.obatList.observe(viewLifecycleOwner) { obatList ->
            obatList?.let { adapter.updateData(it) }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        val filter = jenisFilter
        when {
            filter == null || filter.isEmpty() || filter == "SEMUA" -> viewModel.loadAllObat()
            else -> viewModel.loadObatByJenis(filter)
        }
    }

    fun filterData(keyword: String) {
        if (::adapter.isInitialized) {
            adapter.filterObat(keyword)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
