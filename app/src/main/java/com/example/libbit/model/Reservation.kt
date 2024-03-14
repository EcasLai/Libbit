package com.example.libbit.model

import java.sql.Timestamp

data class Reservation(
    val reservationID: String,
    val reservationDate: Timestamp,
    val patronID: String, // Foreign key reference to Patron table
    val phyBookID: String // Foreign key reference to PhysicalBook table
)