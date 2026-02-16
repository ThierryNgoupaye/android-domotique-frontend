package com.project.domotique.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.domotique.R
import com.project.domotique.activities.LoginActivity
import com.project.domotique.adapters.HouseDeviceTypeAdapter
import com.project.domotique.adapters.RoomDeviceAdapter
import com.project.domotique.models.entities.DeviceEntity.TypeDevice
import com.project.domotique.models.entities.RoomDevices
import com.project.domotique.utils.LocalStorageManager
import com.project.domotique.utils.RoomDistributor
import com.project.domotique.viewModels.DeviceViewModel
import com.project.domotique.viewModels.SharedViewModel

class DeviceFragment : Fragment() {


    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val deviceViewModel: DeviceViewModel by viewModels()

    private var  defaultHouseId : Int = -1

    private lateinit var  deviceFragmentTitle: TextView

    private lateinit var roomsRecyclerView: RecyclerView

    private lateinit var deviceTypeRecyclerView: RecyclerView



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.layout_device_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.roomsRecyclerView = view.findViewById(R.id.rooms_recycler_view)
        this.deviceTypeRecyclerView = view.findViewById(R.id.different_house_devices)
        this.observeSelectedHouse(view)
        this.observeHouseDevicesUi()
        this.getHouseDevices()
    }


    private fun observeSelectedHouse(view: View) {
        this.sharedViewModel.selectedHouseId.observe(viewLifecycleOwner) { house ->
            if (house != null) displayDevicesForHouse(view, house.houseId)
            else displayDefaultHouse(view)
        }
        if (this.sharedViewModel.selectedHouseId.value == null) {
            displayDefaultHouse(view)
        }
    }

    private fun observeHouseDevicesUi() {
        this.deviceViewModel.deviceState.observe(viewLifecycleOwner) { state ->
            if (state.success) {
                state.data?.let { devices ->
                    val roomDevices = RoomDistributor.distributeDevices(devices)
                    this.initDevicesTypes(roomDevices)
                    this.initDeviceList(roomDevices)
                }
            } else {
                state.errors?.let { error ->
                    if (error == "403") {
                        Toast.makeText(requireContext(), "Votre session a expiré", Toast.LENGTH_SHORT).show()
                        val intentToLogin = Intent(requireContext(), LoginActivity::class.java)
                        startActivity(intentToLogin)
                    }
                    else {
                        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            if (state.loading) {
                Toast.makeText(requireContext(), "Chargement...", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun getHouseDevices()
    {
        val houseId = this.sharedViewModel.getSelectedHouse()?.houseId ?: this.defaultHouseId
        val localStorageManager = LocalStorageManager(requireContext())
        val token = localStorageManager.getToken()
        if(token == null){
            localStorageManager.clearPreferences()
            Toast.makeText(requireContext(),"Votre session a expiré, veuillez vous reconnecter",Toast.LENGTH_SHORT).show()
            val intentToLogin = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intentToLogin)
        }
        else {
            this.deviceViewModel.retrieveDeviceList(
                houseId = houseId,
                token = token
            )
        }
    }



    private fun displayDevicesForHouse(view: View, houseId: Int) {
        this.deviceFragmentTitle = view.findViewById(R.id.device_fragment_title)
        this.deviceFragmentTitle.text = requireContext().getString(R.string.device_fragment_title, houseId)
    }


    private fun displayDefaultHouse(view: View) {
        this.deviceFragmentTitle = view.findViewById(R.id.device_fragment_title)
        this.deviceFragmentTitle.text = requireContext().getString(R.string.device_fragment_default_title)
        val localStorageManager = LocalStorageManager(requireContext())
        defaultHouseId = localStorageManager.getHouseId()
    }



    private fun initDevicesTypes(deviceRoomList: List<RoomDevices>)
    {
        val deviceTypeList: List<TypeDevice> = listOf(TypeDevice.ROLLING_SHUTTER, TypeDevice.LIGHT, TypeDevice.GARAGE_DOOR)
        val adapter = HouseDeviceTypeAdapter(deviceTypeList,deviceRoomList)
        this.deviceTypeRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        this.deviceTypeRecyclerView.adapter = adapter
    }


    private fun initDeviceList(deviceRoomList: List<RoomDevices>)
    {
        val adapter = RoomDeviceAdapter(deviceRoomList)
        this.roomsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        this.roomsRecyclerView.adapter = adapter
    }









}