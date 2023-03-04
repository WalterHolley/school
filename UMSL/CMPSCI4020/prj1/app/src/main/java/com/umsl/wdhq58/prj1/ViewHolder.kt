package com.umsl.wdhq58.prj1

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolder(viewItem: View): RecyclerView.ViewHolder(viewItem) {
    val contactName: TextView

    init{
        contactName = viewItem.findViewById(R.id.contactName)
    }
}