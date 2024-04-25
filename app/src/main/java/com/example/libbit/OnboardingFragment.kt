package com.example.libbit

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.libbit.adapter.ViewPagerAdapter
import com.example.libbit.databinding.FragmentOnboardingBinding

class OnboardingFragment : Fragment() {
    private lateinit var binding: FragmentOnboardingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentList = arrayListOf<Fragment>(
            FirstOnboardingScreen(),
            SecondOnboardingScreen(),
            ThirdOnboardingScreen()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        val viewPager = binding.viewPagerSplash

        viewPager.adapter = adapter

        val indicator = binding.dotsIndicator

        indicator.attachTo(viewPager)

        //Call Function createPageChangeCallback
        viewPager.registerOnPageChangeCallback(createPageChangeCallback(fragmentList))

        binding.btnNext.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem < fragmentList.size - 1) {
                viewPager.setCurrentItem(currentItem + 1, true)
            }
        }

        binding.btnGetStarted.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_homeFragment)
        }
    }

    //Check for any page changes, to show different button
    private fun createPageChangeCallback(fragmentList: List<Fragment>): ViewPager2.OnPageChangeCallback {
        return object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == fragmentList.size - 1) {
                    binding.btnGetStarted.visibility = View.VISIBLE
                    binding.btnNext.visibility = View.GONE
                } else {
                    binding.btnGetStarted.visibility = View.GONE
                    binding.btnNext.visibility = View.VISIBLE
                }
            }
        }
    }
}