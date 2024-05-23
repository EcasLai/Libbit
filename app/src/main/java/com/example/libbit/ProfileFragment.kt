package com.example.libbit

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.libbit.databinding.FragmentProfileBinding
import com.example.libbit.util.AuthenticationManager
import com.example.libbit.util.UserManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private lateinit var fragmentContext: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

        fragmentContext = requireContext() // Store the context

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity = activity as? MainActivity

        //Set username profileImg for current user66666666
        if (AuthenticationManager.getCurrentUser() == null) {
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        } else {
            binding.profileNameTv.setText(UserManager.getCurrentUser()?.displayName)
            Glide.with(this)
                .load(UserManager.getCurrentUser()?.photoUrl)
                .into(binding.profileImg)
        }

        // Set an OnClickListener to handle chip clicks
        binding.chipDarkMode.setOnClickListener {
            // Toggle dark mode
            toggleDarkMode()

            // Update chip's checked state
            binding.chipDarkMode.isChecked = isDarkModeEnabled()
        }

        //Profile Image setting
        binding.profileImg.setOnClickListener{
            // Open gallery to choose photo
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_CHOOSE)
        }

        // Set logout button click listener with confirmation dialog
        binding.cvLogOut.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            // User is signed in
            val uid = user.uid
            val email = user.email
            val displayName = user.displayName
            val photoUrl = user.photoUrl

            // You can use this information as needed, for example:
            Log.d(TAG, "User ID: $uid")
            Log.d(TAG, "Email: $email")
            Log.d(TAG, "Display Name: $displayName")
            Log.d(TAG, "Photo URL: $photoUrl")
        } else {
            // No user is signed in
            Log.d(TAG, "No user is currently signed in.")
        }

        reloadData()
    }

    private fun reloadData() {
        // Load or reload data here
        // For example, you can update the user's profile image and name
        binding.profileNameTv.text = UserManager.getCurrentUser()?.displayName
        Glide.with(this)
            .load(UserManager.getCurrentUser()?.photoUrl)
            .into(binding.profileImg)
    }
    override fun onResume() {
        super.onResume()
        // Reload data whenever the Fragment becomes visible
        reloadData()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CHOOSE && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri = data.data!!
            changeProfilePhoto(selectedImageUri)
        } else {
            // Handle case when user cancels image selection
            // For example, you could display a message or take other action
        }
    }
    private fun changeProfilePhoto(imageUri: Uri) {
        val storageRef = storage.reference
        val profileImageRef = storageRef.child("ProfilePic/${auth.currentUser!!.uid}")

        val uploadTask = profileImageRef.putFile(imageUri)

        binding.profileImgProgress.visibility = View.VISIBLE

        uploadTask.addOnSuccessListener { taskSnapshot ->
            profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                // Update user profile photo URI
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setPhotoUri(uri)
                    .build()

                auth.currentUser?.updateProfile(profileUpdates)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            reloadData()
                            binding.profileImgProgress.visibility = View.GONE
                            Toast.makeText(fragmentContext, "Profile photo updated successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(fragmentContext, "Failed to update profile photo", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }.addOnFailureListener { exception ->
            binding.profileImgProgress.visibility = View.GONE
            Toast.makeText(fragmentContext, "Upload failed: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(fragmentContext)
            .setTitle("Logout")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Yes") { dialog, which ->
                // Perform logout
                AuthenticationManager.signOutFireAuth(findNavController())
                val mainActivity = activity as? MainActivity
                mainActivity?.updateSelectedItem(R.id.homeFragment)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun isDarkModeEnabled(): Boolean {
        // Check if dark mode is enabled
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
    }

    private fun toggleDarkMode() {
        // Toggle dark mode
        val newMode = if (isDarkModeEnabled()) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES
        AppCompatDelegate.setDefaultNightMode(newMode)
    }

    companion object {
        private const val IMAGE_CHOOSE = 10000
        private var selectedImageUri: Uri? = null
    }
}