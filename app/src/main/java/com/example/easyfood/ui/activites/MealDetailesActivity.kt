package com.example.easyfood.ui.activites

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.data.dataclasses.MealDB
import com.example.easyfood.data.dataclasses.MealDetail
import com.example.easyfood.databinding.ActivityMealDetailesBinding
import com.example.easyfood.mvvm.DetailsMVVM
import com.example.easyfood.ui.fragments.HomeFragment.Companion.MEAL_ID
import com.example.easyfood.ui.fragments.HomeFragment.Companion.MEAL_STR
import com.example.easyfood.ui.fragments.HomeFragment.Companion.MEAL_THUMB
import com.google.android.material.snackbar.Snackbar

// Активность для отображения деталей блюда
class MealDetailesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealDetailesBinding
    private lateinit var detailsMVVM: DetailsMVVM
    private var mealId = ""
    private var mealStr = ""
    private var mealThumb = ""
    private var ytUrl = ""
    private lateinit var dtMeal:MealDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailsMVVM = ViewModelProviders.of(this)[DetailsMVVM::class.java]
        binding = ActivityMealDetailesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading()

        getMealInfoFromIntent()
        setUpViewWithMealInformation()
        setFloatingButtonStatues()

        // Получение информации о блюде и наблюдение за ней
        detailsMVVM.getMealById(mealId)
        detailsMVVM.observeMealDetail().observe(this, Observer<List<MealDetail>> { t ->
            setTextsInViews(t!![0])
            stopLoading()
        })

        // Обработка клика по кнопке для перехода на страницу видео
        binding.imgYoutube.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(ytUrl)))
        }

        // Обработка клика по кнопке для сохранения/удаления блюда
        binding.btnSave.setOnClickListener {
            if(isMealSavedInDatabase()){
                deleteMeal()
                binding.btnSave.setImageResource(R.drawable.ic_baseline_save_24)
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Meal was deleted",
                    Snackbar.LENGTH_SHORT).show()
            }else{
                saveMeal()
                binding.btnSave.setImageResource(R.drawable.ic_saved)
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Meal saved",
                    Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    // Удаление блюда из базы данных
    private fun deleteMeal() {
        detailsMVVM.deleteMealById(mealId)
    }

    // Установка статуса плавающей кнопки в зависимости от того, сохранено ли блюдо
    private fun setFloatingButtonStatues() {
        if(isMealSavedInDatabase()){
            binding.btnSave.setImageResource(R.drawable.ic_saved)
        }else{
            binding.btnSave.setImageResource(R.drawable.ic_baseline_save_24)
        }
    }

    // Проверка, сохранено ли блюдо в базе данных
    private fun isMealSavedInDatabase(): Boolean {
        return detailsMVVM.isMealSavedInDatabase(mealId)
    }

    // Сохранение блюда в базу данных
    private fun saveMeal() {
        val meal = MealDB(dtMeal.idMeal.toInt(),
            dtMeal.strMeal,
            dtMeal.strArea,
            dtMeal.strCategory,
            dtMeal.strInstructions,
            dtMeal.strMealThumb,
            dtMeal.strYoutube)

        detailsMVVM.insertMeal(meal)
    }

    // Отображение индикатора загрузки
    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnSave.visibility = View.GONE
        binding.imgYoutube.visibility = View.INVISIBLE
    }

    // Скрытие индикатора загрузки
    private fun stopLoading() {
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnSave.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
    }

    // Установка текстов в представления с информацией о блюде
    private fun setTextsInViews(meal: MealDetail) {
        this.dtMeal = meal
        ytUrl = meal.strYoutube
        binding.apply {
            tvInstructions.text = "- Instructions : "
            tvContent.text = meal.strInstructions
            tvAreaInfo.visibility = View.VISIBLE
            tvCategoryInfo.visibility = View.VISIBLE
            tvAreaInfo.text = tvAreaInfo.text.toString() + meal.strArea
            tvCategoryInfo.text = tvCategoryInfo.text.toString() + meal.strCategory
            imgYoutube.visibility = View.VISIBLE
        }
    }

    // Установка информации о блюде в представления
    private fun setUpViewWithMealInformation() {
        binding.apply {
            collapsingToolbar.title = mealStr
            Glide.with(applicationContext)
                .load(mealThumb)
                .into(imgMealDetail)
        }
    }

    // Получение информации о блюде из интента
    private fun getMealInfoFromIntent() {
        val tempIntent = intent
        this.mealId = tempIntent.getStringExtra(MEAL_ID)!!
        this.mealStr = tempIntent.getStringExtra(MEAL_STR)!!
        this.mealThumb = tempIntent.getStringExtra(MEAL_THUMB)!!
    }
}
