package com.project.domotique.features.home.houseAccess.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.project.domotique.R
import com.project.domotique.features.auth.domain.entities.UserEntity
import com.project.domotique.features.home.house.presentation.viewModels.HouseViewModel
import com.project.domotique.utils.ErrorMessage
import com.project.domotique.utils.LocalStorageManager

class HouseAccessFragment: Fragment() {

    private lateinit var houseAccessListRecyclerView: RecyclerView
    private lateinit var emptyListWidget: ConstraintLayout
    private lateinit var addAccessBtnOnEmptyContent : Button
    private lateinit var localeStorageManager : LocalStorageManager
    private lateinit var errorContent : ConstraintLayout
    private lateinit var errorText : TextView
    private lateinit var addAccessFloatingActionBtn: FloatingActionButton
    private val houseViewModel : HouseViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return  inflater.inflate(R.layout.fragment_home_activity_house_access, container, false)
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
        this.errorContent = view.findViewById(R.id.error_content)
        this.errorText = view.findViewById(R.id.error_text)
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
            state.errors?.let { error ->
                this.displayErrors(error)
            }
        }
    }


    private fun retrieveHouseAccesList()
    {
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
        if(token == null || houseId == -1) this.displayErrors(ErrorMessage.FORBIDDEN.label)
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
        this.errorContent.visibility = View.GONE
        this.houseAccessListRecyclerView.visibility = View.VISIBLE
        this.addAccessFloatingActionBtn.visibility = View.VISIBLE
    }

    private fun displayEmptyAccessListView()
    {
        this.houseAccessListRecyclerView.visibility = View.GONE
        this.errorContent.visibility = View.GONE
        this.addAccessFloatingActionBtn.visibility = View.GONE
        this.emptyListWidget.visibility = View.VISIBLE
        this.addAccessBtnOnEmptyContent = this.emptyListWidget.findViewById(R.id.add_access_button)
        this.addAccessBtnOnEmptyContent.setOnClickListener {
            this.openAddUserAccessPopup()
        }
    }

    private fun displayErrors(error: String )
    {
        this.houseAccessListRecyclerView.visibility = View.GONE
        this.addAccessFloatingActionBtn.visibility = View.GONE
        this.emptyListWidget.visibility = View.GONE
        this.errorContent.visibility = View.VISIBLE
        this.errorText.text = error
    }


}