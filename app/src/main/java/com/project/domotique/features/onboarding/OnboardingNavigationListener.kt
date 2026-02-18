package com.project.domotique.features.onboarding

interface OnboardingNavigationListener {

    fun goToNextFragment()

    fun goToPreviousFragment()

    fun goToAnyFragment(position: Int)

}