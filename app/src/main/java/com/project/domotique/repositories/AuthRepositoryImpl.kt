package com.project.domotique.repositories

import com.project.domotique.models.models.AuthRequest
import com.project.domotique.models.models.LoginResponse
import com.project.domotique.utils.Api
import com.project.domotique.utils.Constants

class AuthRepositoryImpl : AuthRepository {

    private lateinit var  requestData: AuthRequest



    override fun login(login: String, password: String, doAction: (responseStatus: Int, responseData: LoginResponse?) -> Unit)
    {
        try {
            this.requestData = AuthRequest(login=login, password=password)
            Api().post<AuthRequest, LoginResponse>(
                path = "${Constants.BASE_URL_USER}/auth",
                data = this.requestData,
                onSuccess = {
                    code, data -> doAction(code, data)
                }
            )
        }
        catch (error: Exception)
        {
            doAction(500, null)
            println("Error during login $error")
        }
    }



    override fun register(login: String, password: String, doAction: (responseStatus: Int) -> Unit)  {
        try {
            this.requestData = AuthRequest(login=login, password=password)
            Api().post<AuthRequest>(
                path = "${Constants.BASE_URL_USER}/register",
                data = this.requestData,
                onSuccess = { code -> doAction(code) }
            )
        }catch (error: Exception)
        {
            doAction(500)
            println(error)
        }
    }







}