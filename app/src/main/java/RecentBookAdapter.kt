//package com.example.libbit.adapter
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.libbit.R
//import com.example.libbit.model.Book
//
//class RecentBookAdapter(private val bookList: List<Book>) : RecyclerView.Adapter<RecentBookAdapter.BookViewHolder>(){
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_recentbook, parent, false)
//        return BookViewHolder(view)
//    }
//
//    override fun getItemCount(): Int {
//        return bookList.size
//    }
//
//    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
//        val book = bookList[position]
//        holder.bookImageView.setImageResource(book.bookImage)
//        holder.bookNameTv.text = book.title
//        holder.bookAuthorTv.text = book.author
//    }
//
//
//    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
//        val bookImageView : ImageView = itemView.findViewById(R.id.cv_img_recent)
//        val bookNameTv : TextView = itemView.findViewById(R.id.cv_textview1_recent)
//        val bookAuthorTv : TextView = itemView.findViewById(R.id.cv_textview2_recent)
//    }
//}