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
import com.project.domotique.viewModels.AuthViewModel
import com.project.domotique.utils.LocalStorageManager

class LoginActivity : AppCompatActivity() {

    private lateinit var loginInput : EditText
    private lateinit var passwordInput : EditText
    private lateinit var loginButton : Button
    private lateinit var registerButton : TextView



    private val loginViewModel: AuthViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        this.goToRegisterPage()
        this.observeLoginUiState()
        this.login()
    }


    private fun observeLoginUiState()
    {
        this.loginViewModel.authState.observe(this) { state ->
            state.errors?.let { errorMessage ->
                Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
            if(state.success) {
                println("je suis arrivé dans la vue effectivement")
                println(state.data)
                state.data?.let{ token ->
                    val localStorageManager = LocalStorageManager(this@LoginActivity)
                    this.loginInput = findViewById(R.id.login_page_email_input)
                    localStorageManager.saveUserName(loginInput.text.toString())
                    localStorageManager.saveToken(token)
                    val intentToHomePage = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intentToHomePage)
                }
            }
        }
    }

    private fun login() {
        this.loginButton = findViewById(R.id.login_page_login_button)
        this.loginButton.setOnClickListener{
            loginInput = findViewById(R.id.login_page_email_input)
            passwordInput = findViewById(R.id.login_page_password_input)
            this.loginViewModel.authenticateUser(loginInput.text.toString(), passwordInput.text.toString())
        }
    }

    private fun goToRegisterPage()
    {
        this.registerButton = findViewById(R.id.login_page_register_btn_text_view)
        this.registerButton.setOnClickListener {
            val intentToRegister = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intentToRegister)
        }
    }


}