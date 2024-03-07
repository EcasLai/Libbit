package com.example.libbit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.libbit.model.Book
import com.example.libbit.adapter.BookAdapter
import com.example.libbit.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        // Initialize NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragmentCont) as NavHostFragment
        val navController = navHostFragment.navController

        //Update Bottom Nav Visibility
        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateBottomNavigationVisibility(destination.id)
        }

        binding.bottomNavigationView.setupWithNavController(navController)

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {

        }
    }

    private fun updateBottomNavigationVisibility(fragmentId: Int) {
        val bottomNavigationVisibleFragments = setOf(R.id.homeFragment, R.id.bookFragment, R.id.profileFragment, R.id.savedFragment, R.id.notificationActivities)
        binding.bottomNavigationView.visibility =
            if (fragmentId in bottomNavigationVisibleFragments) View.VISIBLE else View.GONE
    }

    private fun verifySignInCondition(){
        if (AuthenticationManager.getCurrentUser() != null) {
            // User is signed in, navigate to the main screen or perform any other required actions
        } else {
            // User is not signed in, navigate to the sign-in screen or perform any other required actions
        }
    }

}

