package com.project.domotique.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.domotique.models.entities.HouseEntity
import com.project.domotique.models.entities.UserEntity
import com.project.domotique.models.models.HouseAccessRequest
import com.project.domotique.repositories.HouseRepository
import com.project.domotique.repositories.HouseRepositoryImpl
import com.project.domotique.uiState.UiState
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

class HouseViewModel : ViewModel() {


    private val houseRepository: HouseRepository = HouseRepositoryImpl()
    private val _homeState : MutableLiveData<UiState<List<HouseEntity>?>> =  MutableLiveData(UiState())
    private val _accessState : MutableLiveData<UiState<List<UserEntity>?>> =  MutableLiveData(UiState())
    val homeState : LiveData<UiState<List<HouseEntity>?>> =  _homeState
    val accessState : LiveData<UiState<List<UserEntity>?>> =  _accessState



    fun retrieveUserHouseList(token: String)
    {
        _homeState.postValue(
            UiState(loading = true)
        )
        viewModelScope.launch {
            this@HouseViewModel.houseRepository.getHouses(
                securityToken = token,
                doAction = { statusCode, data ->
                    run {
                        when (statusCode) {
                            200 -> {
                                println(data)
                                _homeState.postValue(
                                    UiState(
                                        loading = false,
                                        success = true,
                                        data = data
                                    )
                                )
                            }
                            403 -> {
                                _homeState.postValue(
                                    UiState(
                                        loading = false,
                                        errors = "Session expirée, Veuillez vous reconnecter !"
                                    )
                                )
                            }
                            else -> {
                                _homeState.postValue(
                                    UiState(
                                        loading = false,
                                        errors = "Une erreur est survenue, veuillez réessayer plus tard."
                                    )
                                )
                            }
                        }
                    }
                },
            )
        }
    }



    fun retrieveHouseAccessList(houseId: Int, token: String)
    {
        this._accessState.postValue(
            UiState(loading = true)
        )
        viewModelScope.launch {
            this@HouseViewModel.houseRepository.getHouseUserAccess(
                houseId= houseId,
                securityToken = token,
                doAction = { statusCode, data ->
                    run {
                        when (statusCode) {
                            HttpURLConnection.HTTP_OK -> {
                                _accessState.postValue(
                                    UiState(
                                        loading = false,
                                        success = true,
                                        data = data
                                    )
                                )
                            }
                            HttpURLConnection.HTTP_FORBIDDEN -> {
                                _accessState.postValue(
                                    UiState(
                                        loading = false,
                                        success = false,
                                        errors = HttpURLConnection.HTTP_FORBIDDEN.toString()
                                    )
                                )
                            }
                            else -> {
                                _accessState.postValue(
                                    UiState(
                                        loading = false,
                                        success = false,
                                        errors = "Une erreur interne est survenue, veuillez réessayer plus tard !"
                                    )
                                )
                            }
                        }
                    }
                }
            )
        }

    }



    fun grantHouseAccess(houseId: Int, userLogin: String, token: String)
    {
        this._accessState.postValue(UiState(loading = true))
        viewModelScope.launch {
            this@HouseViewModel.houseRepository.grantHouseAccess(
                houseId = houseId,
                securityToken = token,
                requestData = HouseAccessRequest(userLogin),
                doAction = { statusCode ->
                    when(statusCode){
                        HttpURLConnection.HTTP_OK ->
                            _accessState.postValue(
                                UiState(
                                    loading = false,
                                    success = true,
                                )
                            )
                        HttpURLConnection.HTTP_FORBIDDEN ->
                            _accessState.postValue(
                                UiState(
                                    loading = false,
                                    success = false,
                                    errors = HttpURLConnection.HTTP_FORBIDDEN.toString()
                                )
                            )
                        HttpURLConnection.HTTP_CONFLICT ->
                            _accessState.postValue(
                                UiState(
                                    loading = false,
                                    success = false,
                                    errors = "L'utilisateur est déjà associé à cette maison !"
                                )
                            )
                        HttpURLConnection.HTTP_BAD_REQUEST ->
                            _accessState.postValue(
                                UiState(
                                    loading = false,
                                    success = false,
                                    errors = "Vérifiez le nom d'utilisateur renseigné"
                                )
                            )
                        else ->
                            _accessState.postValue(
                                UiState(
                                    loading = false,
                                    success = false,
                                    errors = "Une erreur interne est survenue, veuillez réessayer plus tard !"
                                )
                            )
                    }
                }
            )
        }
    }



    fun revokeUserAccess(houseId: Int, userLogin: String, token: String)
    {
        this._accessState.postValue(UiState(loading = true))
        viewModelScope.launch {
            this@HouseViewModel.houseRepository.revokeHouseAccess(
                houseId = houseId,
                securityToken = token,
                requestData = HouseAccessRequest(userLogin),
                doAction = { statusCode ->
                    run {
                        when(statusCode){
                            HttpURLConnection.HTTP_OK -> {
                                _accessState.postValue(
                                    UiState(
                                        loading = false,
                                        success = true,
                                    )
                                )
                            }
                            HttpURLConnection.HTTP_FORBIDDEN ->  {
                                _accessState.postValue(
                                    UiState(
                                        loading = false,
                                        success = false,
                                        errors = HttpURLConnection.HTTP_FORBIDDEN.toString()
                                    )
                                )
                            }
                            HttpURLConnection.HTTP_BAD_REQUEST -> {
                                _accessState.postValue(
                                    UiState(
                                        loading = false,
                                        success = false,
                                        errors = "Vérifiez le nom d'utilisateur renseigné"
                                    )
                                )
                            }
                            else -> {
                                _accessState.postValue(
                                    UiState(
                                        loading = false,
                                        success = false,
                                        errors = "Une erreur interne est survenue, veuillez réessayer plus tard !"
                                    )
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}