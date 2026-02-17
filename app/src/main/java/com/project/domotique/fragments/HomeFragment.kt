package com.project.domotique.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.project.domotique.R
import com.project.domotique.activities.LoginActivity
import com.project.domotique.adapters.HouseAdapter
import com.project.domotique.adapters.HouseFilterAdapter
import com.project.domotique.animations.GridViewDecoration
import com.project.domotique.models.entities.HouseEntity
import com.project.domotique.utils.LocalStorageManager
import com.project.domotique.viewModels.HouseViewModel
import com.project.domotique.viewModels.SharedViewModel


class HomeFragment: Fragment() {

    private lateinit var houseFilterRecycleView: RecyclerView
    private  lateinit var houseListRecycleView: RecyclerView
    private lateinit var localeStorageManager : LocalStorageManager
    private val houseViewModel : HouseViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.home_fragment, container, false)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        this.localeStorageManager =  LocalStorageManager(requireContext())
        this.observeHouseUi(view)
        this.fetchHouseList()
        this.initHouseFilterList(view)
    }


    private fun initHouseFilterList(view: View)
    {
        val mockedData: List<String> = listOf(
            "Toutes",
            "Mes maisons",
            "Invités"
        )
        val adapter = HouseFilterAdapter(filterList= mockedData) { filter ->
            Toast.makeText(requireContext(), "Filtre: $filter", Toast.LENGTH_SHORT).show()
        }
        this.houseFilterRecycleView = view.findViewById(R.id.houses_filter)
        this.houseFilterRecycleView.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }


    private fun initHouseList(view: View, houseList: List<HouseEntity>)
    {
        this.houseListRecycleView = view.findViewById(R.id.house_list)
        val houseListAdapter = HouseAdapter(houseList = houseList) { house ->
            sharedViewModel.setSelectedHouse(house)
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
        this.houseViewModel.homeState.observe(viewLifecycleOwner){ state ->
            state.errors?.let { errorMessage ->
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
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
                if(state.data == null)
                {
                    Toast.makeText(requireContext(), "Erreur", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun fetchHouseList()
    {
        val localStorageManager = LocalStorageManager(requireContext())
        val token = localStorageManager.getToken()
        if(token == null)
        {
            Toast.makeText(requireContext(), "Votre session a expiré, veuillez vous connecter q", Toast.LENGTH_SHORT).show()
            val intentToLogin = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intentToLogin)
        }
        else this.houseViewModel.retrieveUserHouseList(token)
    }
}