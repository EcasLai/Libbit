package com.example.libbit.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId

data class Book(
    @DocumentId val id: String ?= null,
    val isbn: String? = null,
    val title:String? = null,
    val bookImage:String? = null,
    val author:String? = null

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(isbn)
        parcel.writeString(title)
        parcel.writeString(bookImage)
        parcel.writeString(author)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }
    }
}
