package com.example.libbit.util

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.example.libbit.model.Book
import com.example.libbit.model.BookGenre
import com.example.libbit.model.BookStatus
import com.example.libbit.model.Fine
import com.example.libbit.model.FineStatus
import com.example.libbit.model.Hold
import com.example.libbit.model.HoldStatus
import com.example.libbit.model.HoldType
import com.example.libbit.model.Reservation
import com.example.libbit.model.ReservationStatus
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.sql.Types.NULL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FirestoreUtil {

    private lateinit var db: FirebaseFirestore
    private lateinit var collectionName: String

    init {
        db = FirebaseFirestore.getInstance()
        collectionName = "books"
    }

    fun addBook(book: Book) {
        db.collection(collectionName)
            .add(book)
            .addOnSuccessListener { documentReference ->
                println("DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                println("Error adding document: $e")
            }
    }

    fun getBooks(collectionName: String, onSuccess: (List<Book>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection(collectionName)
            .get()
            .addOnSuccessListener { result: QuerySnapshot ->
                val bookList = mutableListOf<Book>()

                for (document in result.documents) {
                    val isbn = document.getString("isbn") ?: ""
                    val title = document.getString("title") ?: ""
                    val bookImage = document.getString("bookImage") ?: ""
                    val description = document.getString("description")?: ""
                    val author = document.getString("author")?: ""
                    val price = document.getString("price")?: ""
                    val bookUrl = document.getString("bookUrl")?: ""
                    val genreString = document.getString("genre")?: ""
                    val typeString = document.getString("type") ?: ""
                    val statusString = document.getString("status") ?: ""

                    // Convert typeString to HoldType enum
                    val type = when (typeString) {
                        "PHYSICAL_BOOK" -> HoldType.PHYSICAL_BOOK
                        "EBOOK" -> HoldType.EBOOK
                        else -> null
                    }

                    // Convert statusString to HoldType enum
                    val status = when (statusString) {
                        "AVAILABLE" -> BookStatus.AVAILABLE
                        "ON_HOLD" -> BookStatus.ON_HOLD
                        "PURCHASED" -> BookStatus.PURCHASED
                        "DAMAGED" -> BookStatus.DAMAGED
                        "LOST" -> BookStatus.LOST
                        else -> null
                    }

                    // Convert statusString to HoldType enum
                    val genre = when (genreString) {
                        "FANTASY" -> BookGenre.FANTASY
                        "ROMANCE" -> BookGenre.ROMANCE
                        "MYSTERY" -> BookGenre.MYSTERY
                        "SCIENCE_FICTION" -> BookGenre.SCIENCE_FICTION
                        "HORROR" -> BookGenre.HORROR
                        else -> null
                    }

                    val book = Book(id = document.id, isbn, title, bookImage,description, author,price, bookUrl, genre, type, status)


                    bookList.add(book)
                }

                onSuccess(bookList)
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                onFailure(exception)
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }

    fun getBooksType(
        collectionName: String,
        bookType: String,
        onSuccess: (List<Book>) -> Unit,
        onFailure: (Exception) -> Unit) {
        db.collection(collectionName)
            .whereEqualTo("type", bookType)
            .get()
            .addOnSuccessListener { result: QuerySnapshot ->
                val bookList = mutableListOf<Book>()

                for (document in result.documents) {
                    val isbn = document.getString("isbn") ?: ""
                    val title = document.getString("title") ?: ""
                    val bookImage = document.getString("bookImage") ?: ""
                    val description = document.getString("description")?: ""
                    val author = document.getString("author")?: ""
                    val price = document.getString("price")?: ""
                    val bookUrl = document.getString("bookUrl")?: ""
                    val genreString = document.getString("genre")?: ""
                    val typeString = document.getString("type") ?: ""
                    val statusString = document.getString("status") ?: ""

                    // Convert typeString to HoldType enum
                    val type = when (typeString) {
                        "PHYSICAL_BOOK" -> HoldType.PHYSICAL_BOOK
                        "EBOOK" -> HoldType.EBOOK
                        else -> null
                    }

                    // Convert statusString to HoldType enum
                    val status = when (statusString) {
                        "AVAILABLE" -> BookStatus.AVAILABLE
                        "ON_HOLD" -> BookStatus.ON_HOLD
                        "PURCHASED" -> BookStatus.PURCHASED
                        "DAMAGED" -> BookStatus.DAMAGED
                        "LOST" -> BookStatus.LOST
                        else -> null
                    }

                    // Convert statusString to HoldType enum
                    val genre = when (statusString) {
                        "FANTASY" -> BookGenre.FANTASY
                        "ROMANCE" -> BookGenre.ROMANCE
                        "MYSTERY" -> BookGenre.MYSTERY
                        "SCIENCE_FICTION" -> BookGenre.SCIENCE_FICTION
                        "HORROR" -> BookGenre.HORROR
                        else -> null
                    }


                    val book = Book(id = document.id, isbn, title, bookImage,description, author,price, bookUrl, genre, type, status)

                    bookList.add(book)
                }

                onSuccess(bookList)
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                onFailure(exception)
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }

    fun getReservations(
        collectionName: String,
        userId: String?,
        onSuccess: (List<Pair<Reservation, Book>>) -> Unit,
        onFailure: (Exception) -> Unit,
        onEmpty: () -> Unit
    ) {
        db.collection(collectionName)
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val reservationsWithBooks = mutableListOf<Pair<Reservation, Book>>()

                if (result.isEmpty) { // Check if there are no reservations
                    onEmpty() // Invoke the callback for handling empty reservations
                    return@addOnSuccessListener
                }

                for (document in result.documents) {
                    val userId = document.getString("userId") ?: ""
                    val bookId = document.getString("bookId") ?: ""
                    val typeString = document.getString("type") ?: ""
                    val timestampString = document.getString("timestamp") ?: ""
                    val expirationTimestampString = document.getString("expirationTimestamp") ?: ""
                    val location =  document.getString("location") ?: ""
                    val statusString = document.getString("status") ?: ""

                    val timestamp = TimeUtil.convertTimestampToDate(timestampString)

                    val expirationTimestamp = TimeUtil.convertTimestampToDate(expirationTimestampString)

                    // Convert typeString to ReservationType enum
                    val type = when (typeString) {
                        "PHYSICAL_BOOK" -> HoldType.PHYSICAL_BOOK
                        "EBOOK" -> HoldType.EBOOK
                        else -> null
                    }

                    val status = when (statusString) {
                        "RESERVED" -> ReservationStatus.RESERVED
                        "FULFILLED" -> ReservationStatus.FULFILLED
                        "EXPIRED" -> ReservationStatus.EXPIRED
                        "CANCELED" -> ReservationStatus.CANCELED
                        else -> null
                    }

                    val reservation = Reservation(
                        id = document.id,
                        userId,
                        bookId,
                        type,
                        timestamp,
                        expirationTimestamp,
                        location,
                        status
                    )

                    val fetchBookPromise = db.collection("books").document(bookId).get()

                    fetchBookPromise.addOnCompleteListener { bookTask ->
                        if (bookTask.isSuccessful) {
                            val bookDocument = bookTask.result
                            if (bookDocument != null && bookDocument.exists()) {
                                val isbn = bookDocument.getString("isbn") ?: ""
                                val title = bookDocument.getString("title") ?: ""
                                val bookImage = bookDocument.getString("bookImage") ?: ""
                                val description = bookDocument.getString("description") ?: ""
                                val author = bookDocument.getString("author") ?: ""
                                val price = bookDocument.getString("price") ?: ""
                                val bookUrl = bookDocument.getString("bookUrl")?: ""
                                val genreString = bookDocument.getString("genre")?: ""
                                val typeString = bookDocument.getString("type") ?: ""
                                val statusString = bookDocument.getString("status") ?: ""

                                // Convert typeString to HoldType enum
                                val type = when (typeString) {
                                    "PHYSICAL_BOOK" -> HoldType.PHYSICAL_BOOK
                                    "EBOOK" -> HoldType.EBOOK
                                    else -> null
                                }

                                // Convert statusString to HoldType enum
                                val status = when (statusString) {
                                    "AVAILABLE" -> BookStatus.AVAILABLE
                                    "ON_HOLD" -> BookStatus.ON_HOLD
                                    "PURCHASED" -> BookStatus.PURCHASED
                                    "DAMAGED" -> BookStatus.DAMAGED
                                    "LOST" -> BookStatus.LOST
                                    else -> null
                                }

                                // Convert statusString to HoldType enum
                                val genre = when (genreString) {
                                    "FANTASY" -> BookGenre.FANTASY
                                    "ROMANCE" -> BookGenre.ROMANCE
                                    "MYSTERY" -> BookGenre.MYSTERY
                                    "SCIENCE_FICTION" -> BookGenre.SCIENCE_FICTION
                                    "HORROR" -> BookGenre.HORROR
                                    else -> null
                                }

                                val book = Book(
                                    id = bookDocument.id,
                                    isbn = isbn,
                                    title = title,
                                    bookImage = bookImage,
                                    description = description,
                                    author = author,
                                    price = price,
                                    bookUrl = bookUrl,
                                    genre = genre,
                                    type = type,
                                    status = status
                                )

                                reservationsWithBooks.add(Pair(reservation, book))

                                if (reservationsWithBooks.size == result.documents.size) {
                                    onSuccess(reservationsWithBooks)
                                }
                            } else {
                                onFailure(Exception("Book document with ID $bookId does not exist"))
                            }
                        } else {
                            onFailure(bookTask.exception ?: Exception("Unknown error"))
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
                Log.w(ContentValues.TAG, "Error getting hold documents.", exception)
            }
    }

    fun getSaved(
        bookType: String,
        collectionName: String,
        userId: String?,
        onSuccess: (List<Pair<Hold, Book>>) -> Unit,
        onFailure: (Exception) -> Unit,
        onEmpty: () -> Unit
    ) {
        db.collection(collectionName)
            .whereEqualTo("type", bookType)
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val savedWithBooks = mutableListOf<Pair<Hold, Book>>()

                if (result.isEmpty) { // Check if there are no reservations
                    onEmpty() // Invoke the callback for handling empty reservations
                    return@addOnSuccessListener
                }

                for (document in result.documents) {
                    val userId = document.getString("userId") ?: ""
                    val bookId = document.getString("bookId") ?: ""
                    val typeString = document.getString("type") ?: ""
                    val timestamp = document.getString("holdTimestamp") ?: ""
                    val expirationTimestamp = document.getString("dueTimestamp") ?: ""
                    val licenseKey =  document.getString("licenseKey") ?: ""
                    val statusString = document.getString("status") ?: ""

                    // Convert typeString to SavedType enum
                    val type = when (typeString) {
                        "PHYSICAL_BOOK" -> HoldType.PHYSICAL_BOOK
                        "EBOOK" -> HoldType.EBOOK
                        else -> null
                    }

                    val status = when (statusString) {
                        "PURCHASED" -> HoldStatus.PURCHASED
                        "HOLDING" -> HoldStatus.HOLDING
                        "OVERDUE" -> HoldStatus.OVERDUE
                        "COMPLETED" -> HoldStatus.COMPLETED
                        "RETURNED" -> HoldStatus.RETURNED
                        else -> null
                    }

                    val hold = Hold(
                        id = document.id,
                        userId,
                        bookId,
                        type,
                        timestamp,
                        expirationTimestamp,
                        licenseKey,
                        status
                    )

                    val fetchBookPromise = db.collection("books").document(bookId).get()

                    fetchBookPromise.addOnCompleteListener { bookTask ->
                        if (bookTask.isSuccessful) {
                            val bookDocument = bookTask.result
                            if (bookDocument != null && bookDocument.exists()) {
                                val isbn = bookDocument.getString("isbn") ?: ""
                                val title = bookDocument.getString("title") ?: ""
                                val bookImage = bookDocument.getString("bookImage") ?: ""
                                val description = bookDocument.getString("description") ?: ""
                                val author = bookDocument.getString("author") ?: ""
                                val price = bookDocument.getString("price") ?: ""
                                val bookUrl = bookDocument.getString("bookUrl")?: ""
                                val genreString = bookDocument.getString("genre")?: ""
                                val typeString = bookDocument.getString("type") ?: ""
                                val statusString = bookDocument.getString("status") ?: ""

                                // Convert typeString to HoldType enum
                                val type = when (typeString) {
                                    "PHYSICAL_BOOK" -> HoldType.PHYSICAL_BOOK
                                    "EBOOK" -> HoldType.EBOOK
                                    else -> null
                                }

                                // Convert statusString to HoldType enum
                                val status = when (statusString) {
                                    "AVAILABLE" -> BookStatus.AVAILABLE
                                    "ON_HOLD" -> BookStatus.ON_HOLD
                                    "PURCHASED" -> BookStatus.PURCHASED
                                    "DAMAGED" -> BookStatus.DAMAGED
                                    "LOST" -> BookStatus.LOST
                                    else -> null
                                }

                                // Convert statusString to HoldType enum
                                val genre = when (genreString) {
                                    "FANTASY" -> BookGenre.FANTASY
                                    "ROMANCE" -> BookGenre.ROMANCE
                                    "MYSTERY" -> BookGenre.MYSTERY
                                    "SCIENCE_FICTION" -> BookGenre.SCIENCE_FICTION
                                    "HORROR" -> BookGenre.HORROR
                                    else -> null
                                }

                                val book = Book(
                                    id = bookDocument.id,
                                    isbn = isbn,
                                    title = title,
                                    bookImage = bookImage,
                                    description = description,
                                    author = author,
                                    price = price,
                                    bookUrl = bookUrl,
                                    genre = genre,
                                    type = type,
                                    status = status
                                )

                                savedWithBooks.add(Pair(hold, book))

                                // Check if hold is overdue and add fine if needed
                                if (isHoldOverdue(hold) && hold.status == HoldStatus.HOLDING) {
                                    val fineAmount = calculateFineAmount(hold)
                                    // Update fines collection in Firestore with the fine amount and hold details
                                    addFineToFirestore(hold, fineAmount)
                                    // Update status of hold to overdue in Firestore
                                    hold.id?.let { updateHoldStatusInFirestore(it, HoldStatus.OVERDUE) }
                                }

                                if (savedWithBooks.size == result.documents.size) {
                                    onSuccess(savedWithBooks)
                                }

                            } else {
                                onFailure(Exception("Book document with ID $bookId does not exist"))
                            }
                        } else {
                            onFailure(bookTask.exception ?: Exception("Unknown error"))
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
                Log.w(ContentValues.TAG, "Error getting hold documents.", exception)
            }
    }

    private fun updateHoldStatusInFirestore(holdId: String, newStatus: HoldStatus) {
        val holdRef = db.collection("holds").document(holdId)

        // Update hold status field in Firestore
        holdRef.update("status", newStatus.toString())
            .addOnSuccessListener {
                Log.d(TAG, "Hold status updated successfully")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error updating hold status", e)
            }
    }

    fun parseExpiryDate(expiryTimestamp: String): Date {
        val timestamp = expiryTimestamp.toLongOrNull() ?: return Date()
        return Date(timestamp)
    }

    private fun isHoldOverdue(hold: Hold): Boolean {
        val expiryDate = parseExpiryDate(hold.dueTimestamp ?: "")
        val currentDate = Date()
        return currentDate.after(expiryDate)
    }

    private fun calculateFineAmount(hold: Hold): Double {
        val expiryDate = parseExpiryDate(hold.dueTimestamp ?: "")
        val currentDate = Date()

        val millisecondsPerDay = 24 * 60 * 60 * 1000
        val daysLate = ((currentDate.time - expiryDate.time) / millisecondsPerDay).toInt()
        val finePerDay = 1 // Example: $1 per day late
        return (daysLate * finePerDay).toDouble()
    }

    // Function to add fine to fines collection in Firestore
    private fun addFineToFirestore(hold: Hold, fineAmount: Double) {
        val fineData = hashMapOf(
            "userId" to hold.userId,
            "title" to "Late Return Penalty",
            "issueTimestamp" to hold.dueTimestamp,
            "paidTimestamp" to null,
            "fineAmount" to fineAmount.toString(),
            "status" to FineStatus.PENDING.toString(),
            // Add other fine data as needed
        )

        db.collection("fines")
            .add(fineData)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "Fine document added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding fine document", e)
            }
    }

    fun getFine(collectionName: String,
                userId: String?,
                onSuccess: (List<Fine>) -> Unit,
                onFailure: (Exception) -> Unit,
                onEmpty: () -> Unit
    ) {
        db.collection(collectionName)
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result: QuerySnapshot ->
                val fineList = mutableListOf<Fine>()

                if (result.isEmpty) { // Check if there are no reservations
                    onEmpty() // Invoke the callback for handling empty reservations
                    return@addOnSuccessListener
                }

                for (document in result.documents) {
                    val userId = document.getString("userId") ?: ""
                    val title = document.getString("title") ?: ""
                    val issueTimestamp = document.getString("issueTimestamp") ?: ""
                    val paidTimestamp = document.getString("paidTimestamp")?: ""
                    val fineAmount = document.getString("fineAmount")?: ""
                    val statusString = document.getString("status") ?: ""

                    // Convert statusString to HoldType enum
                    val status = when (statusString) {
                        "PAID" -> FineStatus.PAID
                        "PENDING" -> FineStatus.PENDING
                        "EXPIRED" -> FineStatus.EXPIRED
                        "CANCELED" -> FineStatus.CANCELED
                        else -> null
                    }

                    val fine = Fine(id = document.id, userId, title, issueTimestamp, paidTimestamp, fineAmount, status)

                    fineList.add(fine)
                }

                onSuccess(fineList)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }

    fun removeFine(
        userId: String?,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit,
    ){
        db.collection("fines")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Iterate through fines associated with the user
                for (document in querySnapshot.documents) {
                    // Delete each fine document
                    db.collection("fines")
                        .document(document.id)
                        .delete()
                        .addOnSuccessListener {
                            onSuccess() // Invoke success callback when fine is removed successfully
                        }
                        .addOnFailureListener { exception ->
                            onFailure(exception) // Invoke failure callback if deleting fine fails
                        }
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception) // Invoke failure callback if fetching fines fails
            }
    }
}