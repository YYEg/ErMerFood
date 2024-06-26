package com.example.easyfood.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easyfood.data.dataclasses.Meal
import com.example.easyfood.data.dataclasses.MealsResponse
import com.example.easyfood.data.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// ViewModel для активности с блюдами
class MealActivityMVVM():ViewModel() {
    // LiveData для списка блюд
    private var mutableMeal = MutableLiveData<List<Meal>>()

    // Функция для получения списка блюд по категории
    fun getMealsByCategory(category:String){
        RetrofitInstance.foodApi.getMealsByCategory(category).enqueue(object : Callback<MealsResponse>{
            override fun onResponse(call: Call<MealsResponse>, response: Response<MealsResponse>) {
                mutableMeal.value = response.body()!!.meals
            }

            override fun onFailure(call: Call<MealsResponse>, t: Throwable) {
                Log.d(TAG,t.message.toString())
            }
        })
    }

    // Функция для наблюдения за списком блюд
    fun observeMeal():LiveData<List<Meal>>{
        return mutableMeal
    }
}
