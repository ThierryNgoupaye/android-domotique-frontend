package com.project.domotique.repositories

import com.project.domotique.models.entities.CommandEntity
import com.project.domotique.models.entities.DeviceEntity
import com.project.domotique.models.models.CommandRequest
import com.project.domotique.models.models.DeviceDTO
import com.project.domotique.models.models.DeviceListResponse
import com.project.domotique.utils.Api
import com.project.domotique.utils.Constants
import java.net.HttpURLConnection

class DeviceRepositoryImpl : DeviceRepository {



    override fun getHouseDevices(houseId: Int, token: String, doAction : (statusCode: Int, responseData: List<DeviceEntity>?) -> Unit) {
        try
        {
            Api().get<DeviceListResponse>(
                path = "${Constants.BASE_URL_HOUSE}/${houseId}/devices",
                securityToken = token,
                onSuccess = { code, data ->
                    println("data: ${data?.devices}")
                    if (data != null) {
                        println(data)
                        val deviceList : ArrayList<DeviceEntity> = ArrayList()
                        for (device in data.devices) {
                            deviceList.add(DeviceDTO.toEntity(device))
                        }
                        doAction(code, deviceList)
                    } else {
                        doAction(code, null)
                    }
                },
            )
        }
        catch (error: Exception)
        {
            println(error)
            doAction(HttpURLConnection.HTTP_INTERNAL_ERROR, null)
        }
    }



    override fun  placeDeviceCommand(houseId: Int, deviceId:String, token: String, data: CommandRequest, doAction : (statusCode: Int, responseData: CommandEntity?) -> Unit)
    {
        try {

            Api().post<CommandRequest, CommandRequest>(
                path = "${Constants.BASE_URL_HOUSE}/${houseId}/devices/${deviceId}/command",
                data = data,
                securityToken = token,
                onSuccess = { code, data ->
                    doAction( code, CommandRequest.toEntity(data!!))
                },
            )
        }
        catch (error: Exception)
        {
            println(error)
            doAction(HttpURLConnection.HTTP_INTERNAL_ERROR, null)
        }

    }
}