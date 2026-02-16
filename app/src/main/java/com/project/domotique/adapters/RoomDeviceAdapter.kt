package com.project.domotique.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.domotique.R


import com.project.domotique.models.entities.DeviceEntity
import com.project.domotique.models.entities.RoomDevices

class RoomDeviceAdapter(
    private val rooms: List<RoomDevices>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_SINGLE = 0
        const val TYPE_DOUBLE = 1
        const val TYPE_TRIPLE = 2
        const val TYPE_LIST = 3

        fun getDeviceName(device: DeviceEntity): String {
            return when (device.type) {
                "rolling shutter" -> "Volet ${device.id.split(" ").getOrNull(1) ?: ""}"
                "light" -> "Lumière ${device.id.split(" ").getOrNull(1) ?: ""}"
                "garage door" -> "Porte de garage"
                else -> device.id
            }
        }

        fun getDeviceState(device: DeviceEntity): String {
            return when (device.type) {
                "rolling shutter", "garage door" -> if (device.opening == 1) "Ouvert" else "Fermé"
                "light" -> if (device.power == 1) "Allumée" else "Éteinte"
                else -> "État inconnu"
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return when (rooms[position].totalDevices) {
            1 -> TYPE_SINGLE
            2 -> TYPE_DOUBLE
            3 -> TYPE_TRIPLE
            else -> TYPE_LIST
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        
        return when (viewType) {
            TYPE_SINGLE -> {
                val view = inflater.inflate(R.layout.item_room_single, parent, false)
                SingleRoomViewHolder(view)
            }
            TYPE_DOUBLE -> {
                val view = inflater.inflate(R.layout.item_room_double, parent, false)
                DoubleRoomViewHolder(view)
            }
            TYPE_TRIPLE -> {
                val view = inflater.inflate(R.layout.item_room_triple, parent, false)
                TripleRoomViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.item_room_list, parent, false)
                ListRoomViewHolder(view)
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val roomData = rooms[position]
        when (holder) {
            is SingleRoomViewHolder -> holder.bind(roomData)
            is DoubleRoomViewHolder -> holder.bind(roomData)
            is TripleRoomViewHolder -> holder.bind(roomData)
            is ListRoomViewHolder -> holder.bind(roomData)
        }
    }

    override fun getItemCount() = rooms.size



    internal class SingleRoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val roomTitle: TextView = view.findViewById(R.id.room_title)

        private val deviceIcon: ImageView = view.findViewById(R.id.device_icon)
        private val deviceName: TextView = view.findViewById(R.id.device_name)
        private val deviceState: TextView = view.findViewById(R.id.device_state)
        
        fun bind(roomData: RoomDevices) {
            roomTitle.text = roomData.room.name
            
            val device = roomData.allDevices.firstOrNull()
            device?.let {
                deviceName.text = getDeviceName(it)
                deviceState.text = getDeviceState(it)
            }
        }
    }


    internal class DoubleRoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val roomTitle: TextView = view.findViewById(R.id.room_title)
        private val leftName: TextView = view.findViewById(R.id.left_name)
        private val leftState: TextView = view.findViewById(R.id.left_state)
        private val rightName: TextView = view.findViewById(R.id.right_name)
        private val rightState: TextView = view.findViewById(R.id.right_state)
        
        fun bind(roomData: RoomDevices) {
            roomTitle.text = roomData.room.name
            
            val devices = roomData.allDevices
            if (devices.size >= 2) {
                leftName.text = getDeviceName(devices[0])
                leftState.text = getDeviceState(devices[0])
                
                rightName.text = getDeviceName(devices[1])
                rightState.text = getDeviceState(devices[1])
            }
        }
    }



    internal class TripleRoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val roomTitle: TextView = view.findViewById(R.id.room_title)
        private val leftName: TextView = view.findViewById(R.id.left_name)
        private val leftState: TextView = view.findViewById(R.id.left_state)
        private val topName: TextView = view.findViewById(R.id.top_name)
        private val bottomName: TextView = view.findViewById(R.id.bottom_name)
        
        fun bind(roomData: RoomDevices) {
            roomTitle.text = roomData.room.name
            
            val devices = roomData.allDevices
            if (devices.size >= 3) {
                leftName.text = getDeviceName(devices[0])
                leftState.text = getDeviceState(devices[0])
                
                topName.text = getDeviceName(devices[1])
                bottomName.text = getDeviceName(devices[2])
            }
        }
    }


    internal class ListRoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val roomTitle: TextView = view.findViewById(R.id.room_title)
        private val devicesContainer: LinearLayout = view.findViewById(R.id.devices_list_container)
        
        fun bind(roomData: RoomDevices) {
            roomTitle.text = roomData.room.name
            
            // Vide le container
            devicesContainer.removeAllViews()
            
            // Ajoute chaque device
            roomData.allDevices.forEach { device ->
                val miniCard = LayoutInflater.from(itemView.context)
                    .inflate(R.layout.item_device_mini, devicesContainer, false)
                
                miniCard.findViewById<TextView>(R.id.mini_device_name).text = getDeviceName(device)
                miniCard.findViewById<TextView>(R.id.mini_device_state).text = getDeviceState(device)
                
                devicesContainer.addView(miniCard)
            }
        }
    }
}