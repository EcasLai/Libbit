package com.example.libbit

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.libbit.databinding.FragmentProfileBinding
import com.example.libbit.util.AuthenticationManager
import com.example.libbit.util.UserManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Set username profileImg for current user
        if (AuthenticationManager.getCurrentUser() == null) {
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        } else {
            binding.profileNameTv.setText(UserManager.getCurrentUser()?.displayName)
            Glide.with(this)
                .load(UserManager.getCurrentUser()?.photoUrl)
                .into(binding.profileImg)
        }
        //Profile Image setting
        binding.profileImg.setOnClickListener{
            // Open gallery to choose photo
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_CHOOSE)
        }

        binding.cvLogOut.setOnClickListener{
            AuthenticationManager.signOut( findNavController())
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

        uploadTask.addOnSuccessListener { taskSnapshot ->
            profileImageRef.downloadUrl.addOnSuccessListener { uri ->
                // Update user profile photo URI
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setPhotoUri(uri)
                    .build()

                auth.currentUser?.updateProfile(profileUpdates)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(requireContext(), "Profile photo updated successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Failed to update profile photo", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(requireContext(), "Upload failed: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val IMAGE_CHOOSE = 1000
        private var selectedImageUri: Uri? = null
    }
}