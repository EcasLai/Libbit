package com.example.libbit.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId

data class Purchase(
    @DocumentId val id: String? = null,
    val userId: String? = null,
    val bookId: String? = null,
    val type: HoldType? = null,
    val purchaseTimeStamp: String? = null,
    val licenseKey: String? = null
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
        parcel.writeString(purchaseTimeStamp)
        parcel.writeString(licenseKey)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Purchase> {
        override fun createFromParcel(parcel: Parcel): Purchase {
            return Purchase(parcel)
        }

        override fun newArray(size: Int): Array<Purchase?> {
            return arrayOfNulls(size)
        }
    }

}

