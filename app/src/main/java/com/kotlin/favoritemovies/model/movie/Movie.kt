package com.kotlin.favoritemovies.model.movie

class Movie(
    var movieName: String,
    var rate: Int,
    var platformToWatch: String,
    var watched: Boolean,
    var categoryId: Long
) {
    var id: Long = -1
    constructor() : this("", 0, "", false, 0)
    constructor(movieName: String, rate: Int, platformToWatch: String, watched: Boolean, categoryId: Long, id: Long)
            : this(movieName, rate, platformToWatch, watched, categoryId) {
        this.id = id
    }
}