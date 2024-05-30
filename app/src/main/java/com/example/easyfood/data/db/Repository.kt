package com.example.easyfood.data.db

import androidx.lifecycle.LiveData
import com.example.easyfood.data.dataclasses.MealDB

// Репозиторий для взаимодействия с базой данных
class Repository(private val mealDao: Dao) {
    // LiveData для списка сохраненных блюд
    val mealList: LiveData<List<MealDB>> = mealDao.getAllSavedMeals()

    // Вставка любимого блюда в базу данных
    suspend fun insertFavoriteMeal(meal: MealDB) {
        mealDao.insertFavorite(meal)
    }

    // Получение блюда из базы данных по его идентификатору
    suspend fun getMealById(mealId: String): MealDB {
        return mealDao.getMealById(mealId)
    }

    // Удаление блюда из базы данных по его идентификатору
    suspend fun deleteMealById(mealId: String) {
        mealDao.deleteMealById(mealId)
    }

    // Удаление записи о блюде из базы данных
    suspend fun deleteMeal(meal: MealDB) = mealDao.deleteMeal(meal)
}
