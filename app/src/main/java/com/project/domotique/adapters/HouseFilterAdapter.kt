package com.project.domotique.adapters

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.domotique.R

class HouseFilterAdapter(
    private val filterList: List<String>,
    private val onItemClick: (String) -> Unit
): RecyclerView.Adapter<HouseFilterAdapter.HouseViewHolder>() {
    private var selectedItemPosition: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.list_view_house_filters, parent, false)
        return HouseViewHolder(view)
    }

    override fun onBindViewHolder(holder: HouseViewHolder, position: Int) {
        val houseName = filterList[position]
        val isSelected = position == this.selectedItemPosition
        holder.bind(
            houseName = houseName,
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

       class HouseViewHolder(view: View) : RecyclerView.ViewHolder(view) {

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