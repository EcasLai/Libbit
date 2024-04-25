package com.example.libbit.util

import android.content.ContentValues
import android.util.Log
import com.example.libbit.model.Book
import com.example.libbit.model.BookStatus
import com.example.libbit.model.Hold
import com.example.libbit.model.HoldType
import com.example.libbit.model.Reservation
import com.example.libbit.model.ReservationStatus
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

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

                    val book = Book(id = document.id, isbn, title, bookImage,description, author,price,type,status)


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
        onSuccess: (List<Pair<Reservation, Book>>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection(collectionName)
            .get()
            .addOnSuccessListener { result ->
                val reservationsWithBooks = mutableListOf<Pair<Reservation, Book>>()

                for (document in result.documents) {
                    val userId = document.getString("userId") ?: ""
                    val bookId = document.getString("bookId") ?: ""
                    val typeString = document.getString("type") ?: ""
                    val timestamp = document.getString("timestamp") ?: ""
                    val expirationTimestamp = document.getString("expirationTimestamp") ?: ""
                    val location =  document.getString("location") ?: ""
                    val statusString = document.getString("status") ?: ""

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

                                val book = Book(
                                    id = bookDocument.id,
                                    isbn = isbn,
                                    title = title,
                                    bookImage = bookImage,
                                    description = description,
                                    author = author,
                                    price = price,
                                    type = type
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
}