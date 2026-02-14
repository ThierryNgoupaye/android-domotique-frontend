package com.project.domotique.repositories

import com.project.domotique.models.entities.HouseEntity
import com.project.domotique.models.entities.UserEntity
import com.project.domotique.models.models.HouseAccessRequest
import com.project.domotique.utils.Api
import com.project.domotique.utils.Constants
import javax.net.ssl.HttpsURLConnection

class HouseRepositoryImpl : HouseRepository {

    override fun getHouses(
        doAction: (responseStatus: Int, responseData: List<HouseEntity>?) -> Unit,
        securityToken: String
    ) {
        try {
            Api().get<List<HouseEntity>?>(
                path = Constants.BASE_URL_HOUSE,
                onSuccess = {code, data ->
                    doAction(code, data)
                },
                securityToken = securityToken
            )
        } catch (error: Exception)
        {
            println(error.message)
            doAction(HttpsURLConnection.HTTP_INTERNAL_ERROR, null)
        }
    }



    override fun grantHouseAccess(
        houseId: Int,
        requestData: HouseAccessRequest,
        doAction: (responseStatus: Int) -> Unit,
        securityToken: String
    ) {
        try {
            Api().post<HouseAccessRequest>(
                path = "${Constants.BASE_URL_HOUSE}/$houseId/users",
                data = requestData,
                onSuccess = {code ->
                    doAction(code)
                },
                securityToken = securityToken
            )
        }
        catch(error: Exception)
        {
            println(error.message)
            doAction(HttpsURLConnection.HTTP_INTERNAL_ERROR)
        }
    }


    override fun revokeHouseAccess(
        houseId: Int,
        requestData: HouseAccessRequest,
        doAction: (responseStatus: Int) -> Unit,
        securityToken: String
    ) {
       try {
           Api().delete<HouseAccessRequest>(
               path = "${Constants.BASE_URL_HOUSE}/$houseId/users",
               data = requestData,
               onSuccess = {code ->
                   doAction(code)
               },
               securityToken = securityToken
           )
       }
       catch (error: Exception)
       {
           println(error.message)
           doAction(HttpsURLConnection.HTTP_INTERNAL_ERROR)
       }
    }


    override fun getHouseUserAccess(
        houseId: Int,
        doAction: (responseStatus: Int, responseData: List<UserEntity>?) -> Unit,
        securityToken: String
    ) {
        try {
            Api().get<List<UserEntity>?>(
                path = "${Constants.BASE_URL_HOUSE}/$houseId/users",
                onSuccess = {code, data ->
                    doAction(code, data)
                },
                securityToken = securityToken
            )
        }
        catch (error: Exception)
        {
            println(error.message)
            doAction(HttpsURLConnection.HTTP_INTERNAL_ERROR, null)
        }
    }
}