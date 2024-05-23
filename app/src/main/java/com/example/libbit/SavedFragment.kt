package com.example.libbit

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libbit.adapter.HoldAdapter
import com.example.libbit.databinding.FragmentHomeBinding
import com.example.libbit.databinding.FragmentSavedBinding
import com.example.libbit.model.Book
import com.example.libbit.model.Hold
import com.example.libbit.model.HoldType
import com.example.libbit.util.FirestoreUtil
import com.google.firebase.auth.FirebaseAuth

class SavedFragment : Fragment() {
    private lateinit var binding: FragmentSavedBinding
    private lateinit var holdAdapter: HoldAdapter
    private lateinit var auth: FirebaseAuth

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
        // Inflate the layout for this fragment
        binding = FragmentSavedBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        binding.savedBtnBook.setOnClickListener {
            highlightButton(binding.savedBtnBook)
            unhighlightButton(binding.savedBtnEBook)
            retrieveSavedBook(HoldType.PHYSICAL_BOOK.toString(), userId)
            binding.imgEmptyHold.visibility = View.GONE
            binding.tvEmptyHold.visibility = View.GONE
        }

        binding.savedBtnEBook.setOnClickListener {
            highlightButton(binding.savedBtnEBook)
            unhighlightButton(binding.savedBtnBook)
            retrieveSavedBook(HoldType.EBOOK.toString(), userId)
            binding.imgEmptyHold.visibility = View.GONE
            binding.tvEmptyHold.visibility = View.GONE
        }

        val itemClickListener = object : HoldAdapter.OnItemClickListener {
            override fun onItemClick(hold: Hold, book: Book?) {
                // Log a message to indicate that onItemClick is triggered
                Log.d("HoldAdapter", "Item clicked. Hold: $hold, Book: $book")

                // Handle item click event here, e.g., navigate to BookDetailFragment
                book?.let {
                    val bundle = Bundle().apply {
                        putParcelable("hold", hold)
                        putParcelable("book", it)
                    }
                    val navController = findNavController()
                    navController.navigate(R.id.action_savedFragment_to_bookDetailFragment, bundle)
                } ?: run {
                    // Log a message if the book object is null
                    Log.e("HoldAdapter", "Book is null")
                    Toast.makeText(context, "Book information not available", Toast.LENGTH_SHORT).show()
                }
            }
        }

        holdAdapter = HoldAdapter(ArrayList(), HashMap(), itemClickListener)

        binding.rvBookSaved.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = holdAdapter
        }

        //Initial
        retrieveSavedBook(HoldType.PHYSICAL_BOOK.toString(), userId)
    }

    private fun retrieveSavedBook(bookType: String, userId: String?){
        binding.progressBarSaved.visibility = View.VISIBLE
        holdAdapter.clearData()

        FirestoreUtil.getSaved(
            bookType,
            "holds",
            userId,
            onSuccess = { savedWithBooks ->
                activity?.runOnUiThread {
                    if (savedWithBooks.isNotEmpty()) {
                        val holds: List<Hold> = savedWithBooks.mapNotNull { it.first }

                        val booksMap: Map<String, Book> = savedWithBooks
                            .filter { it.second != null && it.second.id != null } // Filter out null books and null ids
                            .associate { it.second!!.id!! to it.second!! } // Associate by non-null ids

                        if (holds.isNotEmpty()) {
                            holdAdapter.updateData(holds, booksMap)
                        } else {
                            // If holds list is empty, assume savedWithBooks contains only books
                            val booksList: List<Book> = savedWithBooks.filter { it.second != null }.map { it.second!! }
                            holdAdapter.updateData(emptyList(), booksList.map { it.id to it }.toMap().filterKeys { it != null }.mapKeys { it.key!! })

                        }
                    } else {
                        Toast.makeText(context, "No hold found", Toast.LENGTH_SHORT).show()
                    }
                    binding.progressBarSaved.visibility = View.GONE
                    binding.imgEmptyHold.visibility = View.GONE
                    binding.tvEmptyHold.visibility = View.GONE
                }
            },
            onFailure = { exception ->
                // Handle failure to retrieve reservations
                Toast.makeText(context, "Failed to retrieve reservations: ${exception.message}", Toast.LENGTH_SHORT).show()
            },
            onEmpty = {
                // Handle empty reservations
                showEmptySavedBook()
            }
        )
    }

    private fun highlightButton(button: Button) {
        button.setBackgroundResource(R.drawable.book_button_highlighted)
    }

    private fun unhighlightButton(button: Button) {
        button.setBackgroundResource(R.drawable.book_button_normal)
    }

    private fun showEmptySavedBook(){
        binding.progressBarSaved.visibility = View.GONE
        binding.imgEmptyHold.visibility = View.VISIBLE
        binding.tvEmptyHold.visibility = View.VISIBLE
    }
}