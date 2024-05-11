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
import com.example.libbit.adapter.HoldAdapter
import com.example.libbit.databinding.FragmentHomeBinding
import com.example.libbit.databinding.FragmentSavedBinding
import com.example.libbit.model.Book
import com.example.libbit.model.Hold
import com.example.libbit.model.HoldType
import com.example.libbit.util.FirestoreUtil

class SavedFragment : Fragment() {
    private lateinit var binding: FragmentSavedBinding
//    private lateinit var bookAdapter: BookSavedAdapter
    private lateinit var holdAdapter: HoldAdapter

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

        val itemClickListener = object : HoldAdapter.OnItemClickListener {
            override fun onItemClick(hold: Hold, book: Book?) {
                // Handle item click event here, e.g., navigate to BookDetailFragment
                book?.let {
                    val bundle = Bundle().apply {
                        putParcelable("hold", hold)
                        putParcelable("book", it)
                    }
                    val navController = findNavController()
                    navController.navigate(R.id.action_savedFragment_to_bookDetailFragment, bundle)
                } ?: run {
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
        retrieveSavedBook(HoldType.PHYSICAL_BOOK.toString())
    }

    private fun retrieveSavedBook(bookType: String){
        binding.progressBarSaved.visibility = View.VISIBLE

        holdAdapter.clearData()

        FirestoreUtil.getSaved(
            bookType,
            "holds",
            onSuccess = { savedWithBooks ->
                activity?.runOnUiThread {
                    if (savedWithBooks.isNotEmpty()) {
                        val holds = savedWithBooks.map { it.first } // Extract reservations from pairs
                        val booksMap = savedWithBooks.map { it.second.id to it.second }.toMap()
                        holdAdapter.updateData(holds, booksMap)
                    } else {
                        Toast.makeText(context, "No hold found", Toast.LENGTH_SHORT).show()
                    }
                    binding.progressBarSaved.visibility = View.GONE
                }
            },
            onFailure = { exception ->
                // Handle failure to retrieve reservations
                Toast.makeText(context, "Failed to retrieve reservations: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun highlightButton(button: Button) {
        button.setBackgroundResource(R.drawable.book_button_highlighted)
    }

    private fun unhighlightButton(button: Button) {
        button.setBackgroundResource(R.drawable.book_button_normal)
    }
}