package com.example.practica3bookstore.ui.synopsis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.practica3bookstore.data.Book
import com.example.practica3bookstore.databinding.FragmentBookSynopsisBinding

class BookSynopsisFragment : Fragment() {

    private var _binding: FragmentBookSynopsisBinding? = null
    private val binding get() = _binding!!

    private var book: Book? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            book = it.getParcelable("book_arg")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookSynopsisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        book?.let {
            binding.bookTitleDetail.text = it.title
            binding.bookAuthorDetail.text = it.author
            binding.bookPriceDetail.text = it.price
            binding.bookCoverImage.setImageResource(it.image)
            binding.bookSynopsisDetail.text = it.synopsis
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}