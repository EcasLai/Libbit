package com.example.libbit

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.libbit.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment(){

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //navigation
        binding.tvLoginSkip.setOnClickListener{
            findNavController().popBackStack()
        }

        binding.tvLoginSignUp.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }


        binding.txtLoginEmail.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validateEmail()
            }
        }

        binding.txtLoginPassword.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                validatePassword()
            }
        }

        // Set up click listener for the login button
        binding.btnLogin.setOnClickListener {
            val email = binding.txtLoginEmail.text.toString() // Use text property directly from binding
            val password = binding.txtLoginPassword.text.toString() // Use text property directly from binding

            if (email.isEmpty() || !isValidEmail(email)) {
                validateEmail()
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 6) {
                validatePassword()
                return@setOnClickListener
            }

            AuthenticationManager.signIn(email, password) { success, message ->
                if (success) {
                    Toast.makeText(requireContext(), "Sign in successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Sign in failed: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun validateEmail() {
        val email = binding.txtLoginEmail.text.toString().trim()
        if (email.isEmpty() || !isValidEmail(email)) {
            binding.txtLoginInputEmail.error = "Invalid email format"
        } else {
            binding.txtLoginInputEmail.error = null
        }
    }

    private fun validatePassword() {
        val password = binding.txtLoginPassword.text.toString()
        if (password.isEmpty() || password.length < 6) {
            binding.txtLoginInputPassword.error = "Password must be at least 6 characters long"
        } else {
            binding.txtLoginInputPassword.error = null
        }
    }

    //Check email entered in correct format
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return emailRegex.toRegex().matches(email)
    }
}