package com.project.domotique.repositories

import com.project.domotique.models.entities.HouseEntity
import com.project.domotique.models.entities.UserEntity
import com.project.domotique.models.models.HouseAccessRequest

interface HouseRepository {


    fun getHouses(doAction: (responseStatus: Int, responseData: List<HouseEntity>?)-> Unit, securityToken: String)

    fun grantHouseAccess(houseId: Int, requestData: HouseAccessRequest, doAction: (responseStatus: Int) -> Unit, securityToken: String)

    fun revokeHouseAccess(houseId: Int, requestData: HouseAccessRequest, doAction: (responseStatus: Int) -> Unit, securityToken: String)

    fun getHouseUserAccess(houseId: Int, doAction: (responseStatus: Int, responseData: List<UserEntity>?) -> Unit, securityToken: String)

}








