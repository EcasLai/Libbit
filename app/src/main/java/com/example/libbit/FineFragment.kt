package com.example.libbit

import android.app.AlertDialog
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class FineFragment: Fragment() {

    private lateinit var binding: FragmentFineBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var fineAdapter: FineAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFineBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = auth.currentUser
        val userId = currentUser?.uid

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

        binding.progressBar.visibility = View.VISIBLE

        FirestoreUtil.getFine(
            "fines",
            userId,
            onSuccess = { fineList ->
                activity?.runOnUiThread {
                    // Update RecyclerView with fine data
                    fineAdapter.updateData(fineList)
                    binding.progressBar.visibility = View.GONE

                    // Calculate total fine and display it
                    val totalFine = fineAdapter.calculateTotalFine()
                    val totalFineText = String.format("%.2f", totalFine)
                    binding.tvTotalFine.text = "RM ${totalFineText}"

                }
            },
            onFailure = { exception ->
                // Handle any errors
                // You may want to display a message to the user
                binding.progressBar.visibility = View.GONE
            },
            onEmpty = {
                // Handle empty reservations
                showEmptyFineMessage()
            }
        )

        binding.rvFine.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = fineAdapter
        }

        if (userId != null) {
            binding.btnPayFine.setOnClickListener {
                showConfirmationDialog(userId){
                    FirestoreUtil.removeFine(
                        userId,
                        onSuccess = {
                            // Fine removed successfully
                            Toast.makeText(requireContext(), "Fine cleared successfully", Toast.LENGTH_SHORT).show()
                            // You can update UI or perform any other action here
                        },
                        onFailure = { exception ->
                            // Handle failure to remove fine
                            Toast.makeText(requireContext(), "Failed to remove fine: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }

    }

    private fun showEmptyFineMessage(){
        binding.imgNoFine.visibility = View.VISIBLE
        binding.tvNoFine.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    private fun showConfirmationDialog(userId: String?, onConfirmed: () -> Unit) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.apply {
            setTitle("Confirmation")
            setMessage("Are you sure you want to proceed?")
            setPositiveButton("Yes") { dialog, which ->
                FirestoreUtil.removeFine(
                    userId,
                    onSuccess = {
                        // Fine removed successfully
                        Toast.makeText(requireContext(), "Fine cleared successfully", Toast.LENGTH_SHORT).show()
                        // You can update UI or perform any other action here
                    },
                    onFailure = { exception ->
                        // Handle failure to remove fine
                        Toast.makeText(requireContext(), "Failed to remove fine: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                )
                dialog.dismiss()
            }
            setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
        }.create().show()
    }

}