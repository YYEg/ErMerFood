package com.example.easyfood.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import com.example.easyfood.data.dataclasses.MealDB

// Определение интерфейса доступа к данным (DAO)
@Dao
interface Dao {
    // Вставка записи о любимом блюде в базу данных
    @Insert
    fun insertFavorite(meal: MealDB)

    // Обновление записи о любимом блюде в базе данных
    @Update
    fun updateFavorite(meal:MealDB)

    // Получение всех сохраненных блюд из базы данных с сортировкой по идентификатору
    @Query("SELECT * FROM meal_information order by mealId asc")
    fun getAllSavedMeals():LiveData<List<MealDB>>

    // Получение блюда из базы данных по его идентификатору
    @Query("SELECT * FROM meal_information WHERE mealId =:id")
    fun getMealById(id:String):MealDB

    // Удаление блюда из базы данных по его идентификатору
    @Query("DELETE FROM meal_information WHERE mealId =:id")
    fun deleteMealById(id:String)

    // Удаление записи о блюде из базы данных
    @Delete
    fun deleteMeal(meal:MealDB)
}
