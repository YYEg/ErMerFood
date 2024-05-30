package com.example.easyfood.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.easyfood.data.dataclasses.MealDB

// Определение базы данных Room
@Database(entities = [MealDB::class], version = 6)
abstract class MealsDatabase : RoomDatabase() {
    // Абстрактная функция для получения объекта доступа к данным (DAO)
    abstract fun dao(): Dao

    companion object {
        // Переменная для хранения единственного экземпляра базы данных
        @Volatile
        private var INSTANCE: MealsDatabase? = null

        // Функция для получения единственного экземпляра базы данных
        @Synchronized
        fun getInstance(context: Context): MealsDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                // Создание нового экземпляра базы данных, если он еще не был создан
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MealsDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
