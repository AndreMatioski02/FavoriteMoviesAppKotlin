package com.kotlin.favoritemovies.model.movie

import android.content.Context
import android.util.Log

object MovieDataStore {

    val movies: MutableList<Movie> = arrayListOf()
    private var database: MovieDatabase? = null

    fun setContext(context: Context, categoryId: Long) {
        database = MovieDatabase(context)
        database?.let { db ->
            movies.clear()
            movies.addAll(db.getMoviesByCategoryId(categoryId))
        }
    }

    fun getMovie(position: Int): Movie {
        return movies[position]
    }

    fun addMovie(movie: Movie) {
        val id = database?.addMovie(movie) ?: return

        if (id > 0) {
            movie.id = id
            movies.add(movie)
        } else {
            Log.d("FavoriteMoviesApp", "Operação falhou: inserção de dados")
        }
    }

    fun editMovie(position: Int, movie: Movie) {
        movie.id = getMovie(position).id
        val count = database?.editMovie(movie) ?: return

        if(count > 0) {
            movies[position] = movie
        } else {
            Log.d("FavoriteMoviesApp", "Operação falhou: edição de dados")
        }
    }

    fun removeMovie(position: Int) {
        val movie = getMovie(position)
        val count = database?.removeMovie(movie) ?: return

        if(count > 0) {
            movies.removeAt(position)
        } else {
            Log.d("FavoriteMoviesApp", "Operação falhou: remoção de dados")
        }
    }

    fun searchMovies(searchText: String, categoryId: Long): Boolean {
        val db = database ?: return false
        val movies = if(searchText.isEmpty()) {
            db.getMoviesByCategoryId(categoryId)
        } else {
            db.searchMoviesWithName(searchText, categoryId)
        }

        movies.clear()
        movies.addAll(movies)

        return true
    }
}