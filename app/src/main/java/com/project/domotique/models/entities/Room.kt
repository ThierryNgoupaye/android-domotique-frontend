package com.project.domotique.models.entities

data class Room(
    val name: String,
    val floor: Int,
    val shutterCount: Int,
    val lightCount: Int,
    val hasGarageDoor: Boolean = false,
    val icon: Int
)