package com.kotlin.favoritemovies.model

import android.content.Context
import android.util.Log

object DataStore {

    val categories: MutableList<Category> = arrayListOf()
    val movies: MutableList<Movie> = arrayListOf()
    private var database: Database? = null

    fun setContext(context: Context) {
        database = Database(context)
        database?.let { db ->
            categories.clear()
            categories.addAll(db.getAllCategories())
        }
    }
    init {
        movies.add(Movie("Avatar", 4, "Ação", "Netflix"))
        movies.add(Movie("Vingadores", 5, "Ação", "Amazon Prime Videos"))
        movies.add(Movie("o Projeto Adam", 3, "Ação", "Apple TV"))
    }

    fun getCategory(position: Int): Category {
        return categories[position]
    }

    fun addCategory(category: Category) {
        val id = database?.addCategory(category) ?: return

        if (id > 0) {
            category.id = id
            categories.add(category)
        } else {
            Log.d("FavoriteMoviesApp", "Operação falhou: inserção de dados")
        }
    }

    fun editCategory(position: Int, category: Category) {
        category.id = getCategory(position).id
        val count = database?.editCategory(category) ?: return

        if(count > 0) {
            categories[position] = category
        } else {
            Log.d("FavoriteMoviesApp", "Operação falhou: edição de dados")
        }
    }

    fun removeCategory(position: Int) {
        val category = getCategory(position)
        val count = database?.removeCategory(category) ?: return

        if(count > 0) {
            categories.removeAt(position)
        } else {
            Log.d("CitiesApp", "Operação falhou: remoção de dados")
        }
    }

    fun searchCategories(searchText: String): Boolean {
        val db = database ?: return false
        val cities = if(searchText.isEmpty()) {
            db.getAllCategories()
        } else {
            db.searchCategoriesWithName(searchText)
        }

        this.categories.clear()
        this.categories.addAll(cities)

        return true
    }
}