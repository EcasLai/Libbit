package com.example.libbit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.libbit.databinding.ActivityMainBinding
import com.example.libbit.util.AuthenticationManager
import com.example.libbit.util.UserManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
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

        UserManager.setDefaultUsernameIfEmpty()

        // Initialize NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragmentCont) as NavHostFragment
        navController = navHostFragment.navController

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
        val bottomNavigationVisibleFragments = setOf(R.id.homeFragment, R.id.reservationFragment, R.id.profileFragment, R.id.savedFragment, R.id.fineFragment)
        binding.bottomNavigationView.visibility =
            if (fragmentId in bottomNavigationVisibleFragments) View.VISIBLE else View.GONE
    }

    fun updateSelectedItem(itemId: Int) {
        // Find the BottomNavigationView by its ID
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Set the selected item
        bottomNavigationView.selectedItemId = itemId
    }

    private fun verifySignInCondition(){
        if (AuthenticationManager.getCurrentUser() != null) {
            // User is signed in, navigate to the main screen or perform any other required actions
        } else {
            // User is not signed in, navigate to the sign-in screen or perform any other required actions
        }
    }

}

