package com.example.libbit

import com.example.libbit.model.Book
import com.google.firebase.firestore.FirebaseFirestore

object FirestoreUtil {

    private lateinit var db: FirebaseFirestore
    private val collectionName = "books"

    init {
        db = FirebaseFirestore.getInstance()
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

    fun getBooks(callback: (ArrayList<Book>) -> Unit) {
        db.collection(collectionName)
            .get()
            .addOnSuccessListener { result ->
                val bookList = ArrayList<Book>()
                for (document in result) {
                    val book = document.toObject(Book::class.java)
                    bookList.add(book)
                }
                callback(bookList)
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    }
}