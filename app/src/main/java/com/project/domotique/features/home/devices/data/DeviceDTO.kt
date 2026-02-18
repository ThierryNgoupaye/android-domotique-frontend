package com.project.domotique.features.home.devices.data

import com.project.domotique.features.home.devices.domain.entities.CommandEntity
import com.project.domotique.features.home.devices.domain.entities.DeviceEntity

data class DeviceDTO(
    val id: String,
    val type: String,
    val availableCommands: List<String>,
    val opening: Int?,
    val power: Int?,
    val openingMode: Int?
) {
    companion object {
        fun toEntity(dto: DeviceDTO): DeviceEntity {
            val commandList = dto.availableCommands.map { commandString ->
                CommandEntity(command = CommandEntity.Command.fromValue(commandString))
            }
            val deviceType = DeviceEntity.TypeDevice.Companion.fromValue(dto.type)
            val openingState : DeviceEntity.DeviceState? = if (deviceType == DeviceEntity.TypeDevice.ROLLING_SHUTTER || deviceType == DeviceEntity.TypeDevice.GARAGE_DOOR) DeviceEntity.DeviceState.Companion.fromValueForShutter(dto.opening) else null
            val powerState : DeviceEntity.DeviceState? = if (deviceType == DeviceEntity.TypeDevice.LIGHT) DeviceEntity.DeviceState.Companion.fromValueForLight(dto.power) else null
            return DeviceEntity(
                id = dto.id,
                type = deviceType,
                availableCommands = commandList,
                opening = openingState,
                power = powerState,
                openingMode = dto.openingMode
            )
        }
    }
}