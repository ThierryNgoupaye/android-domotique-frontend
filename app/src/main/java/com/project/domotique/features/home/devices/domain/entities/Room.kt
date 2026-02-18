package com.project.domotique.features.home.devices.domain.entities

data class Room(
    val name: String,
    val floor: Int,
    val shutterCount: Int,
    val lightCount: Int,
    val hasGarageDoor: Boolean = false,
)