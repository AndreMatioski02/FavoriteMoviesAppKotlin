package com.kotlin.favoritemovies.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.kotlin.favoritemovies.databinding.ActivityAddMovieBinding
import com.kotlin.favoritemovies.model.movie.Movie
import com.kotlin.favoritemovies.model.movie.MovieDataStore

class AddMovie : AppCompatActivity() {

    private lateinit var binding: ActivityAddMovieBinding
    private var position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getIntExtra("position", -1).apply {
            position = this
            if (position != -1)
                setData(position)
        }

        binding.btnSaveMovie.setOnClickListener {

            getData(position)?.let { movie ->
                saveMovie(movie)
            } ?: run {
                showMessage("Campos inválidos!!!")
            }
        }
        binding.btnCancelMovieAdd.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    override fun onPause() {
        super.onPause()

        Log.d("FavoriteMovieApp!", "Activity Pausada")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("FavoriteMovieApp!", "Activity destruída")
    }

    private fun getData(position: Int): Movie? {
        var movieWatchedValue = 0
        var movieRate = "0"

        if(position != -1) {
            movieWatchedValue = MovieDataStore.getMovie(position).watched
        }
        val receivedCategoryIdParam = intent.getLongExtra("categoryId", -1)

        val movieName = binding.txtMovieName.text.toString()
        if(binding.txtRate.text.isNotEmpty()) {
            movieRate = binding.txtRate.text.toString()
        }
        val moviePlatform = binding.txtPlatform.text.toString()


        if (movieName.isEmpty() || moviePlatform.isEmpty())
            return null

        return Movie(movieName, movieRate.toInt(), moviePlatform,
            movieWatchedValue, receivedCategoryIdParam)
    }

    private fun setData(position: Int) {

        MovieDataStore.getMovie(position).run {
            binding.txtMovieName.setText(this.movieName)
            binding.txtRate.setText(this.rate.toString())
            binding.txtPlatform.setText(this.platformToWatch)
        }
    }

    private fun saveMovie(movie: Movie) {
        if (position == -1)
            MovieDataStore.addMovie(movie)
        else
            MovieDataStore.editMovie(position, movie)
        Intent().run {
            putExtra("movie", movie.movieName)
            putExtra("position", intent.getLongExtra("categoryPosition", -1))
            setResult(RESULT_OK, this)
        }
        finish()
    }

    fun showMessage(message: String) {

        AlertDialog.Builder(this).run {
            title = "FavoriteMoviesApp"
            setMessage(message)
            setPositiveButton("Ok", null)
            show()
        }
    }
}
