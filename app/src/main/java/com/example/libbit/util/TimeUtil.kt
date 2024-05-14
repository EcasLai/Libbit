package com.example.libbit.util
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeUtil {

    fun convertDateToTimestamp(dateString: String): String {
        // Parse the date string into a Date object
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = dateFormat.parse(dateString) ?: return "-1"

        // Convert the Date object to a timestamp (milliseconds since epoch)
        return date.time.toString()
    }

    fun convertTimestampToDate(timestampString: String): String {
        // Assuming the timestamp is in milliseconds
        val timestamp = timestampString.toLong()

        // Create a Date object from the timestamp
        val date = Date(timestamp)

        // Define the desired date format
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        // Format the date and return the formatted string
        return dateFormat.format(date)
    }
}