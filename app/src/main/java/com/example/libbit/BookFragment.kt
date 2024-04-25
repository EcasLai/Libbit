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

class BookFragment : Fragment() {

    private lateinit var binding: FragmentBookBinding
    private lateinit var reservationAdapter: ReservationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            onSuccess = { reservationsWithBooks ->
                activity?.runOnUiThread {
                    if (reservationsWithBooks.isNotEmpty()) {
                        val reservations = reservationsWithBooks.map { it.first } // Extract reservations from pairs
                        val booksMap = reservationsWithBooks.map { it.second.id to it.second }.toMap()
                        reservationAdapter.updateData(reservations, booksMap)
                    } else {
                        Toast.makeText(context, "No reservations found", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onFailure = { exception ->
                // Handle failure to retrieve reservations
                Toast.makeText(context, "Failed to retrieve reservations: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        )

        binding.rvBookReserve.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = reservationAdapter
        }

    }
}