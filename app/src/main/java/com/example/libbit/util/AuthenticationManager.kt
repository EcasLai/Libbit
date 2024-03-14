package com.example.libbit.util

import androidx.navigation.NavController
import com.example.libbit.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


object AuthenticationManager {
    private lateinit var auth: FirebaseAuth

    init {
        auth = FirebaseAuth.getInstance()
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun register(email: String, password: String, navController: NavController, onComplete: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in successful
                    onComplete(true, null)
                    navController.navigate(R.id.homeFragment)
                    UserManager.setDefaultUsernameIfEmpty()
                } else {
                    // Sign in fail
                    onComplete(false, task.exception?.message)
                }
            }
    }

    fun signIn(email: String, password: String, navController: NavController, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        if (!isValidEmail(email)) {
            onFailure("Invalid email format")
            return
        }

        if (password.length < 6) {
            onFailure("Password must be at least 6 characters long")
            return
        }

        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                    navController.navigate(R.id.profileFragment)
                } else {
                    onFailure(task.exception?.message ?: "Sign in failed")
                }
            }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return emailRegex.toRegex().matches(email)
    }


//    fun signInGoogle(email: String, password: String, onComplete: (Boolean, String?) -> Unit){
//        auth.signInWithCredential(email, password)
//            .addOnCompleteListener() { task ->
//                if (task.isSuccessful){
//                    onComplete(true, null)
//                } else{
//                    onComplete(false, task.exception?.message)
//                }
//            }
//    }

    fun signInAsGuest(OnComplete: (Boolean, String?) -> Unit){
        auth.signInAnonymously()
            .addOnCompleteListener() { task ->
                if (task.isSuccessful){
                    OnComplete(true, null)
                } else{
                    OnComplete(false, task.exception?.message)
                }
            }
    }

    fun signOut(navController: NavController) {
        auth.signOut()
        navController.navigate(R.id.homeFragment) // Navigate to the home fragment
    }

}