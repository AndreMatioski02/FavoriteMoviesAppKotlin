package com.kotlin.favoritemovies.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.google.android.material.snackbar.Snackbar
import com.kotlin.favoritemovies.databinding.ActivityMovieCategoryBinding
import com.kotlin.favoritemovies.model.category.CategoryDataStore
import com.kotlin.favoritemovies.view.CategoryAdapter

class MovieCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieCategoryBinding
    private lateinit var adapter: CategoryAdapter
    private lateinit var gesture: GestureDetector

    private val addCategoryForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.let { intent ->
                Snackbar.make(
                    this,
                    binding.layout,
                    "Categoria ${intent.getStringExtra("category")} adicionada com sucesso!!!",
                    Snackbar.LENGTH_LONG
                ).show()
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMovieCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CategoryDataStore.setContext(this)
        loadRecycleView()
        configureFab()
        configureGesture()
        configureRecycleViewEvents()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        AlertDialog.Builder(this).run {
            setMessage("Tem certeza que deseja fechar esta tela??")
            setPositiveButton(getString(android.R.string.ok)) { _,_ ->
                finish()
            }
            setNegativeButton(getString(android.R.string.cancel), null)
            show()
        }
    }

    private fun loadRecycleView() {

        LinearLayoutManager(this).apply {
            this.orientation = LinearLayoutManager.VERTICAL
            binding.rcvCategories.layoutManager = this
            adapter = CategoryAdapter(CategoryDataStore.categories).apply {
                binding.rcvCategories.adapter = this
            }
        }
    }

    private fun configureGesture() {

        gesture = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                binding.rcvCategories.findChildViewUnder(e.x, e.y).run {
                    this?.let { child ->
                        binding.rcvCategories.getChildAdapterPosition(child).apply {
                            val intent = Intent(this@MovieCategoryActivity, MovieListActivity::class.java).run {
                                putExtra("position", this@apply)
                            }
                            startActivity(intent)
                        }
                    }
                }
                return super.onSingleTapConfirmed(e)
            }

            override fun onLongPress(e: MotionEvent) {
                super.onLongPress(e)

                binding.rcvCategories.findChildViewUnder(e.x, e.y).run {
                    this?.let { child ->
                        binding.rcvCategories.getChildAdapterPosition(child).apply {
                            val category = CategoryDataStore.getCategory(this)
                            AlertDialog.Builder(this@MovieCategoryActivity).run {
                                setMessage("Tem certeza que deseja remover esta categoria?")
                                setPositiveButton("Excluir") { _,_ ->
                                    CategoryDataStore.removeCategory(this@apply)
                                    Toast.makeText(this@MovieCategoryActivity, "Categoria ${category.categoryName} removida com sucesso!!!", Toast.LENGTH_LONG).show()
                                    adapter.notifyDataSetChanged()
                                }
                                setNegativeButton("Cancelar", null)
                                show()
                            }
                        }
                    }
                }
            }
        })
    }

    private fun configureRecycleViewEvents() {

        binding.rcvCategories.addOnItemTouchListener(object: OnItemTouchListener {

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                rv.findChildViewUnder(e.x, e.y).apply {
                    return (this != null && gesture.onTouchEvent(e))
                }
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })
    }

    private fun addCategory() {

        Intent(this, AddCategory::class.java).run {
            addCategoryForResult.launch(this)
        }
    }

    private fun configureFab() {
        binding.fab.setOnClickListener {
            addCategory()
        }
    }
}