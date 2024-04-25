package com.example.libbit.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId

data class Book(
    @DocumentId val id: String ?= null,
    val isbn: String? = null,
    val title:String? = null,
    val bookImage:String? = null,
    val description:String? = null,
    val author:String? = null,
    val price:String? = null,
    val type: HoldType? = null,
    val status: BookStatus? = null

) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        HoldType.valueOf(parcel.readString()?: ""),
        BookStatus.valueOf(parcel.readString()?: "")
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(isbn)
        parcel.writeString(title)
        parcel.writeString(bookImage)
        parcel.writeString(description)
        parcel.writeString(author)
        parcel.writeString(price)
        parcel.writeString(type?.name)
        parcel.writeString(status?.name)
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

typealias BookDetailsProvider = (String) -> Book?

enum class HoldType{
    PHYSICAL_BOOK,
    EBOOK;
}

enum class BookStatus{
    AVAILABLE,
    ON_HOLD,
    PURCHASED,
    DAMAGED,
    LOST;
}
