package com.example.libbit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libbit.adapter.BookSavedAdapter
import com.example.libbit.databinding.FragmentHomeBinding
import com.example.libbit.databinding.FragmentSavedBinding
import com.example.libbit.model.Book
import com.example.libbit.model.HoldType
import com.example.libbit.util.FirestoreUtil

class SavedFragment : Fragment() {
    private lateinit var binding: FragmentSavedBinding
    private lateinit var bookAdapter: BookSavedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSavedBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.savedBtnBook.setOnClickListener {
            highlightButton(binding.savedBtnBook)
            unhighlightButton(binding.savedBtnEBook)
            retrieveSavedBook(HoldType.PHYSICAL_BOOK.toString())
        }

        binding.savedBtnEBook.setOnClickListener {
            highlightButton(binding.savedBtnEBook)
            unhighlightButton(binding.savedBtnBook)
            retrieveSavedBook(HoldType.EBOOK.toString())
        }

        val itemClickListener = object : BookSavedAdapter.OnItemClickListener {
            override fun onItemClick(book: Book) {
                // Handle item click event here, e.g., navigate to BookDetailFragment
                val bundle = Bundle().apply {
                    putParcelable("book", book)
                }
                val navController = findNavController()
                navController.navigate(R.id.action_savedFragment_to_bookDetailFragment, bundle)
            }
        }

        bookAdapter = BookSavedAdapter(ArrayList(), itemClickListener)


        binding.rvBookRecent.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = bookAdapter
        }

        //Initial
        retrieveSavedBook(HoldType.PHYSICAL_BOOK.toString())
    }

    private fun retrieveSavedBook(bookType: String){

        binding.progressBarSaved.visibility = View.VISIBLE

        bookAdapter.clearData()

        FirestoreUtil.getBooksType("books", bookType,
            onSuccess = { bookList ->
                activity?.runOnUiThread {
                    bookAdapter.updateData(bookList)
                    binding.progressBarSaved.visibility = View.GONE
                }
            },
            onFailure = { exception ->

                binding.progressBarSaved.visibility = View.GONE
            })
    }

    private fun highlightButton(button: Button) {
        button.setBackgroundResource(R.drawable.book_button_highlighted)
    }

    private fun unhighlightButton(button: Button) {
        button.setBackgroundResource(R.drawable.book_button_normal)
    }
}