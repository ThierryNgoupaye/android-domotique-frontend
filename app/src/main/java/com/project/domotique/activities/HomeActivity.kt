package com.project.domotique.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.domotique.R
import com.project.domotique.utils.LocalStorageManager
import com.project.domotique.viewModels.SharedViewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var logoutBtn: ImageButton

    private val sharedViewModel: SharedViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_actitvity)
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
                    this.sharedViewModel.clearSelection()
                    true
                }
                R.id.deviceFragment -> {
                    if (navController.currentDestination?.id != R.id.deviceFragment) {
                        navController.navigate(R.id.deviceFragment)
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
            val localStorageManager = LocalStorageManager(this)
            localStorageManager.removeToken()
            localStorageManager.removeUserName()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}