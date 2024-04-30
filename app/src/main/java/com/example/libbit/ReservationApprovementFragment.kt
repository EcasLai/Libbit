package com.example.libbit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.libbit.databinding.FragmentReservationApprovementBinding
import com.example.libbit.model.Book
import com.example.libbit.model.HoldStatus
import com.example.libbit.model.HoldType
import com.example.libbit.model.Reservation
import com.example.libbit.model.ReservationStatus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar


class ReservationApprovementFragment : Fragment(){

    private lateinit var binding: FragmentReservationApprovementBinding
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
        binding = FragmentReservationApprovementBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Firebase.firestore
        val qrContent = arguments?.getString("qrContent")

        if(qrContent != null){
            val (reservationId, userId) = parseQRContent(qrContent)
            if(reservationId != null){
                binding.btnApprove.setOnClickListener{
                    approveReservation(reservationId)
                    placeHoldQRPhysical(reservationId)
                }
            }


        }



    }

    private fun parseQRContent(contents: String): Pair<String?, String?> {

        val parts = contents.split("-")

        if (parts.size == 3 && parts[0] == "RES") {
            val reservationId = parts[1]
            val userId = parts[2]
            return Pair(reservationId, userId)
        }

        return Pair(null, null)
    }

    private fun approveReservation(reservationId: String){
        val reservationRef = db.collection("reservations").document("${reservationId}")

        reservationRef.update("status", ReservationStatus.FULFILLED)
            .addOnSuccessListener {

            }
            .addOnFailureListener { e ->

            }

    }



    private suspend fun getReservationById(reservationId: String): Reservation? {
        // Get instance of FirebaseFirestore
        val db = FirebaseFirestore.getInstance()

        // Reference to the specific reservation document
        val reservationRef = db.collection("reservations").document(reservationId)

        return try {
            // Retrieve the reservation document from Firestore
            val snapshot = reservationRef.get().await()

            // Check if the document exists
            if (snapshot.exists()) {
                // Convert the Firestore document to a Reservation object
                val reservation = snapshot.toObject(Reservation::class.java)
                // Return a new Reservation object with the ID field set
                reservation?.copy(id = snapshot.id)
            } else {
                null // Document does not exist
            }
        } catch (e: Exception) {
            null // Handle any exceptions
        }
    }

    private fun placeHoldQRPhysical(reservationId: String?) {
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null && reservationId != null) {
            val userId = currentUser.uid // Get the current user's UID from Firebase Authentication

            // Call the function within a coroutine scope
            CoroutineScope(Dispatchers.Main).launch {
                val reservation = getReservationById(reservationId)
                if (reservation != null) {
                    val holdTimestamp = Calendar.getInstance().timeInMillis
                    val holdData = hashMapOf(
                        "userId" to userId,
                        "reservationId" to reservationId,
                        "type" to HoldType.PHYSICAL_BOOK, // Assuming hold type is for physical books
                        "holdTimestamp" to holdTimestamp.toString(),
                        "dueTimestamp" to null,
                        "licenseKey" to reservationId,
                        "status" to HoldStatus.HOLDING
                    )
                    // Add the hold to Firestore
                    db.collection("holds")
                        .add(holdData)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Successful Added as Hold for reservation $reservationId", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Failed to place hold for reservation $reservationId", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(context, "Failed to retrieve reservation $reservationId", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "Firebase Authentication Fail, Try Again", Toast.LENGTH_SHORT).show()
        }
    }
}