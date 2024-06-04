package com.kotlin.favoritemovies.model

class Movie(
    var movieName: String,
    var rate: Int,
    var category: String,
    var platformToWatch: String
) {
    constructor(): this("", 0, "", "")
    constructor(movieName: String, category: String, platformToWatch: String) : this(movieName, 0, category, platformToWatch)
}