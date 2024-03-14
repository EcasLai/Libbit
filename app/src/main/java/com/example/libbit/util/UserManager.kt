package com.example.libbit.util

import android.net.Uri
import androidx.navigation.NavController
import com.example.libbit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

object UserManager {
    private lateinit var auth: FirebaseAuth
    private val storage = FirebaseStorage.getInstance()

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

    // Function to update user's display name
    fun updateDisplayName(displayName: String, onComplete: (Boolean) -> Unit) {
        val user = auth.currentUser
        if (user != null) {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName)
                .build()

            user.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    onComplete(task.isSuccessful)
                }
        } else {
            onComplete(false)
        }
    }

    fun changeProfilePhoto(imageUri: Uri, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val storageRef: StorageReference = storage.reference
        val profileImageRef = storageRef.child("ProfilePic/${auth.currentUser!!.uid}")

        val uploadTask = profileImageRef.putFile(imageUri)

        uploadTask.addOnSuccessListener { taskSnapshot ->
            profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                // Update user profile photo URI
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setPhotoUri(uri)
                    .build()

                auth.currentUser?.updateProfile(profileUpdates)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onSuccess()
                        } else {
                            onFailure("Failed to update profile photo")
                        }
                    }
            }
        }.addOnFailureListener { exception ->
            onFailure("Upload failed: ${exception.message}")
        }
    }

    //Login


    fun setDefaultUsernameIfEmpty() {
        val currentUser = auth.currentUser
        if (currentUser != null && currentUser.displayName.isNullOrEmpty()) {
            val defaultUsername = "Libbitor_${currentUser.uid.substring(0, 5)}" // Default username based on UID
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(defaultUsername)
                .build()

            currentUser.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Username updated successfully
                    } else {
                        // Failed to update username
                    }
                }
        }
    }

    data class UserInfo(
        val uid: String,
        val email: String?,
        val displayName: String?,
        val photoUrl: String?
    )
}
