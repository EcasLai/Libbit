package com.example.libbit.model

import com.google.firebase.firestore.DocumentId

data class Book(
    @DocumentId val id: String ?= null,
    val isbn: String? = null,
    val title:String? = null,
    val bookImage:String? = null,
    val author:String? = null

):java.io.Serializable
