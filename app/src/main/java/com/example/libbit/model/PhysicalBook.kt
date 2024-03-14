package com.example.libbit.model

class PhysicalBook {
    data class PhysicalBook(
        val phyBookID: String,
        val bookID: String, // Foreign key reference to Book table
        val shelfLocation: String,
        val availableCopies: Int
    )
}