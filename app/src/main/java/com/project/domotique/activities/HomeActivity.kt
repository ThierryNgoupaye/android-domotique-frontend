package com.project.domotique.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.project.domotique.R
import com.project.domotique.fragments.HomeFragment
import com.project.domotique.utils.LocalStorageManager

class HomeActivity : AppCompatActivity() {

    private lateinit var frameLayout: FrameLayout

    private lateinit var logoutBtn: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_actitvity)
        //this.loadHeader()
        this.logOut()
        this.loadHomeFragment()
    }


    /*
    private fun loadHeader() {
        val localStorageManager = LocalStorageManager(this)
        val userName = localStorageManager.getUserName()
        val userNameText = findViewById<TextView>(R.id.userNameText)
        val avatar = findViewById<ImageView>(R.id.avatarImage)
        userNameText.text = userName
        val safeName = userName?.trim()?.lowercase()?.replace(" ", "+")
        val url = "https://ui-avatars.com/api/?name=$safeName&size=128&background=E0E0E0&color=222222&rounded=true&bold=true"
        Glide.with(this)
            .load(url)
            .circleCrop()
            .into(avatar)
    }

     */


    private fun loadHomeFragment()
    {
        this.frameLayout = findViewById(R.id.main_content_frame_layout)
        val fragmentManager = supportFragmentManager.beginTransaction()
        fragmentManager.apply{
            replace(R.id.main_content_frame_layout, HomeFragment())
            commit()
        }
    }


    private fun logOut()
    {
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