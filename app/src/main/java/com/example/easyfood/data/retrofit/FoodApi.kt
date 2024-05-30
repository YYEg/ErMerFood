package com.example.easyfood.data.retrofit

import com.example.easyfood.data.dataclasses.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// Интерфейс для работы с API для получения данных о еде
interface FoodApi {
    // Получение категорий блюд
    @GET("categories.php")
    fun getCategories(): Call<CategoryResponse>

    // Получение блюд по категории
    @GET("filter.php?")
    fun getMealsByCategory(@Query("i") category:String):Call<MealsResponse>

    // Получение случайного блюда
    @GET ("random.php")
    fun getRandomMeal():Call<RandomMealResponse>

    // Получение блюда по его идентификатору
    @GET("lookup.php?")
    fun getMealById(@Query("i") id:String):Call<RandomMealResponse>
}
