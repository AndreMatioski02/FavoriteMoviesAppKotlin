package com.kotlin.favoritemovies.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kotlin.favoritemovies.databinding.AdapterCategoryBinding
import com.kotlin.favoritemovies.model.category.Category

class CategoryAdapter(var categories: MutableList<Category>) : RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {

        AdapterCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false).apply {
            return CategoryHolder(this)
        }
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {

        categories[position].apply {
            "${this.categoryName} (${this.moviesAdded.toString()})".also { holder.binding.txtName.text = it }
        }
    }

    override fun getItemCount() = categories.size

    inner class CategoryHolder(var binding: AdapterCategoryBinding): RecyclerView.ViewHolder(binding.root)
}