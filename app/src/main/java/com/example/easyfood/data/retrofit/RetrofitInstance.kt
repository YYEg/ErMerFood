package com.example.easyfood.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Объект для создания экземпляра Retrofit и FoodApi
object RetrofitInstance {
    // Ленивая инициализация экземпляра FoodApi с помощью Retrofit
    val foodApi: FoodApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FoodApi::class.java)
    }
}