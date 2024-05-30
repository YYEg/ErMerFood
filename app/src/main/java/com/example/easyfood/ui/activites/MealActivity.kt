package com.example.easyfood.ui.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.R
import com.example.easyfood.adapters.MealRecyclerAdapter
import com.example.easyfood.adapters.SetOnMealClickListener
import com.example.easyfood.data.dataclasses.Meal
import com.example.easyfood.databinding.ActivityCategoriesBinding
import com.example.easyfood.mvvm.MealActivityMVVM
import com.example.easyfood.ui.fragments.HomeFragment.Companion.CATEGORY_NAME
import com.example.easyfood.ui.fragments.HomeFragment.Companion.MEAL_ID
import com.example.easyfood.ui.fragments.HomeFragment.Companion.MEAL_STR
import com.example.easyfood.ui.fragments.HomeFragment.Companion.MEAL_THUMB

// Активность для отображения блюд по выбранной категории
class MealActivity : AppCompatActivity() {
    private lateinit var mealActivityMvvm: MealActivityMVVM
    private lateinit var binding: ActivityCategoriesBinding
    private lateinit var myAdapter: MealRecyclerAdapter
    private var categoryNme = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Инициализация ViewModel для активности
        mealActivityMvvm = ViewModelProviders.of(this)[MealActivityMVVM::class.java]
        startLoading()
        prepareRecyclerView()

        // Получение блюд по выбранной категории и наблюдение за ними
        mealActivityMvvm.getMealsByCategory(getCategory())
        mealActivityMvvm.observeMeal().observe(this, Observer<List<Meal>> { t ->
            if (t == null) {
                hideLoading()
                Toast.makeText(applicationContext, "Нет блюд по этой категории", Toast.LENGTH_SHORT).show()
                onBackPressed()
            } else {
                myAdapter.setMealList(t)
                binding.tvCategoryCount.text = categoryNme + " : " + t.size.toString()
                hideLoading()
            }
        })

        // Установка слушателя кликов на элементы списка блюд
        myAdapter.setOnMealClickListener(object : SetOnMealClickListener {
            override fun setOnClickListener(meal: Meal) {
                val intent = Intent(applicationContext, MealDetailesActivity::class.java)
                intent.putExtra(MEAL_ID, meal.idMeal)
                intent.putExtra(MEAL_STR, meal.strMeal)
                intent.putExtra(MEAL_THUMB, meal.strMealThumb)
                startActivity(intent)
            }
        })
    }

    // Функция для скрытия индикатора загрузки
    private fun hideLoading() {
        binding.apply {
            loadingGifMeals.visibility = View.INVISIBLE
            mealRoot.setBackgroundColor(ContextCompat.getColor(applicationContext,R.color.white))
        }
    }

    // Функция для отображения индикатора загрузки
    private fun startLoading() {
        binding.apply {
            loadingGifMeals.visibility = View.VISIBLE
            mealRoot.setBackgroundColor(ContextCompat.getColor(applicationContext,R.color.g_loading))
        }
    }

    // Функция для получения выбранной категории из интента
    private fun getCategory(): String {
        val tempIntent = intent
        val x = intent.getStringExtra(CATEGORY_NAME)!!
        categoryNme = x
        return x
    }

    // Функция для настройки RecyclerView
    private fun prepareRecyclerView() {
        myAdapter = MealRecyclerAdapter()
        binding.mealRecyclerview.apply {
            adapter = myAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
    }
}
