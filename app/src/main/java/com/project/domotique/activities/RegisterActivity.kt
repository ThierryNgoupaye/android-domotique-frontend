package com.project.domotique.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

import com.project.domotique.R
import com.project.domotique.utils.LocalStorageManager
import com.project.domotique.viewModels.AuthViewModel
import kotlin.getValue

class RegisterActivity : AppCompatActivity() {


    private lateinit var loginInput : EditText
    private lateinit var passwordInput : EditText
    private lateinit var confirmPasswordInput : EditText
    private lateinit var loginButton : TextView
    private lateinit var registerButton: Button

    private val authViewModel: AuthViewModel by viewModels()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        this.goToLogin()
        this.observeRegisterUi()
        this.register()
    }



    private fun observeRegisterUi() {
        this.authViewModel.authState.observe(this) { state ->
            state.errors?.let { errorMessage ->
                println(errorMessage)
                Toast.makeText(this@RegisterActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
            if(state.success)
            {
                val localStorageManager = LocalStorageManager(this)
                localStorageManager.saveOnboardingPreference(true)
                val intentToLogin = Intent(this, LoginActivity::class.java)
                startActivity(intentToLogin)
            }
        }
    }



        private fun register() {
            this.registerButton = findViewById(R.id.register_page_register_button)
            this.registerButton.setOnClickListener {
                loginInput = findViewById(R.id.register_page_email_input)
                passwordInput = findViewById(R.id.register_page_password_input)
                confirmPasswordInput = findViewById(R.id.register_page_confirm_password_input)
                this.authViewModel.registerUser(
                    loginInput.text.toString(),
                    passwordInput.text.toString(),
                    confirmPasswordInput.text.toString()
                )
            }
        }

        private fun goToLogin() {
            this.loginButton = findViewById(R.id.register_page_login_button)
            this.loginButton.setOnClickListener {
                val intentToLogin = Intent(this, LoginActivity::class.java)
                startActivity(intentToLogin)
            }
        }


}

