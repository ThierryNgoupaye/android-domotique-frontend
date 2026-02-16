package com.project.domotique.repositories

import com.project.domotique.models.entities.CommandEntity
import com.project.domotique.models.entities.DeviceEntity
import com.project.domotique.models.models.CommandRequest

interface DeviceRepository {


    fun getHouseDevices(houseId : Int, token:String, doAction : (statusCode: Int, responseData: List<DeviceEntity>?) -> Unit)

    fun placeDeviceCommand(houseId: Int, deviceId:String, token: String, data: CommandRequest, doAction : (statusCode: Int, responseData: CommandEntity?) -> Unit)








}