package com.kotlin.favoritemovies.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.favoritemovies.databinding.AdapterMovieBinding
import com.kotlin.favoritemovies.model.movie.Movie

class MovieAdapter(
    var movies: MutableList<Movie>,
    private val onCheckboxClickListener: OnCheckboxClickListener
) : RecyclerView.Adapter<MovieAdapter.MovieHolder>() {

    interface OnCheckboxClickListener {
        fun onCheckboxClick(movie: Movie, isChecked: Boolean)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        val binding = AdapterMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return MovieHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val movie = movies[position]

        holder.binding.apply {
            txtMovieName.text = movie.movieName
            if(movie.rate == 0) {
                txtRate.text = " - "
            } else {
                txtRate.text = movie.rate.toString()
            }
            txtPlatformToWatch.text = movie.platformToWatch
            checkboxWatched.isChecked = movie.watched != 0
            checkboxWatched.setOnCheckedChangeListener { _, isChecked ->
                holder.isCheckBoxClicked = true
                onCheckboxClickListener.onCheckboxClick(movie, isChecked)
            }
        }
    }

    override fun getItemCount() = movies.size

    inner class MovieHolder(var binding: AdapterMovieBinding): RecyclerView.ViewHolder(binding.root) {
        var isCheckBoxClicked: Boolean = false
    }
}
