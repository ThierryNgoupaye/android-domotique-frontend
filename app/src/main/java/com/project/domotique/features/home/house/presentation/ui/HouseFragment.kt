package com.project.domotique.features.home.house.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.domotique.R
import com.project.domotique.features.home.house.presentation.adapters.HouseAdapter
import com.project.domotique.features.home.house.presentation.adapters.HouseFilterAdapter
import com.project.domotique.utils.animations.GridViewDecoration
import com.project.domotique.features.home.house.domain.entites.HouseEntity
import com.project.domotique.utils.LocalStorageManager
import com.project.domotique.features.home.house.presentation.viewModels.HouseViewModel
import com.project.domotique.features.home.HomeSharedViewModel
import com.project.domotique.features.home.house.domain.entites.HouseFilter
import com.project.domotique.utils.ErrorMessage

class HouseFragment: Fragment() {

    private lateinit var houseFilterRecycleView: RecyclerView
    private  lateinit var houseListRecycleView: RecyclerView
    private lateinit var localeStorageManager : LocalStorageManager
    private lateinit var errorContent: ConstraintLayout
    private lateinit var errorText: TextView
    private val houseViewModel : HouseViewModel by viewModels()
    private val homeSharedViewModel: HomeSharedViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.fragment_home_activity_house, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        this.initWidgets(view)

        this.observeHouseUi(view)
        this.fetchHouseList()
        this.initHouseFilterList(view)
    }


    private fun initWidgets(view: View)
    {
        this.houseListRecycleView = view.findViewById(R.id.house_list)
        this.errorContent = view.findViewById(R.id.error_content)
        this.errorText = view.findViewById(R.id.error_text)
        this.houseFilterRecycleView = view.findViewById(R.id.houses_filter)
        this.localeStorageManager = LocalStorageManager(requireContext())
    }

    private fun initHouseFilterList(view: View)
    {
        val filterList: List<HouseFilter> = listOf(
            HouseFilter.ALL,
            HouseFilter.MY_HOUSES,
            HouseFilter.INVITED
        )
        val adapter = HouseFilterAdapter(filterList = filterList) { filter ->
            Toast.makeText(requireContext(), "Filtre: $filter", Toast.LENGTH_SHORT).show()
        }
        this.houseFilterRecycleView.apply {
            this.adapter = adapter
            this.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }


    private fun initHouseList(view: View, houseList: List<HouseEntity>)
    {
        val houseListAdapter = HouseAdapter(houseList = houseList, context = requireContext()) { house ->
            homeSharedViewModel.setSelectedHouse(house)
            findNavController().navigate(R.id.deviceFragment)
        }
        houseListRecycleView.apply {
            this.adapter = houseListAdapter
            this.layoutManager = GridLayoutManager(requireContext(), 2)
            this.addItemDecoration(GridViewDecoration())
        }
    }


    private fun observeHouseUi(view: View)
    {
        this.displayMainContent()
        this.houseViewModel.homeState.observe(viewLifecycleOwner){ state ->
            state.errors?.let { errorMessage ->
                this.displayErrors(errorMessage)
            }
            if(state.success)
            {
                state?.data?.let { list ->
                    for(house in list)
                    {
                        if(house.owner)
                        {
                            localeStorageManager.setHouseId(house.houseId)
                        }
                    }
                    this.initHouseList(view, list)
                }
            }
        }
    }

    private fun fetchHouseList()
    {
        this.displayMainContent()
        val localStorageManager = LocalStorageManager(requireContext())
        val token = localStorageManager.getToken()
        if(token == null)
        {
            this.displayErrors(ErrorMessage.FORBIDDEN.label)

        }
        else this.houseViewModel.retrieveUserHouseList(token)
    }


    private fun displayErrors(error: String )
    {
        this.houseListRecycleView.visibility = View.GONE
        this.errorContent.visibility = View.VISIBLE
        this.errorText.text = error
    }

    private fun displayMainContent()
    {
        this.errorContent.visibility = View.GONE
        this.houseListRecycleView.visibility = View.VISIBLE

    }
}