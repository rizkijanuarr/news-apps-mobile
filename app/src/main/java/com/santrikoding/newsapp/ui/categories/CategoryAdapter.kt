package com.santrikoding.newsapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.santrikoding.newsapp.data.remote.model.Category
import com.santrikoding.newsapp.databinding.ItemCategoryBinding

class CategoryAdapter(private val onClick: (category: Category) -> Unit) :
    PagingDataAdapter<Category, CategoryAdapter.CategoryViewHolder>(diffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder =
        CategoryViewHolder(
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class CategoryViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Category?) {
            item?.let { category ->
                binding.itemCategory.text = category.name
                itemView.setOnClickListener {
                    onClick(item)
                }
            }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(
                oldItem: Category,
                newItem: Category
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: Category,
                newItem: Category
            ): Boolean =
                oldItem == newItem

        }
    }

}