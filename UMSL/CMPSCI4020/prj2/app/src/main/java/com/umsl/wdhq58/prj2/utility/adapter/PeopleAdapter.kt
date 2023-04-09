package com.umsl.wdhq58.prj2.utility.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.umsl.wdhq58.prj2.R
import com.umsl.wdhq58.prj2.utility.api.data.PopularPerson

class PeopleAdapter(val persons: List<PopularPerson>) : RecyclerView.Adapter<PeopleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeopleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.people_item, parent, false)
        return PeopleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return persons.size
    }

    override fun onBindViewHolder(holder: PeopleViewHolder, position: Int) {
        return holder.bind(persons[position])
    }
}


class PeopleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    private val photo: ImageView = itemView.findViewById(R.id.ivProfilePhoto)
    private val profileName: TextView = itemView.findViewById(R.id.tvName)
    private val datOfBirth: TextView = itemView.findViewById(R.id.tvBirthday)
    private val rating: TextView = itemView.findViewById(R.id.tvPersonPopularity)

    fun bind(person: PopularPerson){
        profileName.text = person.name
        datOfBirth.text = person.birthday
        rating.text = person.popularity.toString()
    }

}