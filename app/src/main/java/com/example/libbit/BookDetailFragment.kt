package com.example.libbit

import android.app.AlertDialog
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
import com.example.libbit.model.BookStatus
import com.example.libbit.model.Hold
import com.example.libbit.model.HoldStatus
import com.example.libbit.model.HoldType
import com.example.libbit.util.FirestoreUtil
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
        val hold: Hold? = arguments?.getParcelable("hold")

        val db = Firebase.firestore

        //Dynamic Display book information
        if (book != null) {
            // Set the book details
            binding.tvBookDetailTitle.text = book.title
            binding.tvBookDetailAuthor.text = book.author
            Glide.with(requireContext())
                .load(book.bookImage)
                .into(binding.ivBookDetailImg)
            binding.tvBookDetailDesc.text = book.description

            binding.btnBookDetailSubmit.text = book.status.toString()

            //Update reflect to book status
            updateButtonStatus(book, hold)

        }

        //Scrollable textview
        binding.tvBookDetailDesc.movementMethod = ScrollingMovementMethod.getInstance()

        binding.btnBookDetailBack.setOnClickListener{
            findNavController().navigateUp()
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

    //Function to  purchase e-book
    private fun placeHold(book: Book?){
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null && book != null) {
            val userId = currentUser.uid // Get the current user's UID from Firebase Authentication
            val holdTimestamp = Calendar.getInstance().timeInMillis
            val holdData = hashMapOf(
                "userId" to userId,
                "bookId" to book.id,
                "type" to book.type,
                "holdTimestamp" to holdTimestamp.toString(),
                "dueTimestamp" to null,
                "licenseKey" to book.id,
                "status" to HoldStatus.PURCHASED
            )
            // Add the hold to Firestore
            db.collection("holds")
                .add(holdData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successful Added as Hold ${book.title}", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Fail to own Book", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "Firebase Authentication Fail, Try Again", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateButtonStatus(book: Book, hold: Hold?) {
        val buttonText = when (book.status) {
            BookStatus.AVAILABLE -> if (hold?.status in setOf(HoldStatus.HOLDING, HoldStatus.PURCHASED, HoldStatus.COMPLETED)) "Read Book" else "Get Book"
            BookStatus.ON_HOLD -> if (hold?.status in setOf(HoldStatus.HOLDING, HoldStatus.PURCHASED, HoldStatus.COMPLETED)) "Read Book" else "Reserve Book"
            BookStatus.PURCHASED -> if (hold?.status in setOf(HoldStatus.HOLDING, HoldStatus.PURCHASED, HoldStatus.COMPLETED)) "Read Book" else "Owned"
            else -> if (hold?.status in setOf(HoldStatus.HOLDING, HoldStatus.PURCHASED, HoldStatus.COMPLETED)) "Read Book" else "Unavailable"
        }
        binding.btnBookDetailSubmit.text = buttonText

        binding.btnBookDetailSubmit.setOnClickListener {
            when (book.status) {
                BookStatus.AVAILABLE -> if (buttonText == "Get Book") showConfirmationDialog(book) { placeHold(book) }
                BookStatus.ON_HOLD -> if (buttonText == "Reserve Book") {
                    val bundle = Bundle().apply {
                        putParcelable("book", book)
                    }
                    val navController = findNavController()
                    navController.navigate(R.id.action_bookDetailFragment_to_makeReservationFragment, bundle)
                }
                else -> Unit // Do nothing for PURCHASED or other statuses
            }
        }
    }


    private fun showConfirmationDialog(book: Book, onConfirmed: () -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.apply {
            setTitle("Confirmation")
            setMessage("Are you sure you want to proceed?")
            setPositiveButton("Yes") { dialog, which ->
                placeHold(book)
                dialog.dismiss()
            }
            setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
        }.create().show()
    }

}
