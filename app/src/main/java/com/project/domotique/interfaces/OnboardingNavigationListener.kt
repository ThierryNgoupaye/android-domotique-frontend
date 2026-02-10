package com.project.domotique.interfaces

interface OnboardingNavigationListener {

    fun goToNextFragment()

    fun goToPreviousFragment()

    fun goToAnyFragment(position: Int)

}