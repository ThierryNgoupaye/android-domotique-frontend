package com.project.domotique.features.home

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.domotique.R
import com.project.domotique.features.auth.presentation.ui.LoginActivity
import com.project.domotique.shared.ConfirmPopupDialog
import com.project.domotique.shared.CongratulationDialog
import com.project.domotique.utils.LocalStorageManager

class HomeActivity : AppCompatActivity() {
    private lateinit var logoutBtn: ImageButton
    private val homeSharedViewModel: HomeSharedViewModel by viewModels()
    private var confirmPopupDialog: ConfirmPopupDialog? = null
    private var congratulationDialog: CongratulationDialog? = null
    private lateinit var localStorageManager: LocalStorageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        this.localStorageManager = LocalStorageManager(this)
        if (this.localStorageManager.getNewUserPreferences()) {
            this.congratulationDialog = CongratulationDialog(
                context = this,
                title = "Bienvenue ${localStorageManager.getUserName()}",
            ) {
                localStorageManager.saveNewUserPreferences(false)
            }
            this.congratulationDialog?.show()
        }
        this.setupNavigation()
        this.logOut()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.setupWithNavController(navController)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    navController.popBackStack(R.id.homeFragment, false)
                    this.homeSharedViewModel.clearSelection()
                    true
                }
                R.id.deviceFragment -> {
                    if (navController.currentDestination?.id != R.id.deviceFragment) {
                        navController.navigate(R.id.deviceFragment)
                    }
                    true
                }
                R.id.house_access_fragment -> {
                    if (navController.currentDestination?.id != R.id.house_access_fragment) {
                        navController.navigate(R.id.house_access_fragment)
                    }
                    true
                }
                R.id.customCommandFragment -> {
                    if (navController.currentDestination?.id != R.id.customCommandFragment) {
                        navController.navigate(R.id.customCommandFragment)
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun logOut() {
        this.logoutBtn = findViewById(R.id.logoutBtn)
        this.logoutBtn.setOnClickListener {
            this.confirmPopupDialog?.dismiss()
            this.confirmPopupDialog = ConfirmPopupDialog(
                context = this,
                title = "Se déconnecter",
                description = "Êtes vous sur de vouloir vous déconnecter ?",
                onConfirm = {
                    localStorageManager.clearPreferences()
                    startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                    finish()
                }
            )
            this.confirmPopupDialog?.show()
        }
    }
}