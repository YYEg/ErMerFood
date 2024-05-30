package com.example.easyfood.mvvm

import android.util.Log
import androidx.lifecycle.*
import com.example.easyfood.data.dataclasses.*
import com.example.easyfood.data.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// ViewModel для главного фрагмента
const val TAG = "MainMVVM"

class MainFragMVVM: ViewModel() {
    // LiveData для списка категорий блюд
    private val mutableCategory = MutableLiveData<CategoryResponse>()
    // LiveData для случайного блюда
    private val mutableRandomMeal = MutableLiveData<RandomMealResponse>()
    // LiveData для списка блюд по категории
    private val mutableMealsByCategory = MutableLiveData<MealsResponse>()

    init {
        // Получение случайного блюда, списка категорий и списка блюд по категории при создании экземпляра
        getRandomMeal()
        getAllCategories()
        getMealsByCategory("beef")
    }

    // Получение всех категорий блюд
    private fun getAllCategories() {
        RetrofitInstance.foodApi.getCategories().enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                mutableCategory.value = response.body()
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Log.d(TAG, t.message.toString())
            }
        })
    }

    // Получение случайного блюда
    private fun getRandomMeal() {
        RetrofitInstance.foodApi.getRandomMeal().enqueue(object : Callback<RandomMealResponse> {
            override fun onResponse(call: Call<RandomMealResponse>, response: Response<RandomMealResponse>) {
                mutableRandomMeal.value = response.body()
            }

            override fun onFailure(call: Call<RandomMealResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }
        })
    }

    // Получение списка блюд по категории
    private fun getMealsByCategory(category:String) {
        RetrofitInstance.foodApi.getMealsByCategory(category).enqueue(object : Callback<MealsResponse> {
            override fun onResponse(call: Call<MealsResponse>, response: Response<MealsResponse>) {
                mutableMealsByCategory.value = response.body()
            }

            override fun onFailure(call: Call<MealsResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }
        })
    }

    // Функция для наблюдения за списком блюд по категории
    fun observeMealByCategory(): LiveData<MealsResponse> {
        return mutableMealsByCategory
    }

    // Функция для наблюдения за случайным блюдом
    fun observeRandomMeal(): LiveData<RandomMealResponse> {
        return mutableRandomMeal
    }

    // Функция для наблюдения за списком категорий блюд
    fun observeCategories(): LiveData<CategoryResponse> {
        return mutableCategory
    }
}
