package com.project.domotique.models.entities

import com.project.domotique.models.models.CommandRequest

data class DeviceEntity (
    val id: String,
    val type: String,
    val availableCommands: List<CommandEntity>,
    val opening : Int?,
    val power: Int?,
    val openingMode: Int?
)


