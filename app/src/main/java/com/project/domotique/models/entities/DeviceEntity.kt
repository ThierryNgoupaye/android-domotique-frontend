package com.project.domotique.models.entities

data class DeviceEntity(
    val id: String,
    val type: TypeDevice,
    val availableCommands: List<CommandEntity>,
    val opening: DeviceState?,
    val power: DeviceState?,
    val openingMode: Int?
) {
    fun getStateLabel(): String {
        return when (type) {
            TypeDevice.ROLLING_SHUTTER, TypeDevice.GARAGE_DOOR -> opening?.label ?: "Inconnu"
            TypeDevice.LIGHT -> power?.label ?: "Inconnu"
        }
    }

    enum class TypeDevice(val label: String, val value: String) {
        GARAGE_DOOR("Porte de garage", "garage door"),
        ROLLING_SHUTTER("Volet", "rolling shutter"),
        LIGHT("Lumière", "light");

        companion object {
            fun fromValue(value: String): TypeDevice {
                return entries.find { it.value == value } ?: throw IllegalArgumentException("Type de device invalide: $value")
            }
        }
    }

    enum class DeviceState(val label: String, val value: Int) {
        OPEN("Ouvert", 1),
        CLOSED("Fermé", 0),
        TURNED_ON("Allumée", 1),
        TURNED_OFF("Éteinte", 0);

        companion object {
            fun fromValueForShutter(value: Int?): DeviceState? {
                return when (value) {
                    1 -> OPEN
                    0 -> CLOSED
                    else -> null
                }
            }

            fun fromValueForLight(value: Int?): DeviceState? {
                return when (value) {
                    1 -> TURNED_ON
                    0 -> TURNED_OFF
                    else -> null
                }
            }
        }
    }
}