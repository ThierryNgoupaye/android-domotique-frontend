package com.project.domotique.utils

import com.project.domotique.R
import com.project.domotique.models.entities.DeviceEntity
import com.project.domotique.models.entities.Room
import com.project.domotique.models.entities.RoomDevices

object RoomDistributor {
    

    private val roomDefinitions = listOf(
        // Floor 1
        Room("Garage", 1, 1, 1, hasGarageDoor = true, R.drawable.ic_device_23),
        Room("Salon", 1, 2, 1, icon = R.drawable.ic_device_23),
        Room("Cuisine", 1, 1, 1, icon = R.drawable.ic_device_23),
        Room("Salle à manger", 1, 1, 1, icon = R.drawable.ic_device_23),
        Room("Bureau ", 1, 1, 1, icon = R.drawable.ic_device_23),
        Room("Chambre 1", 1, 2, 0, icon = R.drawable.ic_device_23),
        Room("Salle de bain", 1, 1, 0, icon = R.drawable.ic_device_23),
        Room("Chambre 2", 1, 2, 1, icon = R.drawable.ic_device_23),
        
        // Floor 2
        Room("Buanderie", 2, 2, 0, icon = R.drawable.ic_device_23),
        Room("Chambre 3", 2, 2, 1, icon = R.drawable.ic_device_23),
        Room("Chambre 4", 2, 2, 1, icon = R.drawable.ic_device_23),
        Room("Chambre 5", 2, 2, 1, icon = R.drawable.ic_device_23),
        Room("Chambre 6", 2, 2, 1, icon = R.drawable.ic_device_23),
        Room("Salle de bain 2", 2, 1, 1, icon = R.drawable.ic_device_23),
    )


    fun distributeDevices(devices: List<DeviceEntity>): List<RoomDevices> {
        val result = mutableListOf<RoomDevices>()
        val shuttersByFloor : Map<Int, List<DeviceEntity>> = devices
            .filter { it.type == "rolling shutter" }
            .groupBy { extractFloor(it.id) }
            .mapValues { it.value.sortedBy { device -> device.id } }
        
        val lightsByFloor : Map<Int, List<DeviceEntity>> = devices
            .filter { it.type == "light" }
            .groupBy { extractFloor(it.id) }
            .mapValues { it.value.sortedBy { device -> device.id } }
        
        val garageDoor : DeviceEntity? = devices.find { it.type == "garage door" }
        var shutterIndex1 = 0
        var shutterIndex2 = 0
        var lightIndex1 = 0
        var lightIndex2 = 0
        
        roomDefinitions.forEach { room ->
            val roomShutters = mutableListOf<DeviceEntity>()
            val roomLights = mutableListOf<DeviceEntity>()
            var roomGarageDoor: DeviceEntity? = null

            val shutters : List<DeviceEntity>? = if (room.floor == 1) shuttersByFloor[1] else shuttersByFloor[2]

            repeat(room.shutterCount) {
                val index = if (room.floor == 1) shutterIndex1++ else shutterIndex2++
                shutters?.getOrNull(index)?.let { roomShutters.add(it) }
            }
            

            val lights = if (room.floor == 1) lightsByFloor[1] else lightsByFloor[2]
            repeat(room.lightCount) {
                val index = if (room.floor == 1) lightIndex1++ else lightIndex2++
                lights?.getOrNull(index)?.let { roomLights.add(it) }
            }

            if (room.hasGarageDoor && garageDoor != null) {
                roomGarageDoor = garageDoor
            }
            if (roomShutters.isNotEmpty() || roomLights.isNotEmpty() || roomGarageDoor != null) {
                result.add(
                    RoomDevices(
                        room = room,
                        shutters = roomShutters,
                        lights = roomLights,
                        garageDoor = roomGarageDoor
                    )
                )
            }
        }
        return result
    }


    private fun extractFloor(deviceId: String): Int {
        return try {
            deviceId.split(" ")[1].split(".")[0].toInt()
        } catch (error: Exception) {
            println(error)
            -1
        }
    }
}