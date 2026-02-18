package com.project.domotique.features.home.customCommands.domain

import com.project.domotique.features.home.devices.domain.entities.DeviceEntity

data class CustomCommandEntity (
    val targetDevice1 : DeviceEntity.TypeDevice,
    val targetDevice2 : DeviceEntity.TypeDevice?,
    val command: CustomCommand,
){
    init {
        if (targetDevice1 == targetDevice2)
        {
            throw IllegalArgumentException("Les deux devices ne doivent pas être identiques")
        }
    }
    enum class CustomCommand(val label: String)
    {
        /* Light Command */
        TURN_ON_ALL_LIGHT_IN_FIRST_FLOOR("Allumer toutes lumières du rez de chaussée"),
        TURN_OFF_ALL_LIGHT_IN_FIRST_FLOOR("Éteindre toutes les lumières du rez de chaussée"),
        TURN_ON_ALL_LIGHT_IN_SECOND_FLOOR("Allumer toutes lumières du 1er étage"),
        TURN_OFF_ALL_LIGHT_IN_SECOND_FLOOR("Éteindre toutes les lumières du 1er étage"),
        TURN_ON_ALL_LIGHT_IN_ALL_ROOM("Allumer toutes lumières de la maison"),
        TURN_OFF_ALL_LIGHT_IN_ALL_ROOM("Éteindre toutes les lumières de la maison"),

        /* Door Command */
        TURN_ON_ALL_DOOR("Ouvrir toutes les portes du 1er étage"),

        /* Rolling Shutter Commands */
        OPEN_ALL_ROLLING_SHUTTER("Ouvrir tous les volets de la maison"),
        CLOSE_ALL_ROLLING_SHUTTER("Fermer tous les volets de la maison"),
        OPEN_ROLLING_SHUTTER_IN_FIRST_FLOOR("Ouvrir les volets du rez de chaussée"),
        CLOSE_ROLLING_SHUTTER_IN_FIRST_FLOOR("Fermer les volets du rez de chaussée"),
        OPEN_ROLLING_SHUTTER_IN_SECOND_FLOOR("Ouvrir les volets du 1er étage"),
        CLOSE_ROLLING_SHUTTER_IN_SECOND_FLOOR("Fermer les volets du 1er étage"),

        /* Rolling shutter + Light Commands*/
        OPEN_ALL_ROLLING_SHUTTER_AND_TURN_ON_ALL_LIGHT_IN_FIRST_FLOOR("Ouvrir les volets et allumer les lumières du rez de chaussée"),
        CLOSE_ALL_ROLLING_SHUTTER_AND_TURN_OFF_ALL_LIGHT_IN_FIRST_FLOOR("Fermer tous les volets et éteindre toutes les lumières du rez de chaussée"),
        OPEN_ALL_ROLLING_SHUTTER_AND_TURN_ON_ALL_LIGHT_IN_SECOND_FLOOR("Ouvrir tous les volets et allumer toutes les lumières du 1er étage"),
        CLOSE_ALL_ROLLING_SHUTTER_AND_TURN_OFF_ALL_LIGHT_IN_SECOND_FLOOR("Fermer tous les volets et éteindre toutes les lumières du 1er étage"),
        OPEN_ALL_ROLLING_SHUTTER_AND_TURN_ON_ALL_LIGHT_IN_ALL_ROOM("Ouvrir tous les volets et allumer toutes les lumières de la maison"),
        CLOSE_ALL_ROLLING_SHUTTER_AND_TURN_OFF_ALL_LIGHT_IN_ALL_ROOM("Fermer tous les volets et éteindre toutes les lumières de la maison"),
    }
}