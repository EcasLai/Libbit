package com.example.libbit.model
import java.sql.Timestamp

data class Hold(
    val holdID: String,
    val startTime: Timestamp,
    val endTime: Timestamp
)