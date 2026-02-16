package com.project.domotique.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.domotique.models.entities.DeviceEntity
import com.project.domotique.repositories.DeviceRepository
import com.project.domotique.repositories.DeviceRepositoryImpl
import com.project.domotique.uiState.UiState
import kotlinx.coroutines.launch

class DeviceViewModel :  ViewModel() {


    private val deviceRepository : DeviceRepository = DeviceRepositoryImpl()

    private val _deviceState = MutableLiveData<UiState<List<DeviceEntity>>>()
    val deviceState: LiveData<UiState<List<DeviceEntity>>> = _deviceState




    fun retrieveDeviceList(houseId: Int, token: String)
    {
        _deviceState.postValue(UiState(loading = true))
        viewModelScope.launch {
            deviceRepository.getHouseDevices(houseId, token) { statusCode, data ->
                when(statusCode){
                    200 -> {
                        println(data)
                        _deviceState.postValue(
                            UiState(
                                success = true,
                                data = data
                            )
                        )
                    }
                    403 -> {
                        _deviceState.postValue(
                            UiState(
                                success = false,
                                errors = "403"
                            )
                        )
                    }
                    500 -> {
                        _deviceState.postValue(
                            UiState(
                                success = false,
                                errors = "Erreur serveur, veuillez réessayer plutard !"
                            )
                        )
                    }
                    400 -> {
                        _deviceState.postValue(
                            UiState(
                                success = false,
                                errors = "Mauvaise requete"
                            )
                        )
                    }
                    else  -> {
                        _deviceState.postValue(
                            UiState(
                                success = false,
                                errors = "Erreur interne du servezr"
                            )
                        )
                    }
                }
            }
        }
    }



    fun placeCommand()
    {

    }






}