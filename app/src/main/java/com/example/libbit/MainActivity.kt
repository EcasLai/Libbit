package com.example.libbit

import android.annotation.SuppressLint
import android.content.Context
import   android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.libbit.databinding.ActivityMainBinding
import com.example.libbit.model.LibraryViewModel
import com.example.libbit.util.PreferenceManager
import com.example.libbit.util.AuthenticationManager
import com.example.libbit.util.Constants
import com.example.libbit.util.UserManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import link.magic.android.Magic
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import org.web3j.protocol.core.methods.request.Transaction.createEtherTransaction
import org.web3j.protocol.geth.Geth
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger
import java.util.prefs.Preferences

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    lateinit var navController: NavController
    private lateinit var web3j: Web3j
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var libraryViewModel: LibraryViewModel
    private var deployedSmartContractAddress: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        UserManager.setDefaultUsernameIfEmpty()

        //Blockchain
        libraryViewModel = ViewModelProvider(this)[LibraryViewModel::class.java]
        preferenceManager = PreferenceManager(this)

        deployedSmartContractAddress =
            preferenceManager.getString("deployedSmartContractAddress")

        // Add a dummy log message for testing
        Log.d("MainActivity", "Dummy log message for testing")


        if (deployedSmartContractAddress.isNullOrEmpty() || deployedSmartContractAddress=="-1") {
            libraryViewModel.deploySmartContract(getCredentialsFromPrivateKey())
        } else {
            Toast.makeText(this, "Deploy Successful", Toast.LENGTH_LONG).show()
        }

        observers()


        // Initialize NavController
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragmentCont) as NavHostFragment
        navController = navHostFragment.navController

        //Update Bottom Nav Visibility
        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateBottomNavigationVisibility(destination.id)
        }
        binding.bottomNavigationView.setupWithNavController(navController)

        web3j = Web3j.build(HttpService(Constants.GOERLI_TESTNET))

    }

    private fun observers() {
        libraryViewModel.contractAddress.observe(this) {
            if (it != "-1") {
                preferenceManager.putString("deployedSmartContractAddress", it)
                Toast.makeText(this, "Smart Contract Deployed", Toast.LENGTH_SHORT).show()
            } else {

                Toast.makeText(this, "Smart Contract Connecting fail", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getCredentialsFromPrivateKey(): Credentials {
        val privateKey = "0x87E310Ee41A3A8B80f640828b54aB9C1E4bE86D9"
        return Credentials.create(privateKey)
    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Toast.makeText(this, "Kindly Login to access more contents", Toast.LENGTH_SHORT).show()
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

