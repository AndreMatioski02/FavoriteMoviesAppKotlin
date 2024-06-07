package com.kotlin.favoritemovies.model.movie

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.kotlin.favoritemovies.model.category.CategoryDatabase

class MovieDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "favorite_movies.db"
        const val DATABASE_VERSION = 1

        const val DB_TABLE_MOVIE = "movie"
        const val DB_FIELD_ID = "id"
        const val DB_FIELD_MOVIE_NAME = "movieName"
        const val DB_FIELD_RATE = "rate"
        const val DB_FIELD_PLATFORM_TO_WATCH = "platformToWatch"
        const val DB_FIELD_CATEGORY_ID = "categoryId"

        val SQL_CREATE_MOVIES = "CREATE TABLE IF NOT EXISTS $DB_TABLE_MOVIE (" +
                "$DB_FIELD_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$DB_FIELD_MOVIE_NAME TEXT, " +
                "$DB_FIELD_RATE INTEGER, " +
                "$DB_FIELD_PLATFORM_TO_WATCH TEXT, " +
                "$DB_FIELD_CATEGORY_ID INTEGER, " +
                "FOREIGN KEY($DB_FIELD_CATEGORY_ID) REFERENCES ${CategoryDatabase.DB_TABLE_CATEGORY}(${CategoryDatabase.DB_FIELD_ID}));"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val database = db ?: return

        database.beginTransaction()

        try {
            database.execSQL(SQL_CREATE_MOVIES)
            database.setTransactionSuccessful()
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.d("FavoriteMovieApp", it) }
        } finally {
            database.endTransaction()
        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun getAllMovies(): MutableList<Movie> {
        val movies = mutableListOf<Movie>()
        val db = readableDatabase
        val cursor = db.query(
            DB_TABLE_MOVIE,
            null,
            null,
            null,
            null,
            null,
            DB_FIELD_MOVIE_NAME
        )
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(DB_FIELD_ID))
                val movieName = getString(getColumnIndexOrThrow(DB_FIELD_MOVIE_NAME))
                val rate = getInt(getColumnIndexOrThrow(DB_FIELD_RATE))
                val platformToWatch = getString(getColumnIndexOrThrow(DB_FIELD_PLATFORM_TO_WATCH))
                val categoryId = getLong(getColumnIndexOrThrow(DB_FIELD_CATEGORY_ID))

                val movie = Movie(movieName, rate, platformToWatch, categoryId, id)
                movies.add(movie)
            }
        }

        return movies
    }

    fun addMovie(movie: Movie): Long {
        var id: Long = 0
        val db = writableDatabase
        val values = ContentValues().apply {
            put(DB_FIELD_MOVIE_NAME, movie.movieName)
            put(DB_FIELD_RATE, movie.rate)
            put(DB_FIELD_PLATFORM_TO_WATCH, movie.platformToWatch)
            put(DB_FIELD_CATEGORY_ID, movie.categoryId)
        }

        db.beginTransaction()
        try {
            id = db.insert(DB_TABLE_MOVIE, null, values)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.d("FavoriteMovieApp", it) }
        } finally {
            db.endTransaction()
        }

        return id
    }

    fun editMovie(movie: Movie): Int {
        var count = 0
        val db = writableDatabase
        val values = ContentValues().apply {
            put(DB_FIELD_MOVIE_NAME, movie.movieName)
            put(DB_FIELD_RATE, movie.rate)
            put(DB_FIELD_PLATFORM_TO_WATCH, movie.platformToWatch)
            put(DB_FIELD_CATEGORY_ID, movie.categoryId)
        }

        val selection = "$DB_FIELD_ID = ?"
        val selectionArgs = arrayOf(movie.id.toString())

        db.beginTransaction()
        try {
            count = db.update(DB_TABLE_MOVIE, values, selection, selectionArgs)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.d("FavoriteMovieApp", it) }
        } finally {
            db.endTransaction()
        }

        return count
    }

    fun removeMovie(movie: Movie): Int {
        var count = 0
        val db = writableDatabase


        val selection = "$DB_FIELD_ID = ?"
        val selectionArgs = arrayOf(movie.id.toString())

        db.beginTransaction()
        try {
            count = db.delete(DB_TABLE_MOVIE, selection, selectionArgs)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.d("FavoriteMovieApp", it) }
        } finally {
            db.endTransaction()
        }

        return count
    }

    fun searchMoviesWithName(searchText: String): MutableList<Movie> {
        val movies = mutableListOf<Movie>()
        val db = readableDatabase
        val selection = "$DB_FIELD_MOVIE_NAME LIKE ?"
        val selectionArgs =  arrayOf("%$searchText%")

        val cursor = db.query(
            DB_TABLE_MOVIE,
            null,
            selection,
            selectionArgs,
            null,
            null,
            DB_FIELD_MOVIE_NAME
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(DB_FIELD_ID))
                val movieName = getString(getColumnIndexOrThrow(DB_FIELD_MOVIE_NAME))
                val rate = getInt(getColumnIndexOrThrow(DB_FIELD_RATE))
                val platformToWatch = getString(getColumnIndexOrThrow(DB_FIELD_PLATFORM_TO_WATCH))
                val categoryId = getLong(getColumnIndexOrThrow(DB_FIELD_CATEGORY_ID))

                val movie = Movie(movieName, rate, platformToWatch, categoryId, id)
                movies.add(movie)
            }
        }

        return movies
    }

}