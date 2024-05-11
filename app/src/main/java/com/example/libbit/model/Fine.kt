package com.example.libbit.model
import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId

data class Fine(
    @DocumentId val id: String? = null,
    val userId: String? = null,
    val title: String? = null,
    val issueTimestamp: String? = null,
    val paidTimestamp: String? = null,
    val fineAmount: String? = null,
    val status: FineStatus? = null
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        FineStatus.valueOf(parcel.readString()?: "")
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(userId)
        parcel.writeString(title)
        parcel.writeString(issueTimestamp)
        parcel.writeString(paidTimestamp)
        parcel.writeString(fineAmount)
        parcel.writeString(status?.name)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Fine> {
        override fun createFromParcel(parcel: Parcel): Fine {
            return Fine(parcel)
        }

        override fun newArray(size: Int): Array<Fine?> {
            return arrayOfNulls(size)
        }
    }

}

enum class FineStatus{
    PAID,
    PENDING,
    EXPIRED,
    CANCELED;
}