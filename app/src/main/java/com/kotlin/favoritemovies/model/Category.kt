package com.kotlin.favoritemovies.model

class Category(
    var categoryName: String,
    var moviesAdded: Int
) {
    constructor(): this("", 0)
    constructor(categoryName: String) : this(categoryName, 0)
}