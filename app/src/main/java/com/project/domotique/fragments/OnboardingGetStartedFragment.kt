package com.project.domotique.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.project.domotique.R
import com.project.domotique.interfaces.OnboardingNavigationListener

class OnboardingGetStartedFragment(): Fragment() {


    private lateinit var  getStartedBtn : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_onboarding_get_started, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getStartedBtn = view.findViewById(R.id.onboarding_get_started_button)
        getStartedBtn.setOnClickListener {
            (activity as? OnboardingNavigationListener)?.goToNextFragment()
        }
    }
}