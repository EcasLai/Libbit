package com.example.libbit.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libbit.R
import com.example.libbit.model.Book
import com.example.libbit.model.Hold
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HoldAdapter(
    private val holdList: ArrayList<Hold>,
    private var booksMap: Map<String, Book>, // Map to hold bookId to Book object
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<HoldAdapter.HoldViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoldViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_item_savedbook, parent, false)
        return HoldViewHolder(view)
    }

    override fun getItemCount(): Int {
        return holdList.size
    }

    override fun onBindViewHolder(holder: HoldViewHolder, position: Int) {
        val hold = holdList[position]
        holder.bind(hold, itemClickListener)
    }

    fun clearData() {
        holdList.clear()
        notifyDataSetChanged()
    }

    fun updateData(newHoldList: List<Hold>, newBooksMap: Map<String, Book>) {
        holdList.clear()
        holdList.addAll(newHoldList)
        notifyDataSetChanged()

        // Filter out entries with null keys from the newBooksMap
        val filteredBooksMap = newBooksMap.filterKeys { it != null }.mapKeys { it.key!! }

        // Update the reference to the booksMap
        this.booksMap = filteredBooksMap
        Log.d("HoldAdapter", "Books map size: ${booksMap.size}")
    }


    inner class HoldViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val holdImageView : ImageView = itemView.findViewById(R.id.imgSavedBookCover)
        val holdNameTv : TextView = itemView.findViewById(R.id.tvSavedTitle)
        val holdAuthorTv : TextView = itemView.findViewById(R.id.tvSavedAuthor)
        val holdStatusTv : TextView = itemView.findViewById(R.id.tvSavedStatus)

        fun bind(hold: Hold, clickListener: OnItemClickListener) {

            val formattedDate = formatDate(hold.holdTimestamp)

            val bookID = hold.bookId
            val book = booksMap[bookID]
                holdNameTv.text = book?.title ?: "Unknown Book Title"
                holdAuthorTv.text = book?.author ?: "Unknown Book Author"
                Glide.with(itemView.context)
                    .load(book?.bookImage)
                    .into(holdImageView)
                holdStatusTv.text = hold.status.toString()

                itemView.setOnClickListener { clickListener.onItemClick(hold, book) }
        }

        //convert long type into date
        private fun formatDate(timestamp: String?): String {
            timestamp?.toLongOrNull()?.let { millis ->
                val date = Date(millis)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                return dateFormat.format(date)
            }
            return "N/A" // Handle case when timestamp is null or not a valid long
        }
    }

    interface OnItemClickListener {
        fun onItemClick(hold: Hold, book: Book?)
    }
}