package com.example.practica3bookstore.ui.synopsis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practica3bookstore.data.DataSource
import com.example.practica3bookstore.databinding.FragmentAllSynopsisBinding

class AllSynopsisFragment : Fragment() {

    private var _binding: FragmentAllSynopsisBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllSynopsisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val synopsisAdapter = AllSynopsisAdapter(DataSource.books)

        binding.synopsisRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = synopsisAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}