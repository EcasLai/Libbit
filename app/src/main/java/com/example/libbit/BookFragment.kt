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
import com.example.libbit.adapter.HoldAdapter
import com.example.libbit.databinding.FragmentBookBinding
import com.example.libbit.model.Hold
import com.example.libbit.util.FirestoreUtil

class BookFragment : Fragment() {

    private lateinit var binding: FragmentBookBinding
    private lateinit var holdAdapter: HoldAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemClickListener = object : HoldAdapter.OnItemClickListener {
            override fun onItemClick(hold: Hold) {
                // Handle item click event here, e.g., navigate to BookDetailFragment
                val bundle = Bundle().apply {
                    putParcelable("hold", hold)
                }
                val navController = findNavController()
                navController.navigate(R.id.action_bookFragment_to_reservationDetailFragment, bundle)
            }
        }

        holdAdapter = HoldAdapter(ArrayList(), HashMap(), itemClickListener)

        FirestoreUtil.getHolds(
            "holds",
            onSuccess = { holdsWithBooks ->
                activity?.runOnUiThread {
                    if (holdsWithBooks.isNotEmpty()) {
                        val holds = holdsWithBooks.map { it.first } // Extract holds from pairs
                        val booksMap = holdsWithBooks.map { it.second.id to it.second }.toMap()
                        holdAdapter.updateData(holds, booksMap)
                    } else {
                        Toast.makeText(context, "No reservations found", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onFailure = { exception ->
                // Handle failure to retrieve holds
                Toast.makeText(context, "Failed to retrieve reservations: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        )

        binding.rvBookReserve.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = holdAdapter
        }

    }
}