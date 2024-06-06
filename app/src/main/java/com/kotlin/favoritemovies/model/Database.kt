package com.kotlin.favoritemovies.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class Database(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "favorite_movies.db"
        const val DATABASE_VERSION = 1

        const val DB_TABLE_CATEGORY = "category"

        const val DB_FIELD_ID = "id"
        const val DB_FIELD_CATEGORY_NAME = "categoryName"
        const val DB_FIELD_MOVIES_ADDED= "moviesAdded"

        const val SQL_CREATE_CATEGORIES = "CREATE TABLE IF NOT EXISTS $DB_TABLE_CATEGORY (" +
                "$DB_FIELD_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$DB_FIELD_CATEGORY_NAME TEXT, " +
                "$DB_FIELD_MOVIES_ADDED INTEGER);"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val database = db ?: return

        database.beginTransaction()

        try {
            database.execSQL(SQL_CREATE_CATEGORIES)
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

    fun getAllCategories(): MutableList<Category> {
        val categories = mutableListOf<Category>()
        val db = readableDatabase
        val cursor = db.query(
            DB_TABLE_CATEGORY,
            null,
            null,
            null,
            null,
            null,
            DB_FIELD_CATEGORY_NAME
        )
        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(DB_FIELD_ID))
                val categoryName = getString(getColumnIndexOrThrow(DB_FIELD_CATEGORY_NAME))
                val moviesAdded = getInt(getColumnIndexOrThrow(DB_FIELD_MOVIES_ADDED))

                val category = Category(categoryName, moviesAdded, id)
                categories.add(category)
            }
        }

        return categories
    }

    fun addCategory(category: Category): Long {
        var id: Long = 0
        val db  =writableDatabase
        val values = ContentValues().apply {
            put(DB_FIELD_CATEGORY_NAME, category.categoryName)
            put(DB_FIELD_MOVIES_ADDED, category.moviesAdded)
        }

        db.beginTransaction()
        try {
            id = db.insert(DB_TABLE_CATEGORY, null, values)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.d("FavoriteMovieApp", it) }
        } finally {
            db.endTransaction()
        }

        return id
    }

    fun editCategory(category: Category): Int {
        var count = 0
        val db = writableDatabase
        val values = ContentValues().apply {
            put(DB_FIELD_CATEGORY_NAME, category.categoryName)
            put(DB_FIELD_MOVIES_ADDED, category.moviesAdded)
        }

        val selection = "$DB_FIELD_ID = ?"
        val selectionArgs = arrayOf(category.id.toString())

        db.beginTransaction()
        try {
            count = db.update(DB_TABLE_CATEGORY, values, selection, selectionArgs)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.d("FavoriteMovieApp", it) }
        } finally {
            db.endTransaction()
        }

        return count
    }

    fun removeCategory(category: Category): Int {
        var count = 0
        val db = writableDatabase


        val selection = "$DB_FIELD_ID = ?"
        val selectionArgs = arrayOf(category.id.toString())

        db.beginTransaction()
        try {
            count = db.delete(DB_TABLE_CATEGORY, selection, selectionArgs)
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.d("FavoriteMovieApp", it) }
        } finally {
            db.endTransaction()
        }

        return count
    }

    fun searchCategoriesWithName(searchText: String): MutableList<Category> {
        val categories = mutableListOf<Category>()
        val db = readableDatabase
        val selection = "$DB_FIELD_CATEGORY_NAME LIKE ?"
        val selectionArgs =  arrayOf("%$searchText%")

        val cursor = db.query(
            DB_TABLE_CATEGORY,
            null,
            selection,
            selectionArgs,
            null,
            null,
            DB_FIELD_CATEGORY_NAME
        )

        with(cursor) {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(DB_FIELD_ID))
                val name = getString(getColumnIndexOrThrow(DB_FIELD_CATEGORY_NAME))
                val people = getInt(getColumnIndexOrThrow(DB_FIELD_MOVIES_ADDED))

                val category = Category(name, people, id)
                categories.add(category)
            }
        }

        return categories
    }
}