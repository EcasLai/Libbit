package com.example.libbit

import android.R
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.libbit.databinding.FragmentLoginBinding
import com.example.libbit.databinding.FragmentMakeReservationBinding
import com.example.libbit.model.Book
import com.example.libbit.model.ReservationStatus
import com.example.libbit.util.AuthenticationManager
import com.example.libbit.util.TimeUtil
import com.example.libbit.util.UserManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class MakeReservationFragment : Fragment(){

    private lateinit var binding: FragmentMakeReservationBinding
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
        // Inflate the layout for this fragment
        binding = FragmentMakeReservationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //Retrieve Book object from other fragment
        val book: Book? = arguments?.getParcelable("book")

        val navController = findNavController()

        binding.btnMakeReservationBack.setOnClickListener {
            findNavController().navigateUp()
        }

        if(book != null){
            binding.tvReserveBookName.text = book.title
            binding.tvReserveBookAuthor.text = book.author

            binding.editTextReserveDate.setOnClickListener {
                showDatePickerDialog()
            }

            //Pickup Venue Selection
            populateVenueSpinner()

            if(binding.venueSpinner != null && binding.editTextReserveDate != null){
                binding.btnMakeReservationSubmit.setOnClickListener {
                    showConfirmationDialog(){
                        placeReservation(book)
                    }
                }
            }
        }

    }

    private fun placeReservation(book: Book?) {
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null && book != null) {
            val userId = currentUser.uid // Get the current user's UID from Firebase Authentication

            val reservationString = binding.editTextReserveDate.text.toString()
            val reservationTimestamp = TimeUtil.convertDateToTimestamp(reservationString)

            val expiryString = binding.editTextDeadlineDate.text.toString()
            val expiryTimestamp = TimeUtil.convertDateToTimestamp(expiryString)

            val reservationData = hashMapOf(
                "userId" to userId,
                "bookId" to book.id,
                "type" to book.type,
                "timestamp" to reservationTimestamp,
                "expirationTimestamp" to expiryTimestamp,
                "location" to binding.venueSpinner.selectedItem.toString(),
                "status" to ReservationStatus.RESERVED
            )

            // Add the reservation to Firestore
            db.collection("reservations")
                .add(reservationData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successful Added as Reservation ${book.title}", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Fail Add Book", Toast.LENGTH_SHORT).show()
                }

        } else {
            Toast.makeText(context, "Firebase Authentication Fail, Try Again", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Update the EditText with the selected date
                val selectedCalendar = Calendar.getInstance()

                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)
                // Format the current date
                val selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedCalendar.time)

                selectedCalendar.add(Calendar.DAY_OF_MONTH, 7)
                // Format the date with an additional 7 days
                val selectedDeadline = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedCalendar.time)


                binding.editTextReserveDate.setText(selectedDate)
                binding.editTextDeadlineDate.setText(selectedDeadline)
            },
            year,
            month,
            dayOfMonth
        )

        // Show the DatePickerDialog
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun populateVenueSpinner() {
        val venues = arrayOf("Venue A", "Venue B", "Venue C", "Venue D")

        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, venues)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.venueSpinner.adapter = adapter
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

}