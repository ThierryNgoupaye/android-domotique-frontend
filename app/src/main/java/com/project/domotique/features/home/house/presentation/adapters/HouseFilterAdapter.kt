package com.project.domotique.features.home.house.presentation.adapters

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.domotique.R
import com.project.domotique.features.home.house.domain.entites.HouseFilter

class HouseFilterAdapter(
    private val filterList: List<HouseFilter>,
    private val onItemClick: (HouseFilter) -> Unit
): RecyclerView.Adapter<HouseFilterAdapter.HouseViewHolder>() {
    private var selectedItemPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.layout_list_house_filters, parent, false)
        return HouseViewHolder(view)
    }

    override fun onBindViewHolder(holder: HouseViewHolder, position: Int) {
        val houseName = filterList[position]
        val isSelected = position == this.selectedItemPosition
        holder.bind(
            houseName = houseName.label,
            isSelected = isSelected,
            onItemClick = {
                val clickedPosition = holder.bindingAdapterPosition
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    val previousPosition = this.selectedItemPosition
                    this.selectedItemPosition = clickedPosition

                    notifyItemChanged(previousPosition)
                    notifyItemChanged(selectedItemPosition)

                    onItemClick(filterList[clickedPosition])
                }
            }
        )
    }

    override fun getItemCount(): Int = filterList.size

       class HouseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val filterItem: TextView = itemView.findViewById(R.id.filter_item)

        fun bind(houseName: String, isSelected: Boolean, onItemClick: () -> Unit) {
            filterItem.text = houseName
            filterItem.isSelected = isSelected
            filterItem.typeface = if (isSelected) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
            itemView.setOnClickListener {
                onItemClick()
            }
        }
    }
}