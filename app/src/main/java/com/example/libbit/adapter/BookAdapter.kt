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

class BookAdapter(private val bookList: ArrayList<Book>,
                  private val itemClickListener: OnItemClickListener
                  ) : RecyclerView.Adapter<BookAdapter.BookViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_hotpick, parent, false)
        return BookViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.bind(book, itemClickListener)
    }

    fun updateData(newList: List<Book>) {
        bookList.clear()
        bookList.addAll(newList)
        notifyDataSetChanged()
    }

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val bookImageView : ImageView = itemView.findViewById(R.id.cv_img_hot)
        val bookNameTv : TextView = itemView.findViewById(R.id.cv_textview1_hot)
        val bookAuthorTv : TextView = itemView.findViewById(R.id.cv_textview2_hot)

        fun bind(book: Book, clickListener: OnItemClickListener){
            bookNameTv.text = book.title
            bookAuthorTv.text = book.author
            Glide.with(itemView.context)
                .load(book.bookImage)
                .into(bookImageView)

            itemView.setOnClickListener { clickListener.onItemClick(book) }
        }
    }
    interface OnItemClickListener {
        fun onItemClick(book: Book)
    }

}

class BookSavedAdapter(private val savedBookList: ArrayList<Book>,
                       private val itemClickListener: OnItemClickListener
                       ) : RecyclerView.Adapter<BookSavedAdapter.BookSavedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookSavedViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_recentbook, parent, false)
        return BookSavedViewHolder(view)
    }

    override fun getItemCount(): Int {
        return savedBookList.size
    }

    override fun onBindViewHolder(holder: BookSavedViewHolder, position: Int) {
        val book = savedBookList[position]
        holder.bind(book, itemClickListener)
    }

    fun updateData(newList: List<Book>) {
        savedBookList.clear()
        savedBookList.addAll(newList)
        notifyDataSetChanged()
    }

    fun clearData() {
        savedBookList.clear()
        notifyDataSetChanged()
    }

    inner class BookSavedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val savedBookImageView: ImageView = itemView.findViewById(R.id.imgRecentBookCover)
        val savedBookNameTv: TextView = itemView.findViewById(R.id.tvRecentTitle)
        val savedBookAuthorTv: TextView = itemView.findViewById(R.id.tvRecentAuthor)

        fun bind(book: Book, clickListener: OnItemClickListener) {
            savedBookNameTv.text = book.title
            savedBookAuthorTv.text = book.author
            Glide.with(itemView.context)
                .load(book.bookImage)
                .into(savedBookImageView)

            itemView.setOnClickListener { clickListener.onItemClick(book) }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(book: Book)
    }
}