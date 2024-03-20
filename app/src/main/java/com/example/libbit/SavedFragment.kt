package com.example.libbit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libbit.adapter.BookAdapter
import com.example.libbit.adapter.BookSavedAdapter
import com.example.libbit.databinding.FragmentHomeBinding
import com.example.libbit.databinding.FragmentSavedBinding
import com.example.libbit.model.Book
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

        val itemClickListener = object : BookSavedAdapter.OnItemClickListener {
            override fun onItemClick(book: Book) {
                // Handle item click event here, e.g., navigate to BookDetailFragment
                val bundle = Bundle().apply {
                    putParcelable("book", book)
                }
                val navController = findNavController()
                navController.navigate(R.id.action_homeFragment_to_bookDetailFragment, bundle)
            }
        }

        bookAdapter = BookSavedAdapter(ArrayList(), itemClickListener)

        FirestoreUtil.getBooks("books",
            onSuccess = { bookList ->
                activity?.runOnUiThread {
                    bookAdapter.updateData(bookList)
//                    binding.progressBarDiscover.visibility = View.GONE
                }
            },
            onFailure = { exception ->

//                binding.progressBarDiscover.visibility = View.GONE
            }
        )

        binding.rvBookRecent.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = bookAdapter
        }


//        recyclerView.adapter = itemAdapter
    }
}