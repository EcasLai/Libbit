package com.example.libbit

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libbit.adapter.BookAdapter
import com.example.libbit.databinding.FragmentHomeBinding
import com.example.libbit.model.Book
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var bookArrayList : ArrayList<Book>
    private lateinit var bookAdapter: BookAdapter
    //Firestore Database
    private lateinit var db: FirebaseFirestore



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val builder = AlertDialog.Builder(requireContext())

        val db = Firebase.firestore

        val ref = db.collection("Book Collection").document()

        binding.tvDiscoverHeader.setOnClickListener{
            //Set document on firestore
            val book = hashMapOf(
                "id" to ref.id,
                "isbn" to "1234",
                "title" to "Lets try",
                "bookImage" to "1",
                "author" to "Ecas"
//
//                @DocumentId val id: String ?= "",
//                val isbn: String ?= "",
//            val title:String ?= "",
//            val bookImage:Int,
//            val Author:String ?= ""
            )

            //Add book into cloud firestore
            db.collection("Book Collection").document("EBook")
                .set(book)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot successfully written!")
                    Toast.makeText(context, "City added", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

        }

        //

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        //Get book from Constants.kt
        val bookList = Constants.getBookData()

        val itemAdapter = BookAdapter(bookList)

        bookArrayList = arrayListOf()
        val bookAdapter = BookAdapter(bookArrayList)

        binding.tvDiscoverViewAll.setOnClickListener{

        }

        binding.rvBookHot.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = itemAdapter
//
//
//            binding.rvBookHot.adapter = bookAdapter
        }

        binding.rvBookReading.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = itemAdapter
        }

        //Time
        binding.tvDate.setText(LocalDate.now().dayOfMonth.toString())
        binding.tvDay.setText(LocalDate.now().dayOfWeek.toString())
        binding.tvMonth.setText(LocalDate.now().month.toString() + " " + LocalDate.now().year.toString())


//        eventChangeListener()
//        binding.tvDiscoverViewAll.setOnClickListener{
//            Toast.makeText(requireContext(), current.toString(), Toast.LENGTH_SHORT).show()
//        }

    //binding.tvDate.setText()

    }

    private fun eventChangeListener(){

        db = FirebaseFirestore.getInstance()

        db.collection("Book Collection")
//            .orderBy("title", Query.Direction.ASCENDING)
            .addSnapshotListener(object: EventListener<QuerySnapshot>{
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if(error != null){
                        Log.e("FireStore Error", error.message.toString())
                        return
                    }

                    for(dc: DocumentChange in value?.documentChanges!!){

                        if(dc.type == DocumentChange.Type.ADDED){

                            bookArrayList.add(dc.document.toObject(Book::class.java))
                        }
                    }

                    bookAdapter.notifyDataSetChanged()
                }
            })


    }
}