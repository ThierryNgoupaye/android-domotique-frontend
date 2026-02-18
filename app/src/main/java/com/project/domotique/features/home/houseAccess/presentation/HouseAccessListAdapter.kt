package com.project.domotique.features.home.houseAccess.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.project.domotique.R
import com.project.domotique.features.auth.domain.entities.UserEntity
import com.project.domotique.shared.ConfirmPopupDialog

class HouseAccessListAdapter(
    private val context: Context,
    private val houseAccessList: List<UserEntity>,
    private val onConfirm: (selectedUser:  String) -> Unit
) : RecyclerView.Adapter<HouseAccessListAdapter.HouseAccessViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseAccessViewHolder {
        val view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.layout_house_access_list, parent, false)
        return HouseAccessViewHolder(view)
    }


    override fun onBindViewHolder(holder:HouseAccessViewHolder, position: Int) {
        val user = this.houseAccessList[position]
        holder.bind(user) {
            this.onConfirm(user.userLogin)
        }
    }



    override fun getItemCount(): Int {
        return this.houseAccessList.size
    }


    class HouseAccessViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private val userLogin: TextView = itemView.findViewById(R.id.user_house_access_login)
        private var currentDialog: ConfirmPopupDialog? = null
        private val revokeUserAccesImageBtn : ImageButton = itemView.findViewById(R.id.remove_access_btn)


        fun bind(user: UserEntity, onConfirm: () -> Unit)
        {
            this.userLogin.text = user.userLogin
            this.revokeUserAccesImageBtn.setOnClickListener {
                this.currentDialog?.dismiss()
                this.currentDialog = ConfirmPopupDialog(
                    context = itemView.context,
                    title = "Retirer les accès",
                    description = "Êtes vous sur de vouloir retirer les accès de votre maison à ${user.userLogin} ?",
                    onConfirm = onConfirm
                )
                this.currentDialog?.show()
            }
        }
    }
}