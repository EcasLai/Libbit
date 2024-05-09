package com.example.libbit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libbit.adapter.BookAdapter
import com.example.libbit.adapter.FineAdapter
import com.example.libbit.adapter.HoldAdapter
import com.example.libbit.databinding.FragmentFineBinding
import com.example.libbit.databinding.FragmentSearchBinding
import com.example.libbit.model.Book
import com.example.libbit.model.Fine
import com.example.libbit.model.Hold
import com.example.libbit.model.HoldType
import com.example.libbit.util.FirestoreUtil
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class FineFragment: Fragment() {

    private lateinit var binding: FragmentFineBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var fineAdapter: FineAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFineBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemClickListener = object : FineAdapter.OnItemClickListener {
            override fun onItemClick(fine: Fine) {
                // Handle item click event here, e.g., navigate to BookDetailFragment
                val bundle = Bundle().apply {
                    putParcelable("fine", fine)
                }
//                val navController = findNavController()
//                navController.navigate(R.id.action_homeFragment_to_bookDetailFragment, bundle)
            }
        }

        fineAdapter = FineAdapter(ArrayList(), itemClickListener)

        FirestoreUtil.getFine(
            "fines",
            onSuccess = { fineList ->
                activity?.runOnUiThread {
                    fineAdapter.updateData(fineList)
                    binding.progressBar.visibility = View.GONE

                    // Calculate total fine and display it
                    val totalFine = fineAdapter.calculateTotalFine()
                    binding.tvTotalFine.text = totalFine.toString()
                    Toast.makeText(context, "Total Fine: $totalFine", Toast.LENGTH_SHORT).show()
                }
            },
            onFailure = { exception ->
                // Handle any errors
                // You may want to display a message to the user
                binding.progressBar.visibility = View.GONE
            }
        )

        binding.rvFine.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = fineAdapter
        }

    }

}