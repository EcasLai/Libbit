package com.example.libbit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libbit.R
import com.example.libbit.model.Fine
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FineAdapter(
    private val fineList: ArrayList<Fine>,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<FineAdapter.FineViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FineViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_item_fine, parent, false)
        return FineViewHolder(view)
    }

    override fun getItemCount(): Int {
        return fineList.size
    }

    override fun onBindViewHolder(holder: FineViewHolder, position: Int) {
        val fine = fineList[position]
        holder.bind(fine, itemClickListener)
    }


    fun updateData(newFineList: List<Fine>) {
        fineList.clear()
        fineList.addAll(newFineList)
        notifyDataSetChanged()
    }

    fun calculateTotalFine(): Double {
        var totalFine = 0.00
        for (fine in fineList) {
            // Convert fine amount string to double
            val fineAmount = fine.fineAmount?.toDoubleOrNull() ?: 0.00
            totalFine += fineAmount
        }
        return totalFine
    }

    inner class FineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fineTitleTv : TextView = itemView.findViewById(R.id.tvFineTitle)
        val fineIssueDateTv : TextView = itemView.findViewById(R.id.tvFineDate)
        val fineAmountTv : TextView = itemView.findViewById(R.id.tvFineAmount)

        fun bind(fine: Fine, clickListener: OnItemClickListener) {

//            val formattedDate = formatDate(fine.issueTimestamp)

            fineTitleTv.text = fine?.title ?: "Unknown Fine Title"
            fineIssueDateTv.text = fine?.issueTimestamp ?: "Unknown Fine Issued Date"
            val fineAmountText = fine?.fineAmount ?: "Unknown Fine Amount"
            fineAmountTv.text = String.format("%.2f", fineAmountText.toDoubleOrNull() ?: 0.0)

            itemView.setOnClickListener { clickListener.onItemClick(fine) }


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
        fun onItemClick(fine: Fine)
    }
}