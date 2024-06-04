package com.kotlin.favoritemovies.controller

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kotlin.favoritemovies.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configureNavigateButton()
    }

    private fun navigateToMovieCategory() {
        val intent = Intent(this, MovieCategoryActivity::class.java)
        startActivity(intent)
    }

    private fun configureNavigateButton() {
        binding.welcomeButton.setOnClickListener {
            navigateToMovieCategory()
        }
    }
}
