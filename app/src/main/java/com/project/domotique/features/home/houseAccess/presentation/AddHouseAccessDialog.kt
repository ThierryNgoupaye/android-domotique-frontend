package com.project.domotique.features.home.houseAccess.presentation

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import com.project.domotique.R

class AddHouseAccessDialog(
    context: Context,
    private val onAccessGranted: (userLogin: String, onResult: (success: Boolean, error: String?) -> Unit) -> Unit
) : Dialog(context) {

    private lateinit var errorText: TextView
    private lateinit var userLoginInput: EditText
    private lateinit var addAccessButton: Button
    private lateinit var closeDialogBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_grant_house_access_popup)
        window?.setBackgroundDrawable(android.R.color.transparent.toDrawable())
        this.initViewWidgets()
        this.addAccess()
        this.closeDialog()
    }

    private fun initViewWidgets() {
        this.errorText = findViewById(R.id.errors_text)
        this.userLoginInput = findViewById(R.id.grant_access_user_login_input)
        this.addAccessButton = findViewById(R.id.grant_acces_button)
        this.closeDialogBtn = findViewById(R.id.dialog_close_icon_btn)
    }

    private fun addAccess() {
        this.addAccessButton.setOnClickListener {
            val userLogin = this.userLoginInput.text.toString()
            if (userLogin.isEmpty()) {
                this.showError("Veuillez renseigner le nom d'utilisateur")
                return@setOnClickListener
            }
            this.addAccessButton.isEnabled = false
            this.addAccessButton.text = "Envoi..."
            this.errorText.visibility = View.GONE
            this.onAccessGranted(userLogin) { success, error ->
                this.addAccessButton.isEnabled = true
                this.addAccessButton.text = "Accorder l'accès"
                if (success) {
                    dismiss()
                } else {
                    this.showError(error ?: "Une erreur est survenue")
                }
            }
        }
    }

    private fun showError(message: String) {
        this.errorText.visibility = View.VISIBLE
        this.errorText.text = message
    }

    private fun closeDialog() {
        this.closeDialogBtn.setOnClickListener {
            dismiss()
        }
    }
}