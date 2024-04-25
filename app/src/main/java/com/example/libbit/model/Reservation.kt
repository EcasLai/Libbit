package com.example.libbit.model
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId

data class Reservation(
    @DocumentId val id: String? = null,
    val userId: String? = null,
    val bookId: String? = null,
    val type: HoldType? = null,
    val timestamp: String? = null,
    val expirationTimestamp: String? = null,
    val location: String? = null,
    val status: ReservationStatus? = null
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        HoldType.valueOf(parcel.readString()?: ""),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        ReservationStatus.valueOf(parcel.readString()?: "")
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(userId)
        parcel.writeString(bookId)
        parcel.writeString(type?.name)
        parcel.writeString(timestamp)
        parcel.writeString(expirationTimestamp)
        parcel.writeString(location)
        parcel.writeString(status?.name)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Reservation> {
        override fun createFromParcel(parcel: Parcel): Reservation {
            return Reservation(parcel)
        }

        override fun newArray(size: Int): Array<Reservation?> {
            return arrayOfNulls(size)
        }
    }

}

enum class ReservationStatus{
    RESERVED,
    FULFILLED,
    EXPIRED,
    CANCELED;
}
