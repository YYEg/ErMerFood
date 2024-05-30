package com.example.easyfood.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.data.dataclasses.Meal
import com.example.easyfood.databinding.MostPopularCardBinding

class MostPopularRecyclerAdapter : RecyclerView.Adapter<MostPopularRecyclerAdapter.MostPopularMealViewHolder>() {
    private var mealsList: List<Meal> = ArrayList()
    private lateinit var onItemClick: OnItemClick
    private lateinit var onLongItemClick: OnLongItemClick

    // Установка списка наиболее популярных блюд
    fun setMealList(mealsList: List<Meal>) {
        this.mealsList = mealsList
        notifyDataSetChanged()
    }

    // Установка слушателя для клика на блюдо
    fun setOnClickListener(onItemClick: OnItemClick) {
        this.onItemClick = onItemClick
    }

    // Установка слушателя для длинного нажатия на блюдо
    fun setOnLongCLickListener(onLongItemClick: OnLongItemClick) {
        this.onLongItemClick = onLongItemClick
    }

    // ViewHolder для элементов списка
    class MostPopularMealViewHolder(val binding: MostPopularCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MostPopularMealViewHolder {
        return MostPopularMealViewHolder(MostPopularCardBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MostPopularMealViewHolder, position: Int) {
        val i = position
        holder.binding.apply {
            // Загрузка изображения блюда
            Glide.with(holder.itemView)
                .load(mealsList[position].strMealThumb)
                .into(imgPopularMeal)
        }

        // Обработка клика на блюдо
        holder.itemView.setOnClickListener {
            onItemClick.onItemClick(mealsList[position])
        }

        // Обработка длинного нажатия на блюдо
        holder.itemView.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(p0: View?): Boolean {
                onLongItemClick.onItemLongClick(mealsList[i])
                return true
            }

        })
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }
}

// Интерфейс для обработки клика на блюдо
interface OnItemClick {
    fun onItemClick(meal: Meal)
}

// Интерфейс для обработки длинного нажатия на блюдо
interface OnLongItemClick {
    fun onItemLongClick(meal: Meal)
}