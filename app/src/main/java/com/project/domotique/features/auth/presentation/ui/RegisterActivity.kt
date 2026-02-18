package com.project.domotique.features.auth.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.project.domotique.R
import com.project.domotique.shared.LoadingDialog
import com.project.domotique.utils.LocalStorageManager
import com.project.domotique.features.auth.presentation.viewModels.AuthViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var loginInput : EditText
    private lateinit var passwordInput : EditText
    private lateinit var confirmPasswordInput : EditText
    private lateinit var loginButton : TextView
    private lateinit var registerButton: Button
    private lateinit var errorText: TextView
    private var loadingDialog: LoadingDialog? = null

    private lateinit var localStorageManager : LocalStorageManager
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        this.localStorageManager = LocalStorageManager(this)
        this.errorText = findViewById(R.id.register_page_error_text)
        this.goToLogin()
        this.observeRegisterUi()
        this.register()
    }

    private fun observeRegisterUi() {
        this.errorText.visibility = TextView.GONE
        this.authViewModel.authState.observe(this) { state ->
            if(state.loading) this.renderLoadingDialog()
            state.errors?.let { errorMessage ->
                this.dismissLoadingDialog()
                this.errorText.visibility = TextView.VISIBLE
                this.errorText.text = errorMessage
            }
            if(state.success)
            {
                this.dismissLoadingDialog()
                this.localStorageManager.saveOnboardingPreference(true)
                this.localStorageManager.saveNewUserPreferences(true)
                val intentToLogin = Intent(this, LoginActivity::class.java)
                startActivity(intentToLogin)
            }
        }
    }
    private fun register() {
        this.registerButton = findViewById(R.id.register_page_register_button)
        this.registerButton.setOnClickListener {
            loginInput = findViewById(R.id.register_page_username_input)
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

    private fun renderLoadingDialog()
    {
        this.loadingDialog?.dismiss()
        this.loadingDialog = LoadingDialog(this)
        this.loadingDialog?.show()
    }

    private fun dismissLoadingDialog()
    {
        this.loadingDialog?.dismiss()
    }
}