package com.project.domotique.models.entities

data class RoomDevices(
    val room: Room,
    val shutters: List<DeviceEntity>,
    val lights: List<DeviceEntity>,
    val garageDoor: DeviceEntity?
) {

    val totalDevices: Int
        get() = this.shutters.size + this.lights.size + (if (this.garageDoor != null) 1 else 0)
    

    val allDevices: List<DeviceEntity>
        get() {
            val devices = mutableListOf<DeviceEntity>()
            garageDoor?.let { devices.add(it) }
            devices.addAll(this.shutters)
            devices.addAll(this.lights)
            return devices
        }

}