package com.project.domotique.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.project.domotique.fragments.OnboardingEndPageFragment
import com.project.domotique.fragments.OnboardingGetStartedFragment
import com.project.domotique.fragments.OnboardingMiddlePageFragment


class ScreenSlidePagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnboardingGetStartedFragment()
            1 -> OnboardingMiddlePageFragment()
            2 -> OnboardingEndPageFragment()
            else -> OnboardingGetStartedFragment()
        }
    }
}