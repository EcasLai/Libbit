package com.example.libbit.model
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId

data class Hold(
    @DocumentId val id: String? = null,
    val userId: String? = null,
    val bookId: String? = null,
    val type: HoldType? = null,
    val timestamp: String? = null,
    val expirationTimestamp: String? = null
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        HoldType.valueOf(parcel.readString()?: ""),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(userId)
        parcel.writeString(bookId)
        parcel.writeString(type?.name)
        parcel.writeString(timestamp)
        parcel.writeString(expirationTimestamp)

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
