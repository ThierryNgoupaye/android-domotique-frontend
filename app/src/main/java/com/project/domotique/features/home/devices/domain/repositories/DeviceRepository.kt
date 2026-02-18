package com.project.domotique.features.home.devices.domain.repositories

import com.project.domotique.features.home.devices.domain.entities.DeviceEntity
import com.project.domotique.features.home.devices.data.CommandRequest

interface DeviceRepository {


    fun getHouseDevices(houseId : Int, token:String, doAction : (statusCode: Int, responseData: List<DeviceEntity>?) -> Unit)

    fun placeDeviceCommand(houseId: Int, deviceId:String, token: String, data: CommandRequest, doAction : (statusCode: Int) -> Unit)








}