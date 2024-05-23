package com.example.libbit

import android.app.AlertDialog
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
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.libbit.adapter.BookAdapter
import com.example.libbit.databinding.FragmentBookDetailBinding
import com.example.libbit.databinding.FragmentReservationDetailBinding
import com.example.libbit.model.Book
import com.example.libbit.model.Reservation
import com.example.libbit.model.ReservationStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import java.util.Calendar

class ReservationDetailFragment : Fragment(){

    private lateinit var binding: FragmentReservationDetailBinding
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
        binding = FragmentReservationDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Retrieve Book object from other fragment
        val reservation: Reservation? = arguments?.getParcelable("reservation")
        val db = Firebase.firestore


        if (reservation != null) {
            // Set the book details
            Log.d("HoldAdapter", "Item clicked. Hold: $reservation")
            binding.tvReservationDetailDate.text = reservation.timestamp
            binding.tvReservationDetailExpiry.text = reservation.expirationTimestamp
            binding.tvReservationDetailLocation.text = reservation.location
            binding.tvReservationDetailStatus.text = reservation.status.toString()

            //Cancel Booking
            binding.btnReservationDetailCancel.setOnClickListener {
                showConfirmationDialog(){
                    cancelReservation(reservation)
                }
            }

            binding.btnReservationDetailModify.setOnClickListener {
                val bottomSheetFragment = ModifyReservationBottomSheetFragment.newInstance(reservation)
                bottomSheetFragment.show(requireActivity().supportFragmentManager, bottomSheetFragment.tag)
            }
        }

        binding.btnReservationDetailBack.setOnClickListener{
            findNavController().navigateUp()
        }

        val qrCodeImage = generateQRCode("RES-${reservation?.id}-${reservation?.userId}", 200, 200)

        binding.imgReservationDetailQR.setImageBitmap(qrCodeImage)
    }

    private fun cancelReservation(reservation: Reservation){
        db.collection("reservations").document(reservation.id.toString())
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Successful Cancel Reservation", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_reservationDetailFragment_to_bookFragment)
            }
            .addOnFailureListener {

            }
    }

    private fun showConfirmationDialog(onConfirmed: () -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.apply {
            setTitle("Confirmation")
            setMessage("Are you sure you want to proceed?")
            setPositiveButton("Yes") { dialog, which ->
                onConfirmed() // Call the provided function to proceed with the action
                dialog.dismiss()
            }
            setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
        }.create().show()
    }

    // Function to generate QR code
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
