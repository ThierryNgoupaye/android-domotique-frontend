package com.project.domotique.shared

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import com.project.domotique.R

class LoadingDialog(
    context: Context,
    private val message: String = "Chargement..."
) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_loading)
        
        // Background transparent
        window?.setBackgroundDrawable(android.R.color.transparent.toDrawable())
        
        // Non annulable par clic extérieur
        setCancelable(false)
        
        // Configure le message
        findViewById<TextView>(R.id.loading_text).text = message
    }


    companion object {
        fun show(context: Context, message: String = "Chargement..."): LoadingDialog {
            val dialog = LoadingDialog(context, message)
            dialog.show()
            return dialog
        }
    }
}