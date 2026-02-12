package com.project.domotique.repositories

import com.project.domotique.models.models.LoginResponse


interface AuthRepository {


    fun login(login: String, password: String,  doAction: (responseStatus: Int, responseData: LoginResponse?) -> Unit)
    fun register(login: String, password: String, doAction: (responseStatus: Int) -> Unit)

}




