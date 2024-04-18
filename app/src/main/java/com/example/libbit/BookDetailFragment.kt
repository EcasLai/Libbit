package com.example.libbit

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.libbit.adapter.BookAdapter
import com.example.libbit.databinding.FragmentBookDetailBinding
import com.example.libbit.model.Book
import com.example.libbit.model.HoldType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Calendar

class BookDetailFragment : Fragment(){

    private lateinit var binding: FragmentBookDetailBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            firebaseAuth = FirebaseAuth.getInstance()
            db = FirebaseFirestore.getInstance()
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
        val db = Firebase.firestore


        if (book != null) {
            // Set the book details
            binding.tvBookDetailTitle.text = book.title
            binding.tvBookDetailAuthor.text = book.author
            Glide.with(requireContext())
                .load(book.bookImage)
                .into(binding.ivBookDetailImg)
            binding.tvBookDetailDesc.text = book.description
        }

        //Scrollable textview
        binding.tvBookDetailDesc.movementMethod = ScrollingMovementMethod.getInstance()

        binding.btnBookDetailBack.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.btnBookDetailSubmit.setOnClickListener {
             placeHold(book)
        }

//        binding.btnBookDetailSubmit.setOnClickListener{
//            val bottomSheetFragment = OrderBottomSheetFragment().apply {
//                arguments = Bundle().apply {
//                    putParcelable("book", book)
//                }
//            }
//            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
//        }

    }

    //Function to add/buy book
    private fun placeHold(book: Book?) {
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null && book != null) {
            val userId = currentUser.uid // Get the current user's UID from Firebase Authentication
            val expirationTimestamp = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH, 7)
            }.timeInMillis
            val holdData = hashMapOf(
                "userId" to userId,
                "bookId" to book.id,
                "type" to book.type,
                "expirationTimestamp" to expirationTimestamp.toString(),
                "timestamp" to Calendar.getInstance().timeInMillis.toString()
            )
            // Add the hold to Firestore
            db.collection("holds")
                .add(holdData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successful Added as Hold ${book.title}", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Fail Add Book", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "Firebase Authentication Fail, Try Again", Toast.LENGTH_SHORT).show()
        }
    }

}
