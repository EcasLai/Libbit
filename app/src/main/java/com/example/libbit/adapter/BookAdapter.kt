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

class BookAdapter(private val bookList: ArrayList<Book>) : RecyclerView.Adapter<BookAdapter.BookViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_hotpick, parent, false)
        return BookViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.bind(book)
    }


    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val bookImageView : ImageView = itemView.findViewById(R.id.cv_img_hot)
        val bookNameTv : TextView = itemView.findViewById(R.id.cv_textview1_hot)
        val bookAuthorTv : TextView = itemView.findViewById(R.id.cv_textview2_hot)

        fun bind(book: Book){
            bookNameTv.text = book.title
            bookAuthorTv.text = book.author
            Glide.with(itemView.context)
                .load(book.bookImage)
                .into(bookImageView)
        }
    }
}