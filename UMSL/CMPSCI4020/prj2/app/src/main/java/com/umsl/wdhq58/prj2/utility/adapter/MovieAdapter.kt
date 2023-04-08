package com.umsl.wdhq58.prj2.utility.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.umsl.wdhq58.prj2.R
import com.umsl.wdhq58.prj2.utility.api.data.PopularMovie

class MovieAdapter(val movies: List<PopularMovie>) : RecyclerView.Adapter<MovieViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        return holder.bind(movies[position])
    }

}

class MovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    private val photo: ImageView = itemView.findViewById(R.id.ivMoviePhoto)
    private val title: TextView = itemView.findViewById(R.id.tvMovieTitle)
    private val overview: TextView = itemView.findViewById(R.id.tvMovieOverview)
    private val rating: TextView = itemView.findViewById(R.id.tvMovieRating)

    fun bind(movie: PopularMovie){
        title.text = movie.title
        overview.text = movie.overview;
        rating.text = movie.vote_average.toString()
    }
}