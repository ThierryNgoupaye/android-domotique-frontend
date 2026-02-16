package com.project.domotique.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class LocalStorageManager {

    private  var context: Context
    private var sharedPreferenceName: String = Constants.SHARED_PREFERENCES_KEY_NAME
    private var sharedPreferences: SharedPreferences


    constructor(context: Context, sharedPreferenceName: String) {
        this.context = context
        this.sharedPreferenceName = sharedPreferenceName
        this.sharedPreferences = this.context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE)
    }


    constructor(context: Context) {
        this.context = context
        this.sharedPreferences = this.context.getSharedPreferences(sharedPreferenceName, Context.MODE_PRIVATE)
    }




    fun saveToken(token: String) {
        this.sharedPreferences.edit {
            putString(Constants.TOKEN_KEY_NAME, token)
        }
    }


    fun getToken(): String? {
        println("token loadé : ${this.sharedPreferences.getString(Constants.TOKEN_KEY_NAME, null)}")
        return this.sharedPreferences.getString(Constants.TOKEN_KEY_NAME, null)
    }


    fun removeToken() {
        this.sharedPreferences.edit {
            remove(Constants.TOKEN_KEY_NAME)
        }
    }


    fun saveOnboardingPreference(registered: Boolean)
    {
        this.sharedPreferences.edit {
            putBoolean(Constants.ONBOARDING_PREFERENCES_KEY_NAME, registered)
        }
    }


    fun getOnboardingPreference(): Boolean
    {
        return this.sharedPreferences.getBoolean(Constants.ONBOARDING_PREFERENCES_KEY_NAME, false)
    }


    fun removeOnboardingPreference()
    {
        this.sharedPreferences.edit {
            remove(Constants.ONBOARDING_PREFERENCES_KEY_NAME)
        }
    }


    fun saveUserName(userName: String)
    {
        this.sharedPreferences.edit {
            putString(Constants.USER_NAME_KEY_NAME, userName)
        }
    }

    fun getUserName(): String? {
        return this.sharedPreferences.getString(Constants.USER_NAME_KEY_NAME, "Guess User")
    }


    fun removeUserName() {
        this.sharedPreferences.edit {
            remove(Constants.USER_NAME_KEY_NAME)
        }
    }

    fun setHouseId(houseId: Int) {
        this.sharedPreferences.edit {
            putInt(Constants.HOUSE_ID_KEY_NAME, houseId)
        }
    }

    fun getHouseId(): Int {
        return this.sharedPreferences.getInt(Constants.HOUSE_ID_KEY_NAME, -1)
    }

    fun removeHouseId(){
        this.sharedPreferences.edit {
            remove(Constants.HOUSE_ID_KEY_NAME)
        }
    }


    fun clearPreferences() {
        this.removeHouseId()
        this.removeToken()
        this.removeUserName()
    }

    fun clearAll()
    {
        this.clearPreferences()
        this.removeOnboardingPreference()
    }


}







