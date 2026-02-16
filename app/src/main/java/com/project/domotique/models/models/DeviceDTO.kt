package com.project.domotique.models.models

import com.project.domotique.models.entities.CommandEntity
import com.project.domotique.models.entities.DeviceEntity

data class DeviceDTO (
    val id: String,
    val type: String,
    val availableCommands: List<String>,
    val opening : Int?,
    val power: Int?,
    val openingMode: Int?
){
    companion object {
        fun toEntity(dto: DeviceDTO): DeviceEntity {
            val commandList : ArrayList<CommandEntity> = ArrayList()
            for (commandString in dto.availableCommands) {
                commandList.add(
                    CommandEntity(command = CommandEntity.Command.fromValue(commandString))
                )
            }
            return DeviceEntity(
                id = dto.id,
                type = dto.type,
                availableCommands = commandList,
                opening = dto.opening,
                power = dto.power,
                openingMode = dto.openingMode,
            )
        }
    }
}