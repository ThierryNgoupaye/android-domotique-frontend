package com.project.domotique.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.domotique.R

class HouseDeviceTypeAdapter(
    val houseDeviceTypes: List<String>
) : RecyclerView.Adapter<HouseDeviceTypeAdapter.HouseDeviceTypeViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseDeviceTypeViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_house_device_type, parent, false)
        return HouseDeviceTypeViewHolder(view)
    }


    override fun onBindViewHolder(holder: HouseDeviceTypeViewHolder, position: Int) {
        val houseDeviceType = this.houseDeviceTypes[position]
        holder.bind(houseDeviceType)
    }



    override fun getItemCount(): Int {
        return this.houseDeviceTypes.size
    }




    class HouseDeviceTypeViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {


        private  val typeName: TextView = itemView.findViewById(R.id.device_type_name)

        private val deviceNumber : TextView = itemView.findViewById(R.id.device_type_number)

        private val deviceIcon : ImageView = itemView.findViewById(R.id.device_type_icon)

        fun bind(houseDeviceType: String) {
            when(houseDeviceType) {
                "Volet" -> {
                    typeName.text = "Volet"
                    deviceNumber.text = "12"
                    deviceIcon.setImageResource(R.drawable.ic_icons8_window_shade_50)
                }
                "Lumière" -> {
                    typeName.text = "Lumière"
                    deviceNumber.text = "12"
                    deviceIcon.setImageResource(R.drawable.ic_icons8_light_50)
                }
                "Porte de garage" -> {
                    typeName.text = "Porte de garage"
                    deviceNumber.text = "1"
                    deviceIcon.setImageResource(R.drawable.ic_icons8_door_sensor_checked_50)

                }
            }
        }
    }
}