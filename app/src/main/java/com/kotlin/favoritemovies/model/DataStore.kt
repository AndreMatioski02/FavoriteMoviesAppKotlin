package com.kotlin.favoritemovies.model

object DataStore {

    val categories: MutableList<Category> = arrayListOf()
    val movies: MutableList<Movie> = arrayListOf()

    init {
        categories.add(Category("Ação", 2))
        categories.add(Category("Romance", 1))
        categories.add(Category("Comédia", 8))
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
        categories.add(category)
    }

    fun editCategory(position: Int, category: Category) {
        categories.set(position, category)
    }

    fun removeCategory(position: Int) {
        categories.removeAt(position)
    }
}