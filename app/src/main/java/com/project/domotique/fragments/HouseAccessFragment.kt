package com.project.domotique.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.project.domotique.R
import com.project.domotique.activities.LoginActivity
import com.project.domotique.adapters.HouseAccessListAdapter
import com.project.domotique.models.entities.UserEntity
import com.project.domotique.shared.AddHouseAccessDialog
import com.project.domotique.utils.LocalStorageManager
import com.project.domotique.viewModels.HouseViewModel

class HouseAccessFragment: Fragment() {

    private lateinit var houseAccessListRecyclerView: RecyclerView
    private lateinit var emptyListWidget: ConstraintLayout
    private lateinit var addAccessBtnOnEmptyContent : Button
    private lateinit var localeStorageManager : LocalStorageManager
    private lateinit var addAccessFloatingActionBtn: FloatingActionButton
    private val houseViewModel : HouseViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.house_access_fragment, container, false)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.localeStorageManager = LocalStorageManager(requireContext())
        this.initUiElements(view)
        this.observeHouseAccesUi()
        this.retrieveHouseAccesList()
        this.addAccessToUser()
    }


    private fun initUiElements(view: View)
    {
        this.houseAccessListRecyclerView = view.findViewById(R.id.house_access_list)
        this.emptyListWidget = view.findViewById(R.id.no_centent_view)
        this.addAccessFloatingActionBtn = view.findViewById(R.id.floatingActionButton)
    }


    private fun observeHouseAccesUi()
    {
        val userLogin = localeStorageManager.getUserName()
        this.houseViewModel.accessState.observe(viewLifecycleOwner){state ->
            if(state.loading){
                Toast.makeText(requireContext(), "Chargement en cours...", Toast.LENGTH_SHORT).show()
            }
            if(state.success){
                state.data?.let { houseAccessList ->
                    if(houseAccessList.isEmpty() || (houseAccessList.size == 1 && houseAccessList[0].userLogin == userLogin!!)){
                        this.displayEmptyAccessListView()
                    }
                    else {
                        val finalHouseAccessList = houseAccessList.filter { it.userLogin != userLogin }
                        this.initAccessList(finalHouseAccessList)
                        this.displayListView()
                    }
                }
            }
        }
    }


    private fun retrieveHouseAccesList()
    {
        val localeStorageManager = LocalStorageManager(requireContext())
        val houseId = localeStorageManager.getHouseId()
        val token = localeStorageManager.getToken()
        this.ensureUserLoggedIn(token, houseId)
        this.houseViewModel.retrieveHouseAccessList(
            houseId = houseId,
            token = token!!
        )


    }

    private fun ensureUserLoggedIn(token: String?, houseId: Int)
    {
        if(token == null || houseId == -1){
            Toast.makeText(requireContext(), "Session expirée", Toast.LENGTH_SHORT).show()
            val intentToLogin = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intentToLogin)
            localeStorageManager.clearPreferences()
        }
    }


    private fun initAccessList(houseAccessList: List<UserEntity>)
    {
        val accessHouseListAdapter = HouseAccessListAdapter(
            context = requireContext(),
            onConfirm = { user ->
                this.houseViewModel.revokeUserAccess(
                    houseId = localeStorageManager.getHouseId(),
                    userLogin = user,
                    token = localeStorageManager.getToken()!!
                )
            },
            houseAccessList = houseAccessList
        )
        this.houseAccessListRecyclerView.apply {
            this.adapter = accessHouseListAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }


    private fun displayEmptyAccessListView()
    {
        this.houseAccessListRecyclerView.visibility = View.GONE
        this.emptyListWidget.visibility = View.VISIBLE
        this.addAccessFloatingActionBtn.visibility = View.GONE
        this.addAccessBtnOnEmptyContent = this.emptyListWidget.findViewById(R.id.add_access_button)
        this.addAccessBtnOnEmptyContent.setOnClickListener {
            this.openAddUserAccessPopup()
        }
    }

    private fun addAccessToUser()
    {
        this.addAccessFloatingActionBtn.setOnClickListener{
            this.openAddUserAccessPopup()
        }
    }


    private fun openAddUserAccessPopup()
    {
        val houseId = localeStorageManager.getHouseId()
        val token = localeStorageManager.getToken()
        this.ensureUserLoggedIn(token = token, houseId= houseId)
        AddHouseAccessDialog(
            context = requireContext(),
            onAccessGranted = { userLogin, onResult ->
                houseViewModel.grantHouseAccess(
                    houseId = houseId,
                    userLogin = userLogin,
                    token = token!!
                )
                houseViewModel.accessState.observe(viewLifecycleOwner) { state ->
                    if (!state.loading) {
                        if (state.success && state.data == null) {
                            onResult(true, null)
                            retrieveHouseAccesList()
                        }
                        state.errors?.let { error ->
                            onResult(false, error)
                        }
                    }
                }
            }
        ).show()
    }


    private fun displayListView()
    {
        this.emptyListWidget.visibility = View.GONE
        this.houseAccessListRecyclerView.visibility = View.VISIBLE
        this.addAccessFloatingActionBtn.visibility = View.VISIBLE
    }

}