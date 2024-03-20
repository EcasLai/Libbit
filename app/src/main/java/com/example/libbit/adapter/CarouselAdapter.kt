package com.example.libbit.adapter

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.libbit.R

class NewsAdapter(private val newsList: List<NewsArticle>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.carousel_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsArticle = newsList[position]
        holder.bind(newsArticle)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsTextTitle: TextView = itemView.findViewById<TextView>(R.id.tvCarouselTitle)
        val newsImg: ImageView = itemView.findViewById<ImageView>(R.id.ivCarousel)

        fun bind(newsArticle: NewsArticle) {
            newsTextTitle.text = newsArticle.title
            Glide.with(itemView.context)
                .load(newsArticle.imageUrl)
                .into(newsImg)
        }
    }
}

data class NewsArticle(
    val title: String,
    val description: String,
    val imageUrl: String
)
