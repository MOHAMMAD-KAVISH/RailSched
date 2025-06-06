package com.example.railsched

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ScheduleFragment()
            1 -> ForecastFragment()
            2 -> ResourcesFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}