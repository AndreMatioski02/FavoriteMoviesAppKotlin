package com.kotlin.favoritemovies.controller

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kotlin.favoritemovies.databinding.ActivityMovieListBinding
import com.kotlin.favoritemovies.model.category.CategoryDataStore
import com.kotlin.favoritemovies.model.movie.Movie
import com.kotlin.favoritemovies.model.movie.MovieDataStore
import com.kotlin.favoritemovies.view.MovieAdapter

class MovieListActivity : AppCompatActivity(), MovieAdapter.OnCheckboxClickListener {

    private lateinit var binding: ActivityMovieListBinding
    private lateinit var adapter: MovieAdapter
    private lateinit var gesture: GestureDetector
    private var position = -1

    private val addMovieForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let { intent ->
                Snackbar.make(
                    binding.layout,
                    "Filme ${intent.getStringExtra("movie")} adicionado com sucesso!!!",
                    Snackbar.LENGTH_LONG
                ).show()
                adapter.notifyDataSetChanged()
                setData(position)
            }
        }
    }

    private var editMovieForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let { intent ->
                Snackbar.make(
                    binding.layout,
                    "Filme ${intent.getStringExtra("movie")} alterado com sucesso!!!",
                    Snackbar.LENGTH_LONG
                ).show()
                adapter.notifyDataSetChanged()
                setData(position)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMovieListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        position = intent.getIntExtra("position", -1)

        val categoryId = CategoryDataStore.getCategory(position).id

        MovieDataStore.setContext(this, categoryId)

        if (position != -1) {
            setData(position)
        }

        loadRecycleView()
        configureNavigateButton()
        configureAddMovie()
        configureGesture()
        configureRecycleViewEvents()
    }

    private fun loadRecycleView() {
        LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.VERTICAL
            binding.rcvMovies.layoutManager = this
            adapter = MovieAdapter(MovieDataStore.movies, this@MovieListActivity).apply {
                binding.rcvMovies.adapter = this
            }
        }
    }

    override fun onCheckboxClick(movie: Movie, isChecked: Boolean) {
        movie.watched = if (isChecked) 1 else 0
        MovieDataStore.editWatchedMovie(movie)
        Toast.makeText(this, "Filme ${movie.movieName} atualizado!", Toast.LENGTH_SHORT).show()
    }

    private fun configureGesture() {
        gesture = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                binding.rcvMovies.findChildViewUnder(e.x, e.y)?.let { child ->
                    val position = binding.rcvMovies.getChildAdapterPosition(child)
                    val holder = binding.rcvMovies.findViewHolderForAdapterPosition(position) as? MovieAdapter.MovieHolder
                    if (holder != null && holder.isCheckBoxClicked) {
                        holder.isCheckBoxClicked = false
                        return true
                    }

                    Intent(this@MovieListActivity, AddMovie::class.java).apply {
                        val categoryId = CategoryDataStore.getCategory(this@MovieListActivity.position).id
                        putExtra("categoryId", categoryId)
                        putExtra("categoryPosition", this@MovieListActivity.position)
                        putExtra("position", position)
                        editMovieForResult.launch(this)
                    }
                }
                return super.onSingleTapConfirmed(e)
            }

            override fun onLongPress(e: MotionEvent) {
                super.onLongPress(e)
                binding.rcvMovies.findChildViewUnder(e.x, e.y)?.let { child ->
                    val rcvPosition = binding.rcvMovies.getChildAdapterPosition(child)
                    val movie = MovieDataStore.getMovie(rcvPosition)
                    AlertDialog.Builder(this@MovieListActivity).apply {
                        setMessage("Tem certeza que deseja remover este filme??")
                        setPositiveButton("Excluir") { _, _ ->
                            MovieDataStore.removeMovie(rcvPosition)
                            Toast.makeText(this@MovieListActivity, "Filme ${movie.movieName} removido com sucesso!!!", Toast.LENGTH_LONG).show()
                            adapter.notifyDataSetChanged()
                            setData(position)
                        }
                        setNegativeButton("Cancelar", null)
                        show()
                    }
                }
            }
        })
    }

    private fun configureRecycleViewEvents() {
        binding.rcvMovies.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                rv.findChildViewUnder(e.x, e.y)?.let {
                    return gesture.onTouchEvent(e)
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }

    private fun setData(position: Int) {
        CategoryDataStore.getCategory(position).run {
            val text = this.categoryName + " (" + MovieDataStore.movies.size + ")"
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
        val categoryId = CategoryDataStore.getCategory(position).id
        val intent = Intent(this, AddMovie::class.java).apply {
            putExtra("categoryId", categoryId)
            putExtra("categoryPosition", position)
        }
        addMovieForResult.launch(intent)
    }

    private fun configureAddMovie() {
        binding.addMovie.setOnClickListener {
            addMovie()
        }
    }
}
