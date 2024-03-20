package com.example.libbit.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.lifecycle.Lifecycle

class ViewPagerAdapter(list: ArrayList<Fragment>, fm :  FragmentManager, lifecycle : Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {
    private val fragmentList = list

    override fun getItemCount(): Int {
         return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

}