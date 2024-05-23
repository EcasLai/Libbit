package com.example.libbit

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.libbit.databinding.FragmentBookDetailBinding
import com.example.libbit.model.Book
import com.example.libbit.model.BookStatus
import com.example.libbit.model.Hold
import com.example.libbit.model.HoldStatus
import com.example.libbit.model.HoldType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import java.util.Calendar
import java.util.concurrent.TimeUnit

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

        Log.d("HoldAdapter", "Item clicked. Hold: $hold, Book: $book")

        //Dynamic Display book information
        if (book != null) {
            // Set the book details
            binding.tvBookDetailTitle.text = book.title
            binding.tvBookDetailAuthor.text = book.author
            Glide.with(requireContext())
                .load(book.bookImage)
                .into(binding.ivBookDetailImg)
            binding.tvBookDetailDesc.text = book.description
            binding.tvBookPrice.text = "RM ${book.price}"

            binding.btnBookDetailSubmit.text = book.status.toString()

            //Update reflect to book status
            updateButtonStatus(book, hold)

        }

        //Scrollable textview
        binding.tvBookDetailDesc.movementMethod = ScrollingMovementMethod.getInstance()

        binding.btnBookDetailBack.setOnClickListener{
            findNavController().navigateUp()
        }

        val qrCodeImage = generateQRCode("RES-${hold?.id}-${hold?.userId}", 200, 200)


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
            val expiredholdTimestamp = holdTimestamp + TimeUnit.DAYS.toMillis(7)

            val holdData = hashMapOf(
                "userId" to userId,
                "bookId" to book.id,
                "type" to book.type,
                "holdTimestamp" to holdTimestamp.toString(),
                "dueTimestamp" to expiredholdTimestamp.toString(),
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
        val buttonText = when (book.type) {
            HoldType.PHYSICAL_BOOK -> {
                if (hold?.status in setOf(HoldStatus.HOLDING, HoldStatus.PURCHASED, HoldStatus.COMPLETED)) {
                    "Return Book"
                } else {
                    when (book.status) {
                        BookStatus.AVAILABLE -> "Get Book"
                        BookStatus.ON_HOLD -> "Reserve Book"
                        else -> "Unavailable"
                    }
                }
            }
            HoldType.EBOOK -> {
                if (hold?.status in setOf(HoldStatus.PURCHASED, HoldStatus.COMPLETED)) {
                    "Read Book"
                } else {
                    when (book.status) {
                        BookStatus.AVAILABLE -> "Get Ebook"
                        else -> "Unavailable"
                    }
                }
            }
            else -> "Get Now"
        }
        binding.btnBookDetailSubmit.text = buttonText

        // Disable button and change appearance if book is unavailable
        if (book.status == BookStatus.DAMAGED || book.status == BookStatus.LOST) {
            binding.btnBookDetailSubmit.isEnabled = false
            binding.btnBookDetailSubmit.alpha = 0.5f // Reduce opacity to make it less appealing
        } else {
            binding.btnBookDetailSubmit.isEnabled = true
            binding.btnBookDetailSubmit.alpha = 1.0f // Restore opacity
        }

        binding.btnBookDetailSubmit.setOnClickListener {
            when (book.status) {
                BookStatus.AVAILABLE -> {
                    when (buttonText) {
                        "Get Ebook", "Get Now" ->
                            showConfirmationDialog(book) { placeHold(book) }
                        "Read Book" -> {
                            val bundle = Bundle().apply {
                                putParcelable("book", book)
                            }
                            val navController = findNavController()
                            navController.navigate(R.id.action_bookDetailFragment_to_bookViewFragment, bundle)
                        }
                        else -> {
                            val bundle = Bundle().apply {
                                putParcelable("book", book)
                            }
                            val navController = findNavController()
                            navController.navigate(R.id.action_bookDetailFragment_to_makeReservationFragment, bundle)
                        }
                    }
                }
                BookStatus.ON_HOLD -> {
                    if (buttonText == "Reserve Book") {
                        val bundle = Bundle().apply {
                            putParcelable("book", book)
                        }
                        val navController = findNavController()
                        navController.navigate(R.id.action_bookDetailFragment_to_makeReservationFragment, bundle)
                    } else if (buttonText == "Return Book"){
                        val bundle = Bundle().apply {
                            putParcelable("hold", hold)
                        }
                        val navController = findNavController()
                        navController.navigate(R.id.action_bookDetailFragment_to_bookReturnFragment, bundle)
                    }
                }
                else -> Unit // Do nothing for other statuses
            }
        }

        //Share button
        binding.imgBookDetailShare.setOnClickListener{
            val bookContent = "Book Recommendation: " + binding.tvBookDetailTitle.text + " by " + binding.tvBookDetailAuthor.text + "\n\n" +  binding.tvBookDetailDesc.text

            // Create an Intent to share the book content
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, bookContent)

            // Start the Activity with the Intent
            val chooser = Intent.createChooser(shareIntent, "Share Book Content")
            if (shareIntent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(chooser)
            } else {
                // Handle scenario where no suitable app is installed to handle the Intent
                Toast.makeText(context, "No app available to handle the share action", Toast.LENGTH_SHORT).show()
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

    private fun generateQRCode(text: String, width: Int, height: Int): Bitmap? {
        val writer = QRCodeWriter()
        try {
            val bitMatrix: BitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height)
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            return bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }

}
