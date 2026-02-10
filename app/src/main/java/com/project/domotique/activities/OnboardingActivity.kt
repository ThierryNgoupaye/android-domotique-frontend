package com.project.domotique.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.project.domotique.R
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.project.domotique.adapters.ScreenSlidePagerAdapter
import com.project.domotique.animations.DepthPageTransformer
import com.project.domotique.interfaces.OnboardingNavigationListener


class OnboardingActivity : AppCompatActivity(), OnboardingNavigationListener {

    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: ScreenSlidePagerAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var tabLayoutMediator: TabLayoutMediator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_onboarding)
        this.initOnboardingPage()
    }


    private fun initOnboardingPage() {
        viewPager = findViewById(R.id.pager_view)
        viewPager.apply {
            pagerAdapter = ScreenSlidePagerAdapter(fragmentActivity = this@OnboardingActivity)
            adapter = pagerAdapter
            setPageTransformer(DepthPageTransformer())
        }
        tabLayout = findViewById(R.id.page_indicator)
        tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager) { _, _ -> }
        tabLayoutMediator.attach()
    }


    override fun goToNextFragment() {
        val currentItem = viewPager.currentItem
        if (currentItem < pagerAdapter.itemCount - 1) {
            viewPager.setCurrentItem(currentItem + 1, true)
        }
    }


    override fun goToPreviousFragment() {
        val currentItem = viewPager.currentItem
        if (currentItem -1 >= 0 ){
            viewPager.setCurrentItem(currentItem - 1, true)
        }
    }


    override fun goToAnyFragment(position: Int) {
        if(position >= 0 && position <= pagerAdapter.itemCount -1)
        {
            viewPager.setCurrentItem(position, true)
        }
    }




}




