package com.project.domotique.animations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridViewDecoration: RecyclerView.ItemDecoration() {


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
       outRect.apply {
           bottom = 20
           right = 20
       }
    }
}