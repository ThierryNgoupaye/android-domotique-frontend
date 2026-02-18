package com.project.domotique.features.home.devices.presentation.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.project.domotique.R
import com.project.domotique.features.home.devices.presentation.adapters.RoomDeviceAdapter
import com.project.domotique.features.home.HomeSharedViewModel
import com.project.domotique.features.home.devices.presentation.adapters.HouseDeviceTypeAdapter
import com.project.domotique.features.home.devices.domain.entities.DeviceEntity
import com.project.domotique.features.home.devices.domain.entities.RoomDevices
import com.project.domotique.features.home.devices.data.CommandRequest
import com.project.domotique.shared.LoadingDialog
import com.project.domotique.utils.LocalStorageManager
import com.project.domotique.utils.RoomDistributor
import com.project.domotique.features.home.devices.presentation.viewModels.DeviceViewModel
import com.project.domotique.utils.ErrorMessage

class DeviceFragment : Fragment() {

    private val homeSharedViewModel: HomeSharedViewModel by activityViewModels()
    private val deviceViewModel: DeviceViewModel by viewModels()
    private var  defaultHouseId : Int = -1
    private lateinit var  deviceFragmentTitle: TextView
    private lateinit var errorContent : ConstraintLayout
    private lateinit var errorText : TextView
    private lateinit var roomsRecyclerView: RecyclerView
    private lateinit var localeStorageManager: LocalStorageManager
    private lateinit var deviceTypeRecyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private var loadingDialog: LoadingDialog? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_home_activity_device, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.initWidgets(view)
        this.setupSwipeRefresh()
        this.observeSelectedHouse(view)
        this.observeHouseDevicesUi()
        this.observeCommandState()
        this.getHouseDevices()
    }


    private fun initWidgets(view: View)
    {
        this.roomsRecyclerView = view.findViewById(R.id.rooms_recycler_view)
        this.deviceTypeRecyclerView = view.findViewById(R.id.different_house_devices)
        this.swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        this.errorContent = view.findViewById(R.id.error_content)
        this.errorText = view.findViewById(R.id.error_text)
        this.localeStorageManager = LocalStorageManager(requireContext())
    }

    private fun displayErrors(error: String )
    {
        this.swipeRefreshLayout.visibility = View.GONE
        this.errorContent.visibility = View.VISIBLE
        this.errorText.text = error
    }


    private fun displayMainContent()
    {
        this.errorContent.visibility = View.GONE
        this.swipeRefreshLayout.visibility = View.VISIBLE
    }

    private fun setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            getHouseDevices()
        }
        swipeRefreshLayout.setColorSchemeResources(
            R.color.orange_primary_500,
            R.color.orange_primary_600,
            R.color.orange_primary_700
        )
    }

    private fun observeSelectedHouse(view: View) {
        this.homeSharedViewModel.selectedHouseId.observe(viewLifecycleOwner) { house ->
            if (house != null) displayDevicesForHouse(view, house.houseId)
            else displayDefaultHouse(view)
        }
        if (this.homeSharedViewModel.selectedHouseId.value == null) {
            displayDefaultHouse(view)
        }
    }

    private fun observeHouseDevicesUi() {
        displayMainContent()
        this.deviceViewModel.deviceState.observe(viewLifecycleOwner) { state ->
            if (state.loading) {
                if (!swipeRefreshLayout.isRefreshing) {
                    loadingDialog = LoadingDialog.show(requireContext(), "Chargement des appareils...")
                }
            } else {
                swipeRefreshLayout.isRefreshing = false
                loadingDialog?.dismiss()
                loadingDialog = null
            }
            if (state.success) {
                state.data?.let { devices ->
                    val roomDevices = RoomDistributor.distributeDevices(devices)
                    this.initDevicesTypes(roomDevices)
                    this.initDeviceList(roomDevices)
                }
            }
            state.errors?.let { error ->
                displayErrors(error)
            }
        }
    }




    private fun getHouseDevices()
    {
        displayMainContent()
        val houseId = this.homeSharedViewModel.getSelectedHouse()?.houseId ?: this.defaultHouseId
        val localStorageManager = LocalStorageManager(requireContext())
        val token = localStorageManager.getToken()
        if(token == null) displayErrors(ErrorMessage.FORBIDDEN.label)
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
        val deviceTypeList: List<DeviceEntity.TypeDevice> = listOf(DeviceEntity.TypeDevice.ROLLING_SHUTTER, DeviceEntity.TypeDevice.LIGHT, DeviceEntity.TypeDevice.GARAGE_DOOR)
        val adapter = HouseDeviceTypeAdapter(requireContext(), deviceTypeList, deviceRoomList)
        this.deviceTypeRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        this.deviceTypeRecyclerView.adapter = adapter
    }


    private fun initDeviceList(deviceRoomList: List<RoomDevices>) {
        val adapter = RoomDeviceAdapter(requireContext(), deviceRoomList) { device, commandLabel ->
            val houseId = this.homeSharedViewModel.getSelectedHouse()?.houseId ?: this.defaultHouseId
            val token = this.localeStorageManager.getToken()
            if (token != null) {
                val command: String? = device.availableCommands
                    .find { it.command.label == commandLabel } ?.command?.value

                if (command != null) {
                    deviceViewModel.placeCommand(
                        houseId = houseId,
                        deviceId = device.id,
                        token = token,
                        data = CommandRequest(command)
                    )
                }
            } else displayErrors(ErrorMessage.FORBIDDEN.label)
        }
        this.roomsRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        this.roomsRecyclerView.adapter = adapter
    }


    private fun observeCommandState() {
        this.displayMainContent()
        deviceViewModel.commandState.observe(viewLifecycleOwner) { state ->
            if (state.loading) {
                loadingDialog = LoadingDialog.show(requireContext(), "Envoi de la commande...")
            } else {
                loadingDialog?.dismiss()
                loadingDialog = null
            }
            if (state.success) {
                Toast.makeText(requireContext(), "Commande envoyée avec succès !", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    swipeRefreshLayout.isRefreshing = true
                    getHouseDevices()
                }, 5000)

            } else {
                state.errors?.let { error ->
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}