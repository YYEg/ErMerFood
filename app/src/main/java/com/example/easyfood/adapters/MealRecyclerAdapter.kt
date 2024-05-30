package com.example.easyfood.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.data.dataclasses.Meal
import com.example.easyfood.databinding.MealCardBinding

class MealRecyclerAdapter : RecyclerView.Adapter<MealRecyclerAdapter.MealViewHolder>() {

    private var mealList: List<Meal> = ArrayList()
    private lateinit var setOnMealClickListener: SetOnMealClickListener

    // Установка списка блюд
    fun setMealList(mealList: List<Meal>) {
        this.mealList = mealList
        notifyDataSetChanged()
    }

    // Установка слушателя для клика на блюдо
    fun setOnMealClickListener(setOnMealClickListener: SetOnMealClickListener) {
        this.setOnMealClickListener = setOnMealClickListener
    }

    // ViewHolder для элементов списка
    class MealViewHolder(val binding: MealCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        return MealViewHolder(MealCardBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.binding.apply {
            // Установка названия и изображения блюда
            tvMealName.text = mealList[position].strMeal
            Glide.with(holder.itemView)
                .load(mealList[position].strMealThumb)
                .into(imgMeal)
        }

        // Обработка клика на блюдо
        holder.itemView.setOnClickListener {
            setOnMealClickListener.setOnClickListener(mealList[position])
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }
}

// Интерфейс для обработки клика на блюдо
interface SetOnMealClickListener {
    fun setOnClickListener(meal: Meal)
}
