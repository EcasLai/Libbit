package com.example.libbit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libbit.adapter.ReservationAdapter
import com.example.libbit.databinding.FragmentBookBinding
import com.example.libbit.model.Reservation
import com.example.libbit.util.FirestoreUtil
import com.google.firebase.auth.FirebaseAuth

class ReservationFragment : Fragment() {

    private lateinit var binding: FragmentBookBinding
    private lateinit var reservationAdapter: ReservationAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        val itemClickListener = object : ReservationAdapter.OnItemClickListener {
            override fun onItemClick(reservation: Reservation) {
                // Handle item click event here, e.g., navigate to BookDetailFragment
                val bundle = Bundle().apply {
                    putParcelable("reservation", reservation)
                }
                val navController = findNavController()
                navController.navigate(R.id.action_bookFragment_to_reservationDetailFragment, bundle)
            }
        }

        reservationAdapter = ReservationAdapter(ArrayList(), HashMap(), itemClickListener)

        FirestoreUtil.getReservations(
            "reservations",
            userId,
            onSuccess = { reservationsWithBooks ->
                activity?.runOnUiThread {
                    if (reservationsWithBooks.isNotEmpty()) {
                        val reservations = reservationsWithBooks.map { it.first } // Extract reservations from pairs
                        val booksMap = reservationsWithBooks.map { it.second.id to it.second }.toMap()

                        reservationAdapter.updateData(reservations, booksMap)

                        binding.progressBarReserve.visibility = View.GONE
                        binding.imgEmptyReservation.visibility = View.GONE
                        binding.tvEmptyReservation.visibility = View.GONE
                    } else {
                        Toast.makeText(context, "No reservations found", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onFailure = { exception ->
                // Handle failure to retrieve reservations
                Toast.makeText(context, "Failed to retrieve reservations: ${exception.message}", Toast.LENGTH_SHORT).show()
            },
            onEmpty = {
                // Handle empty reservations
                showEmptyReservationMessage()
            }
        )

        binding.rvBookReserve.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = reservationAdapter
        }

    }

    private fun showEmptyReservationMessage(){
        binding.imgEmptyReservation.visibility = View.VISIBLE
        binding.tvEmptyReservation.visibility = View.VISIBLE
        binding.progressBarReserve.visibility = View.GONE
    }
}