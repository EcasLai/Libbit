package com.example.libbit

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libbit.adapter.BookAdapter
import com.example.libbit.databinding.FragmentHomeBinding
import com.example.libbit.model.Book
import com.example.libbit.FirestoreUtil
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate


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

        binding.progressBarDiscover.visibility = View.VISIBLE
        bookAdapter = BookAdapter(ArrayList())

        //Retrieve Book to RecyclerView CardView
        FirestoreUtil.getBooks{ bookArrayList ->
            activity?.runOnUiThread {
                bookAdapter.updateData(bookArrayList)
                binding.progressBarDiscover.visibility = View.GONE
            }
        }

        binding.rvBookHot.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = bookAdapter
        }

        binding.tvDiscoverHeader.setOnClickListener {
            // Add a sample book to Firestore
            val book = Book(title = "Sample Book", author = "John Doe", bookImage = "https://example.com/book1.jpg")
            FirestoreUtil.addBook(book)
        }



    }
}