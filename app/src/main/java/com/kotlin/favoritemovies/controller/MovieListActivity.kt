package com.kotlin.favoritemovies.controller

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.kotlin.favoritemovies.databinding.ActivityMovieListBinding
import com.kotlin.favoritemovies.model.category.CategoryDataStore
import com.kotlin.favoritemovies.model.movie.MovieDataStore
import com.kotlin.favoritemovies.view.MovieAdapter

class MovieListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieListBinding
    private lateinit var adapter: MovieAdapter
    private var position = -1

    private val addMovieForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let { intent ->
                Snackbar.make(
                    this,
                    binding.layout,
                    "Filme ${intent.getStringExtra("movie")} adicionado com sucesso!!!",
                    Snackbar.LENGTH_LONG
                ).show()
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = this

        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getIntExtra("position", -1).apply {
            position = this
            if (position != -1)
                setData(position)
                MovieDataStore.setContext(context, position.toLong())
        }

        loadRecycleView()
        configureNavigateButton()
        configureAddMovie()
    }`

    private fun loadRecycleView() {

        LinearLayoutManager(this).apply {
            this.orientation = LinearLayoutManager.VERTICAL
            binding.rcvMovies.layoutManager = this
            adapter = MovieAdapter(MovieDataStore.movies).apply {
                binding.rcvMovies.adapter = this
            }
        }
    }

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

    private fun addMovie() {
        Intent(this, AddMovie::class.java).run {
            addMovieForResult.launch(this)
        }
    }

    private fun configureAddMovie() {
        binding.addMovie.setOnClickListener {
            addMovie()
        }
    }
}