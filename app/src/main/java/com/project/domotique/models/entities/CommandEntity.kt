package com.project.domotique.models.entities


import com.project.domotique.models.models.CommandRequest


data class CommandEntity (
    val command: Command,
){

    companion object {
        fun toRequest(entity: CommandEntity): CommandRequest {
            return CommandRequest(command = entity.command.value)
        }
    }

    enum class Command(val label: String, val value: String) {
        OPEN("Open", "OPEN"),
        CLOSE("Close", "CLOSE"),
        STOP("Stop", "STOP"),
        TURN_ON("Turn On", "TURN ON"),
        TURN_OFF("Turn Off", "TURN OFF");


        override fun toString(): String = label

       companion object {
           fun fromValue(value: String): Command {
               return Command.entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException("Invalid value: $value")
           }
       }
    }

}





