package com.example.libbit.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object UserManager {
    private lateinit var auth: FirebaseAuth

    init {
        auth = FirebaseAuth.getInstance()
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun getUserInfo(): UserInfo? {
        val user = getCurrentUser()
        return if (user != null) {
            UserInfo(
                uid = user.uid,
                email = user.email,
                displayName = user.displayName,
                photoUrl = user.photoUrl?.toString()
            )
        } else {
            null
        }
    }

    data class UserInfo(
        val uid: String,
        val email: String?,
        val displayName: String?,
        val photoUrl: String?
    )
}
