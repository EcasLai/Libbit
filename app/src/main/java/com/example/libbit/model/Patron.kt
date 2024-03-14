package com.example.libbit.model

data class Patron(
    val patronID: String,
    val name: String,
    val dateJoined: java.sql.Date,
    val email: String,
    val phoneNo: String
)