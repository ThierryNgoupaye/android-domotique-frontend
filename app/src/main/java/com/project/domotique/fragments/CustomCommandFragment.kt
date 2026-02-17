package com.project.domotique.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.domotique.R
import com.project.domotique.adapters.CustomCommandListAdapter
import com.project.domotique.models.entities.CustomCommandEntity
import com.project.domotique.models.entities.DeviceEntity
import com.project.domotique.models.models.CommandRequest
import com.project.domotique.shared.LoadingDialog
import com.project.domotique.utils.CustomCommandResolver
import com.project.domotique.utils.LocalStorageManager
import com.project.domotique.viewModels.DeviceViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CustomCommandFragment : Fragment() {

    private lateinit var customCommandListRecyclerView: RecyclerView
    private lateinit var fragmentTitle: TextView
    private lateinit var localStorageManager: LocalStorageManager
    private lateinit var loadingDialog: LoadingDialog
    private val deviceViewModel: DeviceViewModel by viewModels()
    private var deviceList: List<DeviceEntity> = emptyList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.custom_command_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.localStorageManager = LocalStorageManager(requireContext())
        this.loadingDialog = LoadingDialog(requireContext())
        this.initUiElements(view)
        this.observeDeviceState()
        this.observeBatchCommandState()
        this.retrieveDeviceList()
    }

    private fun initUiElements(view: View) {
        this.customCommandListRecyclerView = view.findViewById(R.id.custom_command_list)
        this.fragmentTitle = view.findViewById(R.id.custom_command_fragment_title)
        this.fragmentTitle.text = "Commandes Rapides"
    }

    private fun retrieveDeviceList() {
        val houseId = localStorageManager.getHouseId()
        val token = localStorageManager.getToken()
        if (token != null && houseId != -1) {
            deviceViewModel.retrieveDeviceList(houseId, token)
        } else {
            Toast.makeText(requireContext(), "Session expirée", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeDeviceState() {
        deviceViewModel.deviceState.observe(viewLifecycleOwner) { state ->
            if (state.success) {
                state.data?.let { devices ->
                    this.deviceList = devices
                    this.initCustomCommandList()
                }
            }
            state.errors?.let { error ->
                if (error == "403") {
                    Toast.makeText(requireContext(), "Session expirée", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeBatchCommandState() {
        deviceViewModel.batchCommandState.observe(viewLifecycleOwner) { state ->
            if (state.success) {
                loadingDialog.dismiss()
                state.data?.let { result ->
                    when {
                        result.isEmpty -> {
                            Toast.makeText(
                                requireContext(),
                                "Tous les appareils sont déjà dans l'état souhaité",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        result.isFullSuccess -> {
                            Toast.makeText(
                                requireContext(),
                                "${result.success}/${result.total} commandes envoyées avec succès !",
                                Toast.LENGTH_LONG
                            ).show()
                            scheduleRefresh()
                        }
                        result.isPartialSuccess -> {
                            Toast.makeText(
                                requireContext(),
                                "${result.success}/${result.total} commandes envoyées, ${result.failed} ont échoué",
                                Toast.LENGTH_LONG
                            ).show()
                            scheduleRefresh()
                        }
                        else -> {
                            Toast.makeText(
                                requireContext(),
                                "Erreur lors de l'envoi des commandes",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            state.errors?.let { error ->
                loadingDialog.dismiss()
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initCustomCommandList() {
        val customCommands = CustomCommandResolver.buildCustomCommandList()
        val adapter = CustomCommandListAdapter(
            requireContext(),
            customCommands
        ) { customCommandEntity ->
            executeCustomCommand(customCommandEntity)
        }
        customCommandListRecyclerView.apply {
            this.adapter = adapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun executeCustomCommand(customCommandEntity: CustomCommandEntity) {
        val houseId = localStorageManager.getHouseId()
        val token = localStorageManager.getToken()
        if (token == null || houseId == -1) {
            Toast.makeText(requireContext(), "Session expirée", Toast.LENGTH_SHORT).show()
            return
        }
        val commands : List<Pair<DeviceEntity, CommandRequest>> = CustomCommandResolver.resolve(
            command = customCommandEntity.command,
            devices = deviceList
        )
        val message = if (commands.isEmpty()) {
            "Vérification en cours..."
        } else {
            "Envoi de ${commands.size} commande(s) en cours...\nCeci peut prendre quelques instants."
        }
        loadingDialog.show()
        deviceViewModel.placeBatchCommand(houseId=houseId, token=token, commands=commands)
    }

    private fun scheduleRefresh() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000)
            retrieveDeviceList()
        }
    }
}