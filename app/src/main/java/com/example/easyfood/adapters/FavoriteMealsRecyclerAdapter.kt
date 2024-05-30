package com.example.easyfood.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.data.dataclasses.MealDB
import com.example.easyfood.databinding.FavMealCardBinding

class FavoriteMealsRecyclerAdapter :
    RecyclerView.Adapter<FavoriteMealsRecyclerAdapter.FavoriteViewHolder>() {
    private var favoriteMeals: List<MealDB> = ArrayList()
    private lateinit var onFavoriteClickListener: OnFavoriteClickListener
    private lateinit var onFavoriteLongClickListener: OnFavoriteLongClickListener

    // Установка списка избранных блюд
    fun setFavoriteMealsList(favoriteMeals: List<MealDB>) {
        this.favoriteMeals = favoriteMeals
        notifyDataSetChanged()
    }

    // Получение блюда по позиции
    fun getMelaByPosition(position: Int): MealDB {
        return favoriteMeals[position]
    }

    // Установка слушателя для клика на избранное блюдо
    fun setOnFavoriteMealClickListener(onFavoriteClickListener: OnFavoriteClickListener) {
        this.onFavoriteClickListener = onFavoriteClickListener
    }

    // Установка слушателя для длинного нажатия на избранное блюдо
    fun setOnFavoriteLongClickListener(onFavoriteLongClickListener: OnFavoriteLongClickListener) {
        this.onFavoriteLongClickListener = onFavoriteLongClickListener
    }

    // ViewHolder для элементов списка
    class FavoriteViewHolder(val binding: FavMealCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(FavMealCardBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val i = position
        holder.binding.apply {
            // Установка названия и изображения блюда
            tvFavMealName.text = favoriteMeals[position].mealName
            Glide.with(holder.itemView)
                .load(favoriteMeals[position].mealThumb)
                .error(R.drawable.mealtest)
                .into(imgFavMeal)
        }

        // Обработка клика на избранное блюдо
        holder.itemView.setOnClickListener {
            onFavoriteClickListener.onFavoriteClick(favoriteMeals[position])
        }

        // Обработка длинного нажатия на избранное блюдо
        holder.itemView.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(p0: View?): Boolean {
                onFavoriteLongClickListener.onFavoriteLongCLick(favoriteMeals[i])
                return true
            }
        })
    }

    override fun getItemCount(): Int {
        return favoriteMeals.size
    }

    // Интерфейс для обработки клика на избранное блюдо
    interface OnFavoriteClickListener {
        fun onFavoriteClick(meal: MealDB)
    }

    // Интерфейс для обработки длинного нажатия на избранное блюдо
    interface OnFavoriteLongClickListener {
        fun onFavoriteLongCLick(meal: MealDB)
    }
}
