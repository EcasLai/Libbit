package com.example.libbit.model

import com.google.firebase.firestore.DocumentId

data class Transaction(
    @DocumentId val transactionId: String? = null,
    val userId: String? = null,
    val paymentAmount: Double? = null,
    val status: TransactionStatus? = null
)

enum class TransactionStatus {
    PENDING,
    COMPLETED,
    CANCELED
}