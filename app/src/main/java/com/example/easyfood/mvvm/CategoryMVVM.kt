package com.example.easyfood.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easyfood.data.dataclasses.CategoryResponse
import com.example.easyfood.data.dataclasses.Category
import com.example.easyfood.data.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// ViewModel для работы с категориями блюд
class CategoryMVVM : ViewModel() {
    private var categories: MutableLiveData<List<Category>> = MutableLiveData<List<Category>>()

    init {
        // Получение категорий при создании экземпляра
        getCategories()
    }

    // Получение категорий блюд
    private fun getCategories() {
        RetrofitInstance.foodApi.getCategories().enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                // Установка значений в LiveData после успешного ответа от сервера
                categories.value = response.body()!!.categories
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                // Обработка ошибки при получении категорий
                Log.d(TAG, t.message.toString())
            }
        })
    }

    // Функция для наблюдения за категориями
    fun observeCategories(): LiveData<List<Category>> {
        return categories
    }
}
