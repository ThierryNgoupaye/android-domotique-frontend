package com.project.domotique.features.auth.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.project.domotique.R
import com.project.domotique.features.home.HomeActivity
import com.project.domotique.shared.LoadingDialog
import com.project.domotique.utils.LocalStorageManager
import com.project.domotique.features.auth.presentation.viewModels.AuthViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var loginInput : EditText
    private lateinit var passwordInput : EditText
    private lateinit var loginButton : Button
    private lateinit var registerButton : TextView
    private var loadingDialog: LoadingDialog? = null
    private lateinit var errorText: TextView
    private  lateinit var localStorageManager: LocalStorageManager
    private val loginViewModel: AuthViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        this.localStorageManager = LocalStorageManager(this)
        if(this.localStorageManager.getUserLoggedPreferences()){
            val intentToHomePage = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(intentToHomePage)
        }
        setContentView(R.layout.activity_login)
        this.errorText = findViewById(R.id.login_error_text)
        this.goToRegisterPage()
        this.observeLoginUiState()
        this.login()
    }


    private fun observeLoginUiState()
    {
        this.errorText.visibility = TextView.GONE
        this.loginViewModel.authState.observe(this) { state ->
            if(state.loading){
                this.renderLoadingDialog()
            }
            state.errors?.let { errorMessage ->
                this.dismissDialog()
                this.errorText.visibility = View.VISIBLE
                this.errorText.text = errorMessage
            }
            if(state.success) {
                this.dismissDialog()
                state.data?.let{ token ->
                    this.loginInput = findViewById(R.id.login_page_email_input)
                    this.localStorageManager.saveUserName(loginInput.text.toString())
                    this.localStorageManager.saveToken(token)
                    this.localStorageManager.saveUserLoggedPreferences(true)
                    this.localStorageManager.saveOnboardingPreference(true)
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

    private fun renderLoadingDialog()
    {
        this.loadingDialog?.dismiss()
        this.loadingDialog = LoadingDialog(this)
        this.loadingDialog?.show()
    }

    private fun dismissDialog()
    {
        this.loadingDialog?.dismiss()
    }


}