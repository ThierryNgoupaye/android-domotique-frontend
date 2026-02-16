package com.project.domotique.models.models

import com.project.domotique.models.entities.CommandEntity

data class CommandRequest (
    val command: String
){

    companion object {
        fun toEntity(request: CommandRequest): CommandEntity {
            return CommandEntity(
                command = CommandEntity.Command.fromValue(request.command)
            )
        }
    }



}