package com.project.domotique.features.onboarding.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.project.domotique.R
import com.project.domotique.features.auth.presentation.ui.LoginActivity
import com.project.domotique.features.auth.presentation.ui.RegisterActivity
import com.project.domotique.features.onboarding.OnboardingActivity

class OnboardingEndPageFragment : Fragment() {

    private lateinit var loginBtn: Button
    private lateinit var registerBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_onboarding_activity_end_page, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerBtn = view.findViewById(R.id.onboarding_register_button)
        loginBtn = view.findViewById(R.id.onboarding_login_button)

        registerBtn.setOnClickListener {
            gotToLogin()
        }

        loginBtn.setOnClickListener {
            goToRegister()
        }
    }

    private fun gotToLogin() {
        val intentToLogin = Intent(activity as? OnboardingActivity, LoginActivity::class.java)
        startActivity(intentToLogin)
    }

    private fun goToRegister() {
        val intentToRegister = Intent(activity as? OnboardingActivity, RegisterActivity::class.java)
        startActivity(intentToRegister)
    }
}