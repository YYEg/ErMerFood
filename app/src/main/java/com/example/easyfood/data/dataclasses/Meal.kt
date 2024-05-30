package com.example.easyfood.data.dataclasses

import androidx.room.Entity

// Класс данных для рецепта
@Entity(tableName = "favorites")
data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String
)