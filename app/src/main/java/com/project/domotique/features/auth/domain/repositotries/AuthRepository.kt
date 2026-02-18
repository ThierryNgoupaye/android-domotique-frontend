package com.project.domotique.features.auth.domain.repositotries

import com.project.domotique.features.auth.data.models.LoginResponse

interface AuthRepository {


    fun login(login: String, password: String,  doAction: (responseStatus: Int, responseData: LoginResponse?) -> Unit)
    fun register(login: String, password: String, doAction: (responseStatus: Int) -> Unit)

}