package com.example.easyfood.mvvm

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.easyfood.data.db.MealsDatabase
import com.example.easyfood.data.db.Repository
import com.example.easyfood.data.dataclasses.MealDB
import com.example.easyfood.data.dataclasses.MealDetail
import com.example.easyfood.data.dataclasses.RandomMealResponse
import com.example.easyfood.data.retrofit.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// ViewModel для работы с деталями блюд
class DetailsMVVM(application: Application) : AndroidViewModel(application) {
    // LiveData для деталей блюда
    private val mutableMealDetail = MutableLiveData<List<MealDetail>>()
    // LiveData для деталей блюда в нижнем листе
    private val mutableMealBottomSheet = MutableLiveData<List<MealDetail>>()
    // LiveData для списка всех сохраненных блюд
    private val allMeals: LiveData<List<MealDB>>
    // Репозиторий для доступа к данным в базе данных
    private val repository: Repository

    init {
        val mealDao = MealsDatabase.getInstance(application).dao()
        repository = Repository(mealDao)
        allMeals = repository.mealList
    }

    // Функция для получения всех сохраненных блюд
    fun getAllSavedMeals() {
        viewModelScope.launch(Dispatchers.Main) {
        }
    }

    // Функция для вставки блюда в базу данных
    fun insertMeal(meal: MealDB) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFavoriteMeal(meal)
            withContext(Dispatchers.Main) {
            }
        }
    }

    // Функция для удаления блюда из базы данных
    fun deleteMeal(meal:MealDB) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteMeal(meal)
    }

    // Функция для получения деталей блюда по его идентификатору
    fun getMealById(id: String) {
        RetrofitInstance.foodApi.getMealById(id).enqueue(object : Callback<RandomMealResponse> {
            override fun onResponse(call: Call<RandomMealResponse>, response: Response<RandomMealResponse>) {
                mutableMealDetail.value = response.body()!!.meals
            }

            override fun onFailure(call: Call<RandomMealResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }

    // Функция для проверки, сохранено ли блюдо в базе данных
    fun isMealSavedInDatabase(mealId: String): Boolean {
        var meal: MealDB? = null
        runBlocking(Dispatchers.IO) {
            meal = repository.getMealById(mealId)
        }
        if (meal == null)
            return false
        return true

    }

    // Функция для удаления блюда из базы данных по его идентификатору
    fun deleteMealById(mealId:String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMealById(mealId)
        }
    }
    // Функция для получения деталей блюда для нижнего листа
    fun getMealByIdBottomSheet(id: String) {
        RetrofitInstance.foodApi.getMealById(id).enqueue(object : Callback<RandomMealResponse> {
            override fun onResponse(call: Call<RandomMealResponse>, response: Response<RandomMealResponse>) {
                mutableMealBottomSheet.value = response.body()!!.meals
            }

            override fun onFailure(call: Call<RandomMealResponse>, t: Throwable) {
                Log.e(TAG, t.message.toString())
            }

        })
    }
    // Функция для наблюдения за деталями блюда
    fun observeMealDetail(): LiveData<List<MealDetail>> {
        return mutableMealDetail
    }

    // Функция для наблюдения за деталями блюда в нижнем листе
    fun observeMealBottomSheet(): LiveData<List<MealDetail>> {
        return mutableMealBottomSheet
    }

    // Функция для наблюдения за списком всех сохраненных блюд
    fun observeSaveMeal(): LiveData<List<MealDB>> {
        return allMeals
    }
}