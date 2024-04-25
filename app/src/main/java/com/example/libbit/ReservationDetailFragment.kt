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
import com.example.libbit.databinding.FragmentReservationDetailBinding
import com.example.libbit.model.Book
import com.example.libbit.model.Reservation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
            binding.tvReservationDetailDate.text = reservation.timestamp
            binding.tvReservationDetailExpiry.text = reservation.expirationTimestamp
//            Glide.with(requireContext())
//                .load(reservation.bookImage)
//                .into(binding.ivBookDetailImg)
            binding.tvReservationDetailLocation.text = reservation.location
            binding.tvReservationDetailStatus.text = reservation.status.toString()
        }

        binding.btnReservationDetailBack.setOnClickListener{
            findNavController().navigateUp()
        }

    }

}
