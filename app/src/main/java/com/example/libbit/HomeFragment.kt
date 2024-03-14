package com.example.libbit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libbit.adapter.BookAdapter
import com.example.libbit.databinding.FragmentHomeBinding
import com.example.libbit.model.Book
import com.example.libbit.util.FirestoreUtil

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var bookAdapter: BookAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Define clickListener
        val itemClickListener = object : BookAdapter.OnItemClickListener {
            override fun onItemClick(book: Book) {
                // Handle item click event here, e.g., navigate to BookDetailFragment
                val bundle = Bundle().apply {
                    putParcelable("book", book)
                }
                val navController = findNavController()
                navController.navigate(R.id.action_homeFragment_to_bookDetailFragment, bundle)
            }
        }

        binding.progressBarDiscover.visibility = View.VISIBLE

        bookAdapter = BookAdapter(ArrayList(), itemClickListener)


        FirestoreUtil.getBooks("books",
            onSuccess = { bookList ->
                activity?.runOnUiThread {
                    bookAdapter.updateData(bookList)
                    binding.progressBarDiscover.visibility = View.GONE
                }
            },
            onFailure = { exception ->
                // Handle any errors
                // You may want to display a message to the user
                binding.progressBarDiscover.visibility = View.GONE
            }
        )

//        FirestoreUtil.getBooks{ ArrayList ->
//            activity?.runOnUiThread {
//                bookAdapter.updateData(ArrayList)
//                binding.progressBarDiscover.visibility = View.GONE
//            }
//        }

        binding.rvBookHot.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = bookAdapter
        }

//        binding.tvDiscoverHeader.setOnClickListener {
//            // Add a sample book to Firestore
//            val book = Book(title = "Sample Book", author = "John Doe", bookImage = "https://example.com/book1.jpg")
//            FirestoreUtil.addBook(book)
//        }

    }
}