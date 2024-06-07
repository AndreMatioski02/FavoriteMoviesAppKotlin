package com.kotlin.favoritemovies.model.category

class Category(
    var categoryName: String,
    var moviesAdded: Int
) {
    var id: Long = -1
    constructor(): this("", 0)
    constructor(categoryName: String) : this(categoryName, 0)
    constructor(categoryName: String, moviesAdded: Int, id: Long) : this(categoryName, moviesAdded) {
        this.id = id
    }
}