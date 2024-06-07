package com.kotlin.favoritemovies.controller

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin.favoritemovies.databinding.ActivityMovieListBinding
import com.kotlin.favoritemovies.model.category.CategoryDataStore
import com.kotlin.favoritemovies.view.MovieAdapter

class MovieListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieListBinding
    private lateinit var adapter: MovieAdapter
    private var position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getIntExtra("position", -1).apply {
            position = this
            if (position != -1)
                setData(position)
        }

        configureNavigateButton()
//        loadRecycleView()
    }

//    private fun loadRecycleView() {
//
//        LinearLayoutManager(this).apply {
//            this.orientation = LinearLayoutManager.VERTICAL
//            binding.rcvMovies.layoutManager = this
//            adapter = MovieAdapter(CategoryDataStore.movies).apply {
//                binding.rcvMovies.adapter = this
//            }
//        }
//    }

    private fun setData(position: Int) {

        CategoryDataStore.getCategory(position).run {
            val text = "${ this.categoryName } - ${ this.id }"
            binding.movieCategory.setText(text)
        }
    }

    private fun navigateBack() {
        val intent = Intent(this, MovieCategoryActivity::class.java)
        startActivity(intent)
    }

    private fun configureNavigateButton() {
        binding.btnListMovieBack.setOnClickListener {
            navigateBack()
        }
    }
}