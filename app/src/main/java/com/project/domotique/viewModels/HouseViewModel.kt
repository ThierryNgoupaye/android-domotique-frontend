package com.project.domotique.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.domotique.models.entities.HouseEntity
import com.project.domotique.repositories.HouseRepository
import com.project.domotique.repositories.HouseRepositoryImpl
import com.project.domotique.uiState.UiState
import kotlinx.coroutines.launch

class HouseViewModel() : ViewModel() {


    private val houseRepository: HouseRepository = HouseRepositoryImpl()

    private val _homeState : MutableLiveData<UiState<List<HouseEntity>?>> =  MutableLiveData(UiState<List<HouseEntity>?>())
    val homeState : LiveData<UiState<List<HouseEntity>?>> =  _homeState



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
                                        errors = "Une erreur est survenue, veuillez réessayer plutard."
                                    )
                                )
                            }
                        }
                    }
                },
            )
        }
    }






}