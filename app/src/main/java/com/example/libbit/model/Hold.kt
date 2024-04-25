package com.example.libbit.model
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId

data class Hold(
    @DocumentId val id: String? = null,
    val userId: String? = null,
    val bookId: String? = null,
    val type: HoldType? = null,
    val holdTimestamp: String? = null,
    val dueTimestamp: String? = null,
    val licenseKey: String? = null,
    val status: HoldStatus? = null
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        HoldType.valueOf(parcel.readString()?: ""),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        HoldStatus.valueOf(parcel.readString()?: "")
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(userId)
        parcel.writeString(bookId)
        parcel.writeString(type?.name)
        parcel.writeString(holdTimestamp)
        parcel.writeString(dueTimestamp)
        parcel.writeString(licenseKey)
        parcel.writeString(status?.name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Hold> {
        override fun createFromParcel(parcel: Parcel): Hold {
            return Hold(parcel)
        }

        override fun newArray(size: Int): Array<Hold?> {
            return arrayOfNulls(size)
        }
    }


}

enum class HoldStatus{
    PURCHASED,
    HOLDING,
    OVERDUE,
    COMPLETED,
    RETURNED;
}

