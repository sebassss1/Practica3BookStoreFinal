package com.example.practica3bookstore.ui.synopsis

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.practica3bookstore.data.Book
import com.example.practica3bookstore.databinding.ItemAllSynopsisBinding

class AllSynopsisAdapter(private val books: List<Book>) : RecyclerView.Adapter<AllSynopsisAdapter.SynopsisViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SynopsisViewHolder {
        val binding = ItemAllSynopsisBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SynopsisViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SynopsisViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount() = books.size

    inner class SynopsisViewHolder(private val binding: ItemAllSynopsisBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.synopsisBookTitle.text = book.title
            binding.synopsisBookAuthor.text = "Por ${book.author}"
            binding.synopsisContent.text = book.synopsis
        }
    }
}