package com.project.domotique.features.home.devices.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.project.domotique.R
import com.project.domotique.features.home.devices.domain.entities.DeviceEntity
import com.project.domotique.features.home.devices.domain.entities.RoomDevices
import com.project.domotique.features.home.devices.presentation.ui.PlaceOrderDialog

class RoomDeviceAdapter(
    val context: Context,
    private val rooms: List<RoomDevices>,
    private val onCommandSelected: (DeviceEntity, String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_SINGLE = 1
        const val TYPE_DOUBLE = 2
        const val TYPE_TRIPLE = 3
        const val TYPE_LIST = 4

        fun getDeviceName(device: DeviceEntity): String {
            return when (device.type) {
                DeviceEntity.TypeDevice.ROLLING_SHUTTER -> "${device.type.label} ${device.id.split(" ").getOrNull(1) ?: ""}"
                DeviceEntity.TypeDevice.LIGHT -> "${device.type.label} ${device.id.split(" ").getOrNull(1) ?: ""}"
                DeviceEntity.TypeDevice.GARAGE_DOOR -> device.type.label
            }
        }

        fun getDeviceState(device: DeviceEntity): String {
            return device.getStateLabel()
        }

        fun getDeviceIcon(device: DeviceEntity): Int {
            return when (device.type) {
                DeviceEntity.TypeDevice.ROLLING_SHUTTER -> R.drawable.ic_icons8_window_shade_50
                DeviceEntity.TypeDevice.LIGHT -> R.drawable.ic_icons8_light_50
                else -> R.drawable.ic_icons8_door_sensor_checked_50
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
        val inflater = LayoutInflater.from(context)
        return when (viewType) {
            TYPE_SINGLE -> {
                val view = inflater.inflate(R.layout.layout_single_device_room, parent, false)
                SingleRoomViewHolder(view)
            }
            TYPE_DOUBLE -> {
                val view = inflater.inflate(R.layout.layout_double_device_room, parent, false)
                DoubleRoomViewHolder(view)
            }
            TYPE_TRIPLE -> {
                val view = inflater.inflate(R.layout.layout_triple_device_room, parent, false)
                TripleRoomViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.layout_multi_device_room, parent, false)
                ListRoomViewHolder(view)
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val roomData = rooms[position]
        when (holder) {
            is SingleRoomViewHolder -> holder.bind(roomData, onCommandSelected)
            is DoubleRoomViewHolder -> holder.bind(roomData, onCommandSelected)
            is TripleRoomViewHolder -> holder.bind(roomData, onCommandSelected)
            is ListRoomViewHolder -> holder.bind(roomData, onCommandSelected)
        }
    }

    override fun getItemCount() = rooms.size



    internal class SingleRoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val roomTitle: TextView = itemView.findViewById(R.id.room_title)
        private val deviceIcon: ImageView = itemView.findViewById(R.id.device_icon)
        private val deviceName: TextView = itemView.findViewById(R.id.device_name)
        private val deviceState: TextView = itemView.findViewById(R.id.device_state)
        private val cardView : CardView = itemView.findViewById(R.id.single_device_card)
        private var currentDialog: PlaceOrderDialog? = null

        fun bind(roomData: RoomDevices, onCommandSelected: (DeviceEntity, String) -> Unit) {
            roomTitle.text = roomData.room.name
            val device = roomData.allDevices.firstOrNull()
            device?.let {
                deviceIcon.setImageResource(getDeviceIcon(it))
                deviceName.text = getDeviceName(it)
                deviceState.text = getDeviceState(it)
            }

            cardView.setOnClickListener {
                this.currentDialog?.dismiss()
                this.currentDialog = PlaceOrderDialog(
                    itemView.context,
                    device!!,
                    onCommandSelected
                )
                this.currentDialog?.show()
            }
        }
    }


    internal class DoubleRoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val roomTitle: TextView = view.findViewById(R.id.room_title)
        private val leftName: TextView = view.findViewById(R.id.left_name)
        private val leftState: TextView = view.findViewById(R.id.left_state)
        private val leftIcon : ImageView = view.findViewById(R.id.left_icon)

        private val leftCardView : CardView = view.findViewById(R.id.left_device_card)

        private val rightIcon : ImageView = view.findViewById(R.id.right_icon)
        private val rightName: TextView = view.findViewById(R.id.right_name)
        private val rightState: TextView = view.findViewById(R.id.right_state)
        private val rightCardView : CardView = view.findViewById(R.id.right_device_card)
        private var currentDialog: PlaceOrderDialog? = null

        fun bind(roomData: RoomDevices, onCommandSelected: (DeviceEntity, String) -> Unit) {
            roomTitle.text = roomData.room.name
            val devices = roomData.allDevices
            val leftDevice = devices[0]
            val rightDevice = devices[1]
            if (devices.size == 2) {
                leftName.text = getDeviceName(leftDevice)
                leftState.text = getDeviceState(leftDevice)
                leftIcon.setImageResource(getDeviceIcon(leftDevice))
                leftCardView.setOnClickListener {
                    this.currentDialog?.dismiss()
                    this.currentDialog = PlaceOrderDialog(
                        itemView.context,
                        leftDevice,
                        onCommandSelected
                    )
                    this.currentDialog?.show()
                }


                rightName.text = getDeviceName(rightDevice)
                rightState.text = getDeviceState(rightDevice)
                rightIcon.setImageResource(getDeviceIcon(rightDevice))
                rightCardView.setOnClickListener {
                    this.currentDialog?.dismiss()
                    this.currentDialog = PlaceOrderDialog(
                        itemView.context,
                        rightDevice,
                        onCommandSelected
                    )
                    this.currentDialog?.show()
                }
            }
        }
    }



    internal class TripleRoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val roomTitle: TextView = view.findViewById(R.id.room_title)
        private val leftName: TextView = view.findViewById(R.id.left_name)
        private val leftState: TextView = view.findViewById(R.id.left_state)
        private val leftIcon : ImageView = view.findViewById(R.id.left_icon)

        private val leftCard: CardView = view.findViewById(R.id.left_large_card)

        private val topName: TextView = view.findViewById(R.id.top_name)
        private val topIcon: ImageView = view.findViewById(R.id.top_icon)
        private val topState: TextView = view.findViewById(R.id.top_state)
        private val topCard: CardView = view.findViewById(R.id.right_top_card)


        private val bottomName: TextView = view.findViewById(R.id.bottom_name)
        private val bottomIcon : ImageView = view.findViewById(R.id.bottom_icon)
        private val bottomState: TextView = view.findViewById(R.id.bottom_state)
        private val bottomCard: CardView = view.findViewById(R.id.right_bottom_card)
        private var currentDialog: PlaceOrderDialog? = null


        fun bind(roomData: RoomDevices, onCommandSelected: (DeviceEntity, String) -> Unit) {
            roomTitle.text = roomData.room.name
            val devices = roomData.allDevices
            if (devices.size == 3) {
                val leftDevice = devices[0]
                val topDevice = devices[1]
                val bottomDevice = devices[2]

                leftName.text = getDeviceName(leftDevice)
                leftState.text = getDeviceState(leftDevice)
                leftIcon.setImageResource(getDeviceIcon(leftDevice))
                leftCard.setOnClickListener {
                    this.currentDialog?.dismiss()
                    this.currentDialog = PlaceOrderDialog(
                        itemView.context,
                        leftDevice,
                        onCommandSelected
                    )
                    this.currentDialog?.show()
                }

                topName.text = getDeviceName(topDevice)
                topState.text = getDeviceState(topDevice)
                topIcon.setImageResource(getDeviceIcon(topDevice))
                topCard.setOnClickListener {
                    this.currentDialog?.dismiss()
                    this.currentDialog = PlaceOrderDialog(
                        itemView.context,
                        topDevice,
                        onCommandSelected
                    )
                    this.currentDialog?.show()
                }


                bottomName.text = getDeviceName(bottomDevice)
                bottomState.text = getDeviceState(bottomDevice)
                bottomIcon.setImageResource(getDeviceIcon(bottomDevice))
                bottomCard.setOnClickListener {
                    this.currentDialog?.dismiss()
                    this.currentDialog = PlaceOrderDialog(
                        itemView.context,
                        bottomDevice,
                        onCommandSelected
                    )
                    this.currentDialog?.show()
                }
            }
        }
    }


    internal class ListRoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val roomTitle: TextView = view.findViewById(R.id.room_title)
        private val devicesContainer: LinearLayout = view.findViewById(R.id.devices_list_container)
        private var currentDialog: PlaceOrderDialog? = null
        fun bind(roomData: RoomDevices, onCommandSelected: (DeviceEntity, String) -> Unit) {
            roomTitle.text = roomData.room.name
            devicesContainer.removeAllViews()
            roomData.allDevices.forEach { device ->
                val miniCard = LayoutInflater
                    .from(itemView.context)
                    .inflate(R.layout.layout_device_mini, devicesContainer, false)

                miniCard.findViewById<TextView>(R.id.mini_device_name).text = getDeviceName(device)
                miniCard.findViewById<TextView>(R.id.mini_device_state).text = getDeviceState(device)
                miniCard.findViewById<ImageView>(R.id.mini_device_icon).setImageResource(getDeviceIcon(device))
                devicesContainer.addView(miniCard)

                miniCard.setOnClickListener {
                    this.currentDialog?.dismiss()
                    this.currentDialog = PlaceOrderDialog(
                        itemView.context,
                        device,
                        onCommandSelected
                    )
                    this.currentDialog?.show()
                }
            }
        }
    }
}