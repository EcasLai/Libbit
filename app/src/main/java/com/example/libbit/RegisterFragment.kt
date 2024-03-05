package com.example.libbit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.libbit.databinding.FragmentRegisterBinding
import java.util.zip.Inflater

class Register: Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up click listener for the register button
        binding.btnRegister.setOnClickListener {
            val email = binding.txtRegisterEmail.text.toString()
            val password = binding.txtRegisterPassword.text.toString()

            // Call AuthenticationManager to register
            AuthenticationManager.register(email, password) { isSuccess, errorMessage ->
                if (isSuccess) {
                    // Registration successful, display success message or perform action
                    Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show()
                    // Example: navigate to login screen or directly log in the user
                } else {
                    // Registration failed, display error message
                    Toast.makeText(requireContext(), errorMessage ?: "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}