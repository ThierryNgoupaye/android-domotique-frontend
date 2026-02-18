package com.project.domotique.features.home.house.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.domotique.R
import com.project.domotique.features.home.house.domain.entites.HouseEntity
import com.project.domotique.features.home.house.domain.entites.HouseOwner

class HouseAdapter(
    val context: Context,
    private val houseList: List<HouseEntity>?,
    private val onItemClick: (house: HouseEntity?) -> Unit,
) : RecyclerView.Adapter<HouseAdapter.HouseViewHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.layout_house_item, parent, false)
        return HouseViewHolder(view)
    }


    override fun onBindViewHolder(holder: HouseViewHolder, position: Int) {
        val house: HouseEntity? = houseList?.get(position)
        holder.bind(house, context) {
            onItemClick(house)
        }
    }


    override fun getItemCount(): Int
    {
       return houseList?.size ?: 0
    }



    class HouseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(house: HouseEntity?, context: Context, onItemClick: (house: HouseEntity?) -> Unit) {
            val itemText = itemView.findViewById<TextView>(R.id.house_owner_text)
            val itemImage = itemView.findViewById<ImageView>(R.id.ic_house_owner)
            val itemNumberText = itemView.findViewById<TextView>(R.id.house_number_text)

            itemNumberText.text = context.getString(R.string.house_number_label, house?.houseId)
            if(house?.owner == true)
            {
                itemText.text = HouseOwner.OWNER.label
                itemImage.visibility = View.VISIBLE
                itemText.setTextColor(itemView.context.getColor(R.color.orange_primary_400))
            }
            else
            {
                itemText.text = HouseOwner.GUEST.label
                itemText.setTextColor(itemView.context.getColor(R.color.secondary_500))
            }
            itemView.setOnClickListener {
                onItemClick(house)
            }

        }
    }
}