package com.project.domotique.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.project.domotique.R
import com.project.domotique.interfaces.OnboardingNavigationListener

class OnboardingMiddlePageFragment: Fragment() {

    private lateinit var skipBtn: Button

    private lateinit var nextBtn: Button


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_onboarding_middle_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        skipBtn = view.findViewById(R.id.onboarding_middle_page_skip_btn)
        nextBtn = view.findViewById(R.id.onboarding_middle_page_next_btn)
        skipBtn.setOnClickListener {
            (activity as? OnboardingNavigationListener)?.goToAnyFragment(position=2)
        }
        nextBtn.setOnClickListener {
            (activity as? OnboardingNavigationListener)?.goToNextFragment()
        }
    }

}