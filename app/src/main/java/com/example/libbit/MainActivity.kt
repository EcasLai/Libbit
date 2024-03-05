package com.example.libbit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.libbit.model.Book
import com.example.libbit.adapter.BookAdapter
import com.example.libbit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //On start Home fragment
        binding.bottomNavigationView.selectedItemId = R.id.bottom_nav_home

        //Bottom Navigation
        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){
                R.id.bottom_nav_book -> replaceFragment(BookFragment())
                R.id.bottom_nav_saved ->  replaceFragment(SavedFragment())
                R.id.bottom_nav_home ->  replaceFragment(HomeFragment())
                R.id.bottom_nav_notification ->  replaceFragment(NotificationActivities())
                R.id.bottom_nav_profile ->  replaceFragment(ProfileFragment())

                else->{

                }

            }
            true
        }



        replaceFragment(HomeFragment())

    }

    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout_main, fragment)
        fragmentTransaction.commit()
    }
}