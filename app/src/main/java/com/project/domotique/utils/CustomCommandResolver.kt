package com.project.domotique.utils

import com.project.domotique.features.home.devices.domain.entities.CommandEntity.Command
import com.project.domotique.features.home.customCommands.domain.CustomCommandEntity
import com.project.domotique.features.home.customCommands.domain.CustomCommandEntity.CustomCommand
import com.project.domotique.features.home.devices.domain.entities.DeviceEntity
import com.project.domotique.features.home.devices.domain.entities.DeviceEntity.DeviceState
import com.project.domotique.features.home.devices.domain.entities.DeviceEntity.TypeDevice
import com.project.domotique.features.home.devices.data.CommandRequest

object CustomCommandResolver {
    fun resolve(command: CustomCommand, devices: List<DeviceEntity>): List<Pair<DeviceEntity, CommandRequest>> {
        return when (command) {
            CustomCommand.TURN_ON_ALL_LIGHT_IN_FIRST_FLOOR ->
                resolveAtom(devices, TypeDevice.LIGHT, 1, Command.TURN_ON)

            CustomCommand.TURN_OFF_ALL_LIGHT_IN_FIRST_FLOOR ->
                resolveAtom(devices, TypeDevice.LIGHT, 1, Command.TURN_OFF)


            CustomCommand.TURN_ON_ALL_LIGHT_IN_SECOND_FLOOR ->
                resolveAtom(devices, TypeDevice.LIGHT, 2, Command.TURN_ON)

            CustomCommand.TURN_OFF_ALL_LIGHT_IN_SECOND_FLOOR ->
                resolveAtom(devices, TypeDevice.LIGHT, 2, Command.TURN_OFF)


            CustomCommand.TURN_ON_ALL_LIGHT_IN_ALL_ROOM ->
                resolveAtom(devices, TypeDevice.LIGHT, 1, Command.TURN_ON) + resolveAtom(devices, TypeDevice.LIGHT, 2, Command.TURN_ON)

            CustomCommand.TURN_OFF_ALL_LIGHT_IN_ALL_ROOM ->
                resolveAtom(devices, TypeDevice.LIGHT, 1, Command.TURN_OFF) + resolveAtom(devices, TypeDevice.LIGHT, 2, Command.TURN_OFF)


            CustomCommand.OPEN_ROLLING_SHUTTER_IN_FIRST_FLOOR -> resolveAtom(devices, TypeDevice.ROLLING_SHUTTER, 1, Command.OPEN)

            CustomCommand.CLOSE_ROLLING_SHUTTER_IN_FIRST_FLOOR -> resolveAtom(devices, TypeDevice.ROLLING_SHUTTER, 1, Command.CLOSE)


            CustomCommand.OPEN_ROLLING_SHUTTER_IN_SECOND_FLOOR ->
                resolveAtom(devices, TypeDevice.ROLLING_SHUTTER, 2, Command.OPEN)

            CustomCommand.CLOSE_ROLLING_SHUTTER_IN_SECOND_FLOOR ->
                resolveAtom(devices, TypeDevice.ROLLING_SHUTTER, 2, Command.CLOSE)


            CustomCommand.OPEN_ALL_ROLLING_SHUTTER ->
                resolveAtom(devices, TypeDevice.ROLLING_SHUTTER, 1, Command.OPEN) +
                        resolveAtom(devices, TypeDevice.ROLLING_SHUTTER, 2, Command.OPEN)

            CustomCommand.CLOSE_ALL_ROLLING_SHUTTER ->
                resolveAtom(devices, TypeDevice.ROLLING_SHUTTER, 1, Command.CLOSE) +
                        resolveAtom(devices, TypeDevice.ROLLING_SHUTTER, 2, Command.CLOSE)


            CustomCommand.OPEN_ALL_ROLLING_SHUTTER_AND_TURN_ON_ALL_LIGHT_IN_FIRST_FLOOR ->
                resolveAtom(devices, TypeDevice.ROLLING_SHUTTER, 1, Command.OPEN) +
                        resolveAtom(devices, TypeDevice.LIGHT, 1, Command.TURN_ON)

            CustomCommand.CLOSE_ALL_ROLLING_SHUTTER_AND_TURN_OFF_ALL_LIGHT_IN_FIRST_FLOOR ->
                resolveAtom(devices, TypeDevice.ROLLING_SHUTTER, 1, Command.CLOSE) +
                        resolveAtom(devices, TypeDevice.LIGHT, 1, Command.TURN_OFF)


            CustomCommand.OPEN_ALL_ROLLING_SHUTTER_AND_TURN_ON_ALL_LIGHT_IN_SECOND_FLOOR ->
                resolveAtom(devices, TypeDevice.ROLLING_SHUTTER, 2, Command.OPEN) +
                        resolveAtom(devices, TypeDevice.LIGHT, 2, Command.TURN_ON)

            CustomCommand.CLOSE_ALL_ROLLING_SHUTTER_AND_TURN_OFF_ALL_LIGHT_IN_SECOND_FLOOR ->
                resolveAtom(devices, TypeDevice.ROLLING_SHUTTER, 2, Command.CLOSE) +
                        resolveAtom(devices, TypeDevice.LIGHT, 2, Command.TURN_OFF)


            CustomCommand.OPEN_ALL_ROLLING_SHUTTER_AND_TURN_ON_ALL_LIGHT_IN_ALL_ROOM ->
                resolveAtom(devices, TypeDevice.ROLLING_SHUTTER, 1, Command.OPEN) +
                        resolveAtom(devices, TypeDevice.ROLLING_SHUTTER, 2, Command.OPEN) +
                        resolveAtom(devices, TypeDevice.LIGHT, 1, Command.TURN_ON) +
                        resolveAtom(devices, TypeDevice.LIGHT, 2, Command.TURN_ON)

            CustomCommand.CLOSE_ALL_ROLLING_SHUTTER_AND_TURN_OFF_ALL_LIGHT_IN_ALL_ROOM ->
                resolveAtom(devices, TypeDevice.ROLLING_SHUTTER, 1, Command.CLOSE) +
                        resolveAtom(devices, TypeDevice.ROLLING_SHUTTER, 2, Command.CLOSE) +
                        resolveAtom(devices, TypeDevice.LIGHT, 1, Command.TURN_OFF) +
                        resolveAtom(devices, TypeDevice.LIGHT, 2, Command.TURN_OFF)

            // Portes
            CustomCommand.TURN_ON_ALL_DOOR -> resolveAtom(devices, TypeDevice.GARAGE_DOOR, null, Command.OPEN)
        }
    }


    private fun resolveAtom(devices: List<DeviceEntity>, type: TypeDevice, floor: Int?, command: Command): List<Pair<DeviceEntity, CommandRequest>> {
        return devices
            .asSequence()
            .filter { it.type == type }
            .filter { floor == null || extractFloor(it.id) == floor }
            .filter { isCommandAvailable(it, command) }
            .filter { isNotAlreadyInTargetState(it, command) }
            .map { device ->
                Pair(device, CommandRequest(command.value))
            }
            .toList()
    }


    private fun extractFloor(deviceId: String): Int {
        return try {
            deviceId.split(" ")[1].split(".")[0].toInt()
        } catch (error: Exception) {
            println(error)
            -1
        }
    }


    private fun isCommandAvailable(device: DeviceEntity, command: Command): Boolean {
        return device.availableCommands.any { it.command == command }
    }


    private fun isNotAlreadyInTargetState(device: DeviceEntity, command: Command): Boolean {
        return when (command) {
            Command.TURN_ON  -> device.power != DeviceState.TURNED_ON
            Command.TURN_OFF -> device.power != DeviceState.TURNED_OFF
            Command.OPEN     -> device.opening != DeviceState.OPEN
            Command.CLOSE    -> device.opening != DeviceState.CLOSED
            Command.STOP     -> true
        }
    }


     fun buildCustomCommandList(): List<CustomCommandEntity> {
        return listOf(
            // Lumières rez-de-chaussée
            CustomCommandEntity(TypeDevice.LIGHT, null, CustomCommand.TURN_ON_ALL_LIGHT_IN_FIRST_FLOOR),
            CustomCommandEntity(TypeDevice.LIGHT, null, CustomCommand.TURN_OFF_ALL_LIGHT_IN_FIRST_FLOOR),

            // Lumières 1er étage
            CustomCommandEntity(TypeDevice.LIGHT, null, CustomCommand.TURN_ON_ALL_LIGHT_IN_SECOND_FLOOR),
            CustomCommandEntity(TypeDevice.LIGHT, null, CustomCommand.TURN_OFF_ALL_LIGHT_IN_SECOND_FLOOR),

            // Lumières toute la maison
            CustomCommandEntity(TypeDevice.LIGHT, null, CustomCommand.TURN_ON_ALL_LIGHT_IN_ALL_ROOM),
            CustomCommandEntity(TypeDevice.LIGHT, null, CustomCommand.TURN_OFF_ALL_LIGHT_IN_ALL_ROOM),

            // Volets rez-de-chaussée
            CustomCommandEntity(TypeDevice.ROLLING_SHUTTER, null, CustomCommand.OPEN_ROLLING_SHUTTER_IN_FIRST_FLOOR),
            CustomCommandEntity(TypeDevice.ROLLING_SHUTTER, null, CustomCommand.CLOSE_ROLLING_SHUTTER_IN_FIRST_FLOOR),

            // Volets 1er étage
            CustomCommandEntity(TypeDevice.ROLLING_SHUTTER, null, CustomCommand.OPEN_ROLLING_SHUTTER_IN_SECOND_FLOOR),
            CustomCommandEntity(TypeDevice.ROLLING_SHUTTER, null, CustomCommand.CLOSE_ROLLING_SHUTTER_IN_SECOND_FLOOR),

            // Volets toute la maison
            CustomCommandEntity(TypeDevice.ROLLING_SHUTTER, null, CustomCommand.OPEN_ALL_ROLLING_SHUTTER),
            CustomCommandEntity(TypeDevice.ROLLING_SHUTTER, null, CustomCommand.CLOSE_ALL_ROLLING_SHUTTER),

            // Volets + Lumières rez-de-chaussée
            CustomCommandEntity(TypeDevice.ROLLING_SHUTTER, TypeDevice.LIGHT, CustomCommand.OPEN_ALL_ROLLING_SHUTTER_AND_TURN_ON_ALL_LIGHT_IN_FIRST_FLOOR),
            CustomCommandEntity(TypeDevice.ROLLING_SHUTTER, TypeDevice.LIGHT, CustomCommand.CLOSE_ALL_ROLLING_SHUTTER_AND_TURN_OFF_ALL_LIGHT_IN_FIRST_FLOOR),

            // Volets + Lumières 1er étage
            CustomCommandEntity(TypeDevice.ROLLING_SHUTTER, TypeDevice.LIGHT, CustomCommand.OPEN_ALL_ROLLING_SHUTTER_AND_TURN_ON_ALL_LIGHT_IN_SECOND_FLOOR),
            CustomCommandEntity(TypeDevice.ROLLING_SHUTTER, TypeDevice.LIGHT, CustomCommand.CLOSE_ALL_ROLLING_SHUTTER_AND_TURN_OFF_ALL_LIGHT_IN_SECOND_FLOOR),

            // Volets + Lumières toute la maison
            CustomCommandEntity(TypeDevice.ROLLING_SHUTTER, TypeDevice.LIGHT, CustomCommand.OPEN_ALL_ROLLING_SHUTTER_AND_TURN_ON_ALL_LIGHT_IN_ALL_ROOM),
            CustomCommandEntity(TypeDevice.ROLLING_SHUTTER, TypeDevice.LIGHT, CustomCommand.CLOSE_ALL_ROLLING_SHUTTER_AND_TURN_OFF_ALL_LIGHT_IN_ALL_ROOM),

            // Porte de garage
            CustomCommandEntity(TypeDevice.GARAGE_DOOR, null, CustomCommand.TURN_ON_ALL_DOOR)
        )
    }
}