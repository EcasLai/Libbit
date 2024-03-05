package com.example.libbit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.libbit.databinding.ActivityLoginBinding
import com.example.libbit.AuthenticationManager

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up UI listeners
        binding.btnRegister.setOnClickListener {
            val email = binding.txtRegisterEmail.text.toString() // Use text property directly from binding
            val password = binding.txtRegisterPassword.text.toString() // Use text property directly from binding

            AuthenticationManager.signIn(email, password) { isSuccess, errorMessage ->
                if (isSuccess) {
                    startActivity(Intent(this, MainActivity::class.java))

                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                } else {
                    // Authentication failed, display error message
                    Toast.makeText(this, errorMessage ?: "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}