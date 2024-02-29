package com.example.libbit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.libbit.databinding.FragmentBookDetailBinding
import com.example.libbit.model.Book

class BookDetailFragment : Fragment(){

    private lateinit var binding: FragmentBookDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookDetailBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Retrieve Book object from other fragment
        val book: Book? = arguments?.getParcelable("book")

        if (book != null) {
            // Set the book details
            binding.tvBookDetailTitle.text = book.title
            binding.tvBookDetailAuthor.text = book.author
            Glide.with(requireContext())
                .load(book.bookImage)
                .into(binding.ivBookDetailImg)
        }
    }

}