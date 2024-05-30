package com.example.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.data.dataclasses.Category
import com.example.easyfood.databinding.CategoryCardBinding

class CategoriesRecyclerAdapter : RecyclerView.Adapter<CategoriesRecyclerAdapter.CategoryViewHolder>() {
    private var categoryList: List<Category> = ArrayList()
    private lateinit var onItemClick: OnItemCategoryClicked
    private lateinit var onLongCategoryClick: OnLongCategoryClick

    // Установка списка категорий
    fun setCategoryList(categoryList: List<Category>) {
        this.categoryList = categoryList
        notifyDataSetChanged()
    }

    // Установка слушателя для длинного нажатия на категорию
    fun setOnLongCategoryClick(onLongCategoryClick: OnLongCategoryClick) {
        this.onLongCategoryClick = onLongCategoryClick
    }

    // Установка слушателя для клика на категорию
    fun onItemClicked(onItemClick: OnItemCategoryClicked) {
        this.onItemClick = onItemClick
    }

    // ViewHolder для элементов списка
    class CategoryViewHolder(val binding: CategoryCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(CategoryCardBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.apply {
            // Установка названия категории и изображения
            tvCategoryName.text = categoryList[position].strCategory
            Glide.with(holder.itemView)
                .load(categoryList[position].strCategoryThumb)
                .into(imgCategory)
        }

        // Обработка клика на категорию
        holder.itemView.setOnClickListener {
            onItemClick.onClickListener(categoryList[position])
        }

        // Обработка длинного нажатия на категорию
        holder.itemView.setOnLongClickListener {
            onLongCategoryClick.onCategoryLongCLick(categoryList[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    // Интерфейс для обработки клика на категорию
    interface OnItemCategoryClicked {
        fun onClickListener(category: Category)
    }

    // Интерфейс для обработки длинного нажатия на категорию
    interface OnLongCategoryClick {
        fun onCategoryLongCLick(category: Category)
    }
}
