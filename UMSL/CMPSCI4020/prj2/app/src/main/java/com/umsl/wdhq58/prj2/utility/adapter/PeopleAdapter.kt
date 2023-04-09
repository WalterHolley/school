package com.umsl.wdhq58.prj2.utility.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
    private val position: TextView = itemView.findViewById(R.id.tvPosition)
    private val rating: TextView = itemView.findViewById(R.id.tvPersonPopularity)

    fun bind(person: PopularPerson){
        val imageUrl = "https://image.tmdb.org/t/p/w500" + person.profile_path
        profileName.text = person.name
        position.text = person.known_for_department
        rating.text = person.popularity.toString()
        Glide.with(itemView.context).load(imageUrl).into(photo)
    }

}