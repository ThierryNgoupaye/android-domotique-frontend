package com.project.domotique.shared

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import com.project.domotique.R

class ConfirmPopupDialog(
    context: Context,
    private val title: String,
    private val description: String,
    private val onConfirm: () -> Unit
) : Dialog(context) {


    private lateinit var popupTitle: TextView
    private lateinit var popupDescription: TextView
    private lateinit var okayButton: Button
    private lateinit var cancelButton: Button
    private lateinit var closePopupImageButton: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.confirm_popup)
        window?.setBackgroundDrawable(android.R.color.transparent.toDrawable())
        this.initViewWidgets()
        this.closePopup()
        this.confirmAction()
    }


    private fun initViewWidgets()
    {
        this.popupTitle = findViewById(R.id.popup_title)
        this.popupDescription = findViewById(R.id.popup_description)
        this.okayButton = findViewById(R.id.okay_button)
        this.cancelButton = findViewById(R.id.cancel_button)
        this.closePopupImageButton = findViewById(R.id.dialog_close_icon_btn)
        this.popupTitle.text = this.title
        this.popupDescription.text = this.description
    }


    private fun confirmAction()
    {
        this.okayButton.setOnClickListener {
            this.onConfirm()
            this.dismiss()
        }
    }

    private fun closePopup()
    {
        this.cancelButton.setOnClickListener {
            this.dismiss()
        }
        this.closePopupImageButton.setOnClickListener {
            this.dismiss()
        }
    }


}