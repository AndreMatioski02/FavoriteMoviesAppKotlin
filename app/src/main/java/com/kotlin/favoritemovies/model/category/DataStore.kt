package com.kotlin.favoritemovies.model.category

import android.content.Context
import android.util.Log

object CategoryDataStore {

    val categories: MutableList<Category> = arrayListOf()
    private var database: CategoryDatabase? = null

    fun setContext(context: Context) {
        database = CategoryDatabase(context)
        database?.let { db ->
            categories.clear()
            categories.addAll(db.getAllCategories())
        }
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

        categories.clear()
        categories.addAll(cities)

        return true
    }
}