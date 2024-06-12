package com.kotlin.favoritemovies.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.favoritemovies.databinding.AdapterMovieBinding
import com.kotlin.favoritemovies.model.movie.Movie

class MovieAdapter(var movies: MutableList<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {

        AdapterMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false).apply {
            return MovieHolder(this)
        }
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {

        movies[position].apply {
            holder.binding.txtMovieName.text = this.movieName
            holder.binding.txtRate.text = this.rate.toString()
            holder.binding.txtPlatformToWatch.text = this.platformToWatch
            holder.binding.checkboxWatched.isChecked = this.watched != 0
        }
    }

    override fun getItemCount() = movies.size

    inner class MovieHolder(var binding: AdapterMovieBinding): RecyclerView.ViewHolder(binding.root)
}