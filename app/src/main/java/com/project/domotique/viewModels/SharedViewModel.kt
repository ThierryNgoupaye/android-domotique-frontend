package com.project.domotique.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.domotique.models.entities.HouseEntity

class SharedViewModel : ViewModel() {

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