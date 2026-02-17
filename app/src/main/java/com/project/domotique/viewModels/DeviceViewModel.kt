package com.project.domotique.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.domotique.models.entities.BatchCommandResult
import com.project.domotique.models.entities.CommandEntity
import com.project.domotique.models.entities.DeviceEntity
import com.project.domotique.models.models.CommandRequest
import com.project.domotique.repositories.DeviceRepository
import com.project.domotique.repositories.DeviceRepositoryImpl
import com.project.domotique.uiState.UiState
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class DeviceViewModel :  ViewModel() {


    private val deviceRepository : DeviceRepository = DeviceRepositoryImpl()
    private val _deviceState = MutableLiveData<UiState<List<DeviceEntity>>>()
    private val _commandState = MutableLiveData<UiState<CommandEntity>>()
    private val _batchCommandState = MutableLiveData<UiState<BatchCommandResult>>()
    val commandState : LiveData<UiState<CommandEntity>> = _commandState
    val deviceState: LiveData<UiState<List<DeviceEntity>>> = _deviceState
    val batchCommandState: LiveData<UiState<BatchCommandResult>> = _batchCommandState



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



    fun placeCommand(houseId: Int, deviceId: String, token: String, data: CommandRequest)
    {
        _commandState.postValue(UiState(loading = true))
        viewModelScope.launch {
             deviceRepository.placeDeviceCommand(
                 houseId = houseId,
                 deviceId = deviceId,
                 token = token,
                 data = data,
                 doAction = {code ->
                     when(code){
                         200 -> {
                             _commandState.postValue(
                                 UiState(
                                     success = true,
                                 )
                             )
                         }

                         403 -> {
                             _commandState.postValue(
                                 UiState(
                                     success = false,
                                     errors = "403"
                                 )
                             )
                         }
                         else -> {
                             _commandState.postValue(
                                 UiState(
                                     success = false,
                                     errors = "Une erreur interne est survenue, veuillez réessayer plus tard !"
                                 )
                             )
                         }
                     }
                 }
             )
        }
    }



    fun placeBatchCommand(houseId: Int, token: String, commands: List<Pair<DeviceEntity, CommandRequest>>
    ) {
        if (commands.isEmpty()) {
            _batchCommandState.postValue(
                UiState(
                    success = true,
                    data = BatchCommandResult(total = 0, success = 0, failed = 0)
                )
            )
            return
        }
        _batchCommandState.postValue(UiState(loading = true))
        viewModelScope.launch {
            val results: List<Boolean> = commands.map { (device, commandRequest) ->
                async {
                    suspendCancellableCoroutine { continuation ->
                        deviceRepository.placeDeviceCommand(
                            houseId = houseId,
                            deviceId = device.id,
                            token = token,
                            data = commandRequest,
                            doAction = { statusCode ->
                                continuation.resume(statusCode == 200)
                            }
                        )
                    }
                }
            }.awaitAll()
            val successCount = results.count { it }
            val failedCount = results.count { !it }

            _batchCommandState.postValue(
                UiState(
                    loading = false,
                    success = true,
                    data = BatchCommandResult(
                        total = commands.size,
                        success = successCount,
                        failed = failedCount
                    )
                )
            )
        }
    }
}