package com.project.domotique.features.home.devices.presentation.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import com.project.domotique.R
import com.project.domotique.features.home.devices.domain.entities.DeviceEntity

class PlaceOrderDialog(
    context: Context,
    private val device: DeviceEntity,
    private val onCommandSelected: (DeviceEntity, String) -> Unit
) : Dialog(context) {

    private lateinit var placeOrderBtn : Button
    private lateinit var cancelBtn: Button
    private lateinit var closePopupImageBtn: ImageButton
    private lateinit var availableCommandSpinner : Spinner
    private lateinit var deviceName: TextView
    private lateinit var deviceNameIcon : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_place_command_popup)
        window?.setBackgroundDrawable(android.R.color.transparent.toDrawable())
        this.setupView()
        this.closeDialog()
        this.placeCommand()
        this.cancelAction()
    }

    private fun setupView() {
        this.deviceName = findViewById(R.id.device_name_input)
        this.deviceNameIcon = findViewById(R.id.device_name_icon)
        this.availableCommandSpinner  = findViewById(R.id.device_name_available_commands_input)
        var name: String
        when (device.type) {
            DeviceEntity.TypeDevice.ROLLING_SHUTTER -> {
                name ="${device.type.label} ${device.id.split(" ").getOrNull(1) ?: ""} (${device.opening?.label})"
                this.deviceNameIcon.setImageResource(R.drawable.ic_icons8_window_shade_50)
            }
            DeviceEntity.TypeDevice.LIGHT -> {
                name = "${device.type.label} ${device.id.split(" ").getOrNull(1) ?: ""} (${device.power?.label})"
                this.deviceNameIcon.setImageResource(R.drawable.ic_icons8_light_50)
            }
            else -> {
                name = "${device.type.label} ${device.id.split(" ").getOrNull(1) ?: ""} (${device.opening?.label})"
                this.deviceNameIcon.setImageResource(R.drawable.ic_icons8_door_sensor_checked_50)
            }
        }
        deviceName.text = name

        val commands = device.availableCommands.map { it.command.label }
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, commands)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        availableCommandSpinner.adapter = adapter
    }


    private fun placeCommand() {
        this.placeOrderBtn = findViewById(R.id.place_order_button)
        this.placeOrderBtn.setOnClickListener {
            val selectedCommand = availableCommandSpinner.selectedItem.toString()
            onCommandSelected(device, selectedCommand)
            dismiss()
        }
    }

    private fun cancelAction()
    {
        this.cancelBtn = findViewById(R.id.cancel_button)
        this.cancelBtn.setOnClickListener {
            dismiss()
        }
    }


    private fun closeDialog()
    {
        this.closePopupImageBtn = findViewById(R.id.dialog_close_icon_btn)
        this.closePopupImageBtn.setOnClickListener {
            dismiss()
        }
    }


}