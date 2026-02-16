package com.project.domotique.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.domotique.R
import com.project.domotique.models.entities.DeviceEntity.TypeDevice
import com.project.domotique.models.entities.RoomDevices

class HouseDeviceTypeAdapter(
    val houseDeviceTypes: List<TypeDevice>,
    val roomsDeviceList: List<RoomDevices>
) : RecyclerView.Adapter<HouseDeviceTypeAdapter.HouseDeviceTypeViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseDeviceTypeViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_house_device_type, parent, false)
        return HouseDeviceTypeViewHolder(view)
    }


    override fun onBindViewHolder(holder: HouseDeviceTypeViewHolder, position: Int) {
        val houseDeviceType = this.houseDeviceTypes[position]
        var shutterCount  = 0
        var lightCount  = 0
        var garageDoorCount  = 0
        for (roomDevices in this.roomsDeviceList) {
            when(houseDeviceType) {
                TypeDevice.ROLLING_SHUTTER -> shutterCount += roomDevices.getShuttersNumber()
                TypeDevice.LIGHT -> lightCount += roomDevices.getLightsNumber()
                TypeDevice.GARAGE_DOOR -> garageDoorCount += 1
            }
        }
        holder.bind(houseDeviceType, shutterCount, lightCount, garageDoorCount)
    }



    override fun getItemCount(): Int {
        return this.houseDeviceTypes.size
    }




    class HouseDeviceTypeViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {


        private  val typeName: TextView = itemView.findViewById(R.id.device_type_name)

        private val deviceNumber : TextView = itemView.findViewById(R.id.device_type_number)

        private val deviceIcon : ImageView = itemView.findViewById(R.id.device_type_icon)

        fun bind(houseDeviceType: TypeDevice, shutterCount: Int, lightCount: Int, garageDoorCount: Int) {
            when(houseDeviceType) {
                TypeDevice.ROLLING_SHUTTER -> {
                    typeName.text = TypeDevice.ROLLING_SHUTTER.label
                    deviceNumber.text = shutterCount.toString()
                    deviceIcon.setImageResource(R.drawable.ic_icons8_window_shade_50)
                }
                TypeDevice.LIGHT -> {
                    typeName.text = TypeDevice.LIGHT.label
                    deviceNumber.text = lightCount.toString()
                    deviceIcon.setImageResource(R.drawable.ic_icons8_light_50)
                }
                TypeDevice.GARAGE_DOOR -> {
                    typeName.text = TypeDevice.GARAGE_DOOR.label
                    deviceNumber.text = garageDoorCount.toString()
                    deviceIcon.setImageResource(R.drawable.ic_icons8_door_sensor_checked_50)
                }
            }
        }
    }
}