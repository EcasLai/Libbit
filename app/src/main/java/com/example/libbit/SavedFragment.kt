package com.example.libbit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.libbit.databinding.FragmentSavedBinding

class SavedFragment : Fragment() {
    private lateinit var binding: FragmentSavedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val bookList = Constants.getBookData()
//
//        val itemAdapter = RecentBookAdapter(bookList)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewBookRecent)

        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

//        recyclerView.adapter = itemAdapter
    }
}