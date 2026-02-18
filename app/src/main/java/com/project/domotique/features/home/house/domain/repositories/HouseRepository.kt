package com.project.domotique.features.home.house.domain.repositories

import com.project.domotique.features.auth.domain.entities.UserEntity
import com.project.domotique.features.home.house.domain.entites.HouseEntity
import com.project.domotique.features.home.houseAccess.data.HouseAccessRequest

interface HouseRepository {


    fun getHouses(doAction: (responseStatus: Int, responseData: List<HouseEntity>?)-> Unit, securityToken: String)

    fun grantHouseAccess(houseId: Int, requestData: HouseAccessRequest, doAction: (responseStatus: Int) -> Unit, securityToken: String)

    fun revokeHouseAccess(houseId: Int, requestData: HouseAccessRequest, doAction: (responseStatus: Int) -> Unit, securityToken: String)

    fun getHouseUserAccess(houseId: Int, doAction: (responseStatus: Int, responseData: List<UserEntity>?) -> Unit, securityToken: String)

}