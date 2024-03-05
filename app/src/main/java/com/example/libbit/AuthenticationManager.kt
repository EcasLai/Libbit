package com.example.libbit
import com.google.android.gms.tasks.NativeOnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object AuthenticationManager {
    private lateinit var auth: FirebaseAuth

    init {
        auth = FirebaseAuth.getInstance()
    }

    fun register(email: String, password: String, onComplete: (Boolean, String?) -> Unit){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    fun signIn(email: String, password: String, onComplete: (Boolean, String?) -> Unit){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful){
                    onComplete(true, null)
                } else{
                    onComplete(false, task.exception?.message)
                }
            }
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

    fun signOut(){
        auth.signOut()
    }

    fun getCurrentUser(): FirebaseUser?{
        return auth.currentUser
    }
}