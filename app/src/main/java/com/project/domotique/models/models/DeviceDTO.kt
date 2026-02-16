package com.project.domotique.models.models

import com.project.domotique.models.entities.CommandEntity
import com.project.domotique.models.entities.DeviceEntity
import com.project.domotique.models.entities.DeviceEntity.TypeDevice
import com.project.domotique.models.entities.DeviceEntity.DeviceState

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
            val deviceType = TypeDevice.fromValue(dto.type)
            val openingState : DeviceState? = if (deviceType == TypeDevice.ROLLING_SHUTTER || deviceType == TypeDevice.GARAGE_DOOR) DeviceState.fromValueForShutter(dto.opening) else null
            val powerState : DeviceState? = if (deviceType == TypeDevice.LIGHT) DeviceState.fromValueForLight(dto.power) else null
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