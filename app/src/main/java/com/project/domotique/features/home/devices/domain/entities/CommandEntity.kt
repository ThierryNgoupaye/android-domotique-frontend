package com.project.domotique.features.home.devices.domain.entities

import com.project.domotique.features.home.devices.data.CommandRequest

data class CommandEntity (
    val command: Command,
){

    companion object {
        fun toRequest(entity: CommandEntity): CommandRequest {
            return CommandRequest(command = entity.command.value)
        }
    }

    enum class Command(val label: String, val value: String) {
        OPEN("Ouvrir", "OPEN"),
        CLOSE("Fermer", "CLOSE"),
        STOP("Arrêter", "STOP"),
        TURN_ON("Allumer", "TURN ON"),
        TURN_OFF("Éteindre", "TURN OFF");


        override fun toString(): String = label

       companion object {
           fun fromValue(value: String): Command {
               return entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException("Invalid command: $value")
           }
       }
    }

}