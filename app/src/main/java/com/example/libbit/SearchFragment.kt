package com.example.libbit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libbit.adapter.BookAdapter
import com.example.libbit.databinding.FragmentSearchBinding
import com.example.libbit.model.Book
import com.example.libbit.util.FirestoreUtil
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class SearchFragment: Fragment() {
        private lateinit var binding: FragmentSearchBinding
        private lateinit var bookAdapter: BookAdapter
        private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchBarBook.requestFocus()

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        //initialize Book List
        loadSearchBook()

        //Binding
        binding.ivNavigateUp.setOnClickListener{
            findNavController().navigateUp()
        }

        //Define clickListener
        val itemClickListener = object : BookAdapter.OnItemClickListener {
            override fun onItemClick(book: Book) {
                // Handle item click event here, e.g., navigate to BookDetailFragment
                val bundle = Bundle().apply {
                    putParcelable("book", book)
                }
                val navController = findNavController()
                navController.navigate(R.id.action_searchFragment_to_bookDetailFragment, bundle)
            }
        }

        //RecyclerView and adapter
        bookAdapter = BookAdapter(ArrayList(), itemClickListener)

        binding.rvBookSearch.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = bookAdapter
        }

        //Search TextListener
        binding.searchBarBook.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                performSearch(newText)
                return true
            }
        })

    }

    //search function keyword search
    private fun performSearch(query: String?) {
        val collectionRef = firestore.collection("books")

        if (query.isNullOrBlank()) {
            loadSearchBook()

        } else {
            val searchWords = query.lowercase(Locale.ROOT)

            // Process the search query locally
            val lowercaseQuery = query.toLowerCase(Locale.ROOT)

            collectionRef.get()
                .addOnSuccessListener { querySnapshot ->
                    val filteredBooks = querySnapshot.documents.mapNotNull { document ->
                    val book = document.toObject(Book::class.java)
                    if (book != null && book.title!!.toLowerCase(Locale.ROOT).contains(lowercaseQuery)) {
                        book

                    } else {
                        null
                    }
                }
                // Update the number of search result shown
                    var numBooksToShow = filteredBooks.size
                    binding.tvSearchNum.setText(numBooksToShow.toString())

                // Update the RecyclerView with the filtered books
                    binding.progressBarSearch.visibility = View.GONE
                    bookAdapter.updateData(filteredBooks)

                }.addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Unable to load book", Toast.LENGTH_SHORT).show()
                    binding.progressBarSearch.visibility = View.GONE
            }

        }

    }

    //Load all Book List
    private fun loadSearchBook(){
        FirestoreUtil.getBooks("books",
            onSuccess = { bookList ->
                activity?.runOnUiThread {
                    bookAdapter.updateData(bookList)
                    binding.progressBarSearch.visibility = View.GONE

                    binding.tvSearchNum.setText(bookAdapter.itemCount.toString())
                }
            },
            onFailure = { exception ->
                // Handle any errors
                // You may want to display a message to the user
                binding.progressBarSearch.visibility = View.GONE
            }
        )
    }
}