package com.project.domotique.features.home.customCommands.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.domotique.R
import com.project.domotique.features.home.devices.domain.entities.DeviceEntity
import com.project.domotique.features.home.customCommands.domain.CustomCommandEntity

class CustomCommandListAdapter(
    val context: Context,
    val customCommandList: List<CustomCommandEntity>,
    private val onCommandClick: (CustomCommandEntity) -> Unit
) : RecyclerView.Adapter<CustomCommandListAdapter.CustomCommandViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomCommandViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_custom_command, parent, false)
        return CustomCommandViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomCommandViewHolder, position: Int) {
        val customCommand = customCommandList[position]
        holder.bind(customCommand, onCommandClick)
    }

    override fun getItemCount(): Int {
        return customCommandList.size
    }


    class CustomCommandViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var image1: ImageView
        private lateinit var image2: ImageView
        private lateinit var commandButton: TextView

        fun bind(customCommandEntity: CustomCommandEntity, onCommandClick: (CustomCommandEntity) -> Unit)
        {
            this.initView()
            when (customCommandEntity.targetDevice1) {
                DeviceEntity.TypeDevice.GARAGE_DOOR -> {
                    this.image1.setImageResource(R.drawable.ic_icons8_door_sensor_checked_50)
                }
                DeviceEntity.TypeDevice.LIGHT -> {
                    this.image1.setImageResource(R.drawable.ic_icons8_light_50)
                }
                DeviceEntity.TypeDevice.ROLLING_SHUTTER -> {
                    this.image1.setImageResource(R.drawable.ic_icons8_window_shade_50)
                }
            }

            if (customCommandEntity.targetDevice2 != null) {
                this.image2.visibility = View.VISIBLE
                when (customCommandEntity.targetDevice2) {
                    DeviceEntity.TypeDevice.GARAGE_DOOR -> {
                        this.image2.setImageResource(R.drawable.ic_icons8_door_sensor_checked_50)
                    }
                    DeviceEntity.TypeDevice.LIGHT -> {
                        this.image2.setImageResource(R.drawable.ic_icons8_light_50)
                    }
                    DeviceEntity.TypeDevice.ROLLING_SHUTTER -> {
                        this.image2.setImageResource(R.drawable.ic_icons8_window_shade_50)
                    }
                }
            } else {
                this.image2.visibility = View.GONE
            }
            this.commandButton.text = customCommandEntity.command.label
            this.commandButton.setOnClickListener {
                onCommandClick(customCommandEntity)
            }
        }
        private fun initView() {
            this.image1 = itemView.findViewById(R.id.command_image_1)
            this.image2 = itemView.findViewById(R.id.command_image_2)
            this.commandButton = itemView.findViewById(R.id.command_btn)
        }
    }
}