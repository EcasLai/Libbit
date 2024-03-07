package com.example.libbit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.libbit.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.zip.Inflater

class RegisterFragment: Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        auth = Firebase.auth

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtRegisterEmail.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if(!hasFocus){
                validateEmail()
            }
        }

        binding.txtRegisterPassword.onFocusChangeListener = View.OnFocusChangeListener {_, hasFocus ->
            if(!hasFocus){
                validatePassword()
            }
        }


        // Set up click listener for the register button
        binding.btnRegister.setOnClickListener {
            val email = binding.txtRegisterEmail.text.toString() // Use text property directly from binding
            val password = binding.txtRegisterPassword.text.toString() // Use text property directly from binding

            if (email.isEmpty() || !isValidEmail(email)) {
                validateEmail()
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 6) {
                validatePassword()
                return@setOnClickListener
            }

            AuthenticationManager.register(email, password) { success, message ->
                if (success) {
                    Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Registration failed: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun validateEmail() {
        val email = binding.txtRegisterEmail.text.toString().trim()
        if (email.isEmpty() || !isValidEmail(email)) {
            binding.txtRegisterInputEmail.error = "Invalid email format"
        } else {
            binding.txtRegisterInputEmail.error = null
        }
    }

    private fun validatePassword() {
        val password = binding.txtRegisterPassword.text.toString()
        if (password.isEmpty() || password.length < 6) {
            binding.txtRegisterInputPassword.error = "Password must be at least 6 characters long"
        } else {
            binding.txtRegisterInputPassword.error = null
        }
    }

    //Check email entered in correct format
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return emailRegex.toRegex().matches(email)
    }
}