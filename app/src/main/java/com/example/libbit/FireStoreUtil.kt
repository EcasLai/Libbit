package com.example.libbit

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.example.libbit.model.Book
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

//                for (document in result.documents) {
//                    // Convert Firestore document into Book object
//                    val book = document.toObject(Book::class.java)
//                    book?.let {
//                        bookList.add(it)
//                    }
//                }

                for (document in result.documents) {
                    val isbn = document.getString("isbn") ?: ""
                    val title = document.getString("title") ?: ""
                    val bookImage = document.getString("bookImage") ?: ""
                    val author = document.getString("author")?: ""
                    val book = Book(id = document.id, isbn, title, bookImage, author)

                    bookList.add(book)
                }

                onSuccess(bookList)
            }
            .addOnFailureListener { exception ->
                // Handle any errors
                onFailure(exception)
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
//            .get()
//            .addOnSuccessListener { result ->
//                val bookList = ArrayList<Book>()
//                for (document in result) {
//                    val book = document.toObject(Book::class.java)
//                    bookList.add(book)
//                }
//                callback(bookList)
//            }
//            .addOnFailureListener { exception ->
//                println("Error getting documents: $exception")
//            }
    }
}