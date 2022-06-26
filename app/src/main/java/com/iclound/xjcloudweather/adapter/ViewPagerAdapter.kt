package com.iclound.xjcloudweather.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fm: FragmentManager, var list: List<Fragment>) : FragmentStatePagerAdapter(
        fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    override fun getCount(): Int  = list.size

    override fun getItem(position: Int): Fragment {
        return list[position]
    }
}