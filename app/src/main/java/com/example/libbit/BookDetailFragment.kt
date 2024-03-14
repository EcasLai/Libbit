package com.example.libbit

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.libbit.adapter.BookAdapter
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

        //Scrollable textview
        binding.tvBookDetailDesc.movementMethod = ScrollingMovementMethod.getInstance()


        binding.btnBookDetailBack.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.btnBookDetailSubmit.setOnClickListener{
            val bottomSheetFragment = OrderBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("book", book)
                }
            }
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
        }

    }

}