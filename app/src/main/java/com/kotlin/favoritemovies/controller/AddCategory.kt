package com.kotlin.favoritemovies.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.kotlin.favoritemovies.databinding.ActivityAddCategoryBinding
import com.kotlin.favoritemovies.model.Category
import com.kotlin.favoritemovies.model.DataStore

class AddCategory : AppCompatActivity() {

    private lateinit var binding: ActivityAddCategoryBinding
    private var position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.getIntExtra("position", -1).apply {
            position = this
            if (position != -1)
                setData(position)
        }

        binding.btnSave.setOnClickListener {

            getData()?.let { category ->
                saveCategory(category)
            } ?: run {
                showMessage("Campos inválidos!!!")
            }
        }
        binding.btnCancel.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    override fun onPause() {
        super.onPause()

        Log.d("CitiesApp!", "Activity Pausada")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("CitiesApp!", "Activity destruída")
    }

    private fun getData(): Category? {

        val categoryName = binding.txtName.text.toString()

        if (categoryName.isEmpty())
            return null

        return Category(categoryName, 0)
    }

    private fun setData(position: Int) {

        DataStore.getCategory(position).run {
            binding.txtName.setText(this.categoryName)
        }
    }

    private fun saveCategory(category: Category) {

        if (position == -1)
            DataStore.addCategory(category)
        else
            DataStore.editCategory(position, category)
        Intent().run {
            putExtra("category", category.categoryName)
            setResult(RESULT_OK, this)
        }
        finish()
    }

    fun showMessage(message: String) {

        AlertDialog.Builder(this).run {
            title = "FavoriteMoviesApp"
            setMessage(message)
            setPositiveButton("Ok", null)
            show()
        }
    }
}
