package com.umsl.wdhq58.prj1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.umsl.wdhq58.prj1.model.ContactModel

class ContactAdapter(private val contactList: ArrayList<ContactModel>): RecyclerView.Adapter<ViewHolder>() {

    private val data:ArrayList<ContactModel>

    init{
        data = contactList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.main_cardlayout,parent, false)
        view.setOnClickListener(RecyclerClickListener(parent.context,parent.findViewById<RecyclerView>(R.id.contentRecycler),contactList))
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.contactName.text = data[position].getFullName()
    }





}