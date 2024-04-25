package com.example.libbit.model

data class Ebook(
    val eBookID: String,
    val bookID: String, // Foreign key reference to Book table
    val format: String,
    val downloadLink: String
)