package com.example.easyfood.data.dataclasses

// Для случайного блюда на главной
data class RandomMealResponse(
    val meals: List<MealDetail>
)