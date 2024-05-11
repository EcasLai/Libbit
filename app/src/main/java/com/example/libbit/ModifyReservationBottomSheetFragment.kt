package com.example.libbit

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.libbit.databinding.BottomsheetModifyReservationBinding
import com.example.libbit.model.Reservation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ModifyReservationBottomSheetFragment: BottomSheetDialogFragment() {
    private var _binding: BottomsheetModifyReservationBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            db = FirebaseFirestore.getInstance()
        }
        isCancelable = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomsheetModifyReservationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reservation: Reservation? = arguments?.getParcelable("reservation")

        if (reservation != null) {
            // Set the book details
            val selectedNewDate = binding.editTextReserveDate.setText(reservation.timestamp)
            val selectedNewExpiryDate = binding.editTextDeadlineDate.setText(reservation.expirationTimestamp)

            binding.editTextReserveDate.setOnClickListener {
                showDatePickerDialog()
            }


            binding.btnModifyReservation.setOnClickListener {
                modifyReservation(reservation)
            }
        }

    }

    private fun modifyReservation(reservation: Reservation){
        db.collection("reservations").document(reservation.id.toString())
            .update(
                mapOf(
                    "timestamp" to binding.editTextReserveDate.text.toString(),
                    "expirationTimestamp" to binding.editTextDeadlineDate.text.toString()
                )
            )
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Modify Successful", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {

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

    companion object {
        fun newInstance(reservation: Reservation): ModifyReservationBottomSheetFragment {
            val fragment = ModifyReservationBottomSheetFragment()
            val args = Bundle().apply {
                putParcelable("reservation", reservation)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
