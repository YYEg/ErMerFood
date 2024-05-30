package com.example.easyfood.data.dataclasses

import androidx.room.Entity
import androidx.room.PrimaryKey


// сущность базы данных для рецепта
@Entity(tableName = "meal_information")
data class MealDB(
    @PrimaryKey
    val mealId: Int,
    val mealName: String,
    val mealCountry: String,
    val mealCategory:String,
    val mealInstruction:String,
    val mealThumb:String,
    val mealYoutubeLink:String
)
