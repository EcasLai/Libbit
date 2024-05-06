package com.example.libbit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libbit.R
import com.example.libbit.model.Book
import com.example.libbit.model.Reservation
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReservationAdapter(
    private val reservationList: ArrayList<Reservation>,
    private var booksMap: Map<String, Book>, // Map to reservation bookId to Book object
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_item_reservation, parent, false)
        return ReservationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return reservationList.size
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = reservationList[position]
        holder.bind(reservation, itemClickListener)
    }

    fun updateData(newReservationList: List<Reservation>, newBooksMap: Map<String?, Book>) {
        reservationList.clear()
        reservationList.addAll(newReservationList)
        notifyDataSetChanged()
        // Update the reference to the booksMap
        this.booksMap = newBooksMap.filterKeys { it != null }.mapKeys { it.key!! }
    }

    inner class ReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reservationImageView : ImageView = itemView.findViewById(R.id.imgReserveBookCover)
        val reservationNameTv : TextView = itemView.findViewById(R.id.tvReserveTitle)
        val reservationAuthorTv : TextView = itemView.findViewById(R.id.tvReserveAuthor)
        val reservationLocationTv : TextView = itemView.findViewById(R.id.tvReserveVenue)
        val reservationDeadlineTv : TextView = itemView.findViewById(R.id.tvReserveDeadline)


        fun bind(reservation: Reservation, clickListener: OnItemClickListener) {

            val bookID = reservation.bookId
            val book = booksMap[bookID]
            reservationNameTv.text = book?.title ?: "Unknown Book Title"
            reservationAuthorTv.text = book?.author ?: "Unknown Book Author"
            Glide.with(itemView.context)
                .load(book?.bookImage)
                .into(reservationImageView)
            reservationDeadlineTv.text = reservation.expirationTimestamp
            reservationLocationTv.text = reservation.location
            itemView.setOnClickListener { clickListener.onItemClick(reservation) }

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
        fun onItemClick(reservation: Reservation)
    }
}