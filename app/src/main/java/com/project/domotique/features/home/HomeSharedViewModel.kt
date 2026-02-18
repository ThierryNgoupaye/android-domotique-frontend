package com.project.domotique.features.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.domotique.features.home.house.domain.entites.HouseEntity

class HomeSharedViewModel : ViewModel() {

    private val _selectedHouseId = MutableLiveData<HouseEntity?>()
    val selectedHouseId: LiveData<HouseEntity?> = _selectedHouseId


    fun setSelectedHouse(house: HouseEntity?) {
        _selectedHouseId.value = house
    }


    fun getSelectedHouse(): HouseEntity? {
        return _selectedHouseId.value
    }


    fun clearSelection() {
        _selectedHouseId.value = null
    }
}