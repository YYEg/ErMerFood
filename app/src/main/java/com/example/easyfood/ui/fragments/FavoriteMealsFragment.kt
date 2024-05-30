package com.example.easyfood.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.easyfood.R
import com.example.easyfood.adapters.FavoriteMealsRecyclerAdapter
import com.example.easyfood.data.dataclasses.MealDB
import com.example.easyfood.data.dataclasses.MealDetail
import com.example.easyfood.databinding.FragmentFavoriteMealsBinding
import com.example.easyfood.mvvm.DetailsMVVM
import com.example.easyfood.ui.activites.MealDetailesActivity
import com.example.easyfood.ui.fragments.HomeFragment.Companion.CATEGORY_NAME
import com.example.easyfood.ui.fragments.HomeFragment.Companion.MEAL_AREA
import com.example.easyfood.ui.fragments.HomeFragment.Companion.MEAL_ID
import com.example.easyfood.ui.fragments.HomeFragment.Companion.MEAL_NAME
import com.example.easyfood.ui.fragments.HomeFragment.Companion.MEAL_STR
import com.example.easyfood.ui.fragments.HomeFragment.Companion.MEAL_THUMB
import com.google.android.material.snackbar.Snackbar

class FavoriteMeals : Fragment() {
    lateinit var recView:RecyclerView
    lateinit var fBinding:FragmentFavoriteMealsBinding
    private lateinit var myAdapter:FavoriteMealsRecyclerAdapter
    private lateinit var detailsMVVM: DetailsMVVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myAdapter = FavoriteMealsRecyclerAdapter() // Создаем адаптер для списка избранных блюд
        detailsMVVM = ViewModelProviders.of(this)[DetailsMVVM::class.java] // Инициализируем ViewModel для взаимодействия с данными
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fBinding = FragmentFavoriteMealsBinding.inflate(inflater,container,false) // Привязываем layout фрагмента
        return fBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView(view) // Подготавливаем RecyclerView
        observeData() // Наблюдаем за изменениями данных
        observeSwipeToDelete() // Наблюдаем за свайпами для удаления
    }

    private fun observeData() {
        detailsMVVM.observeSaveMeal().observe(viewLifecycleOwner,object : Observer<List<MealDB>>{ // Наблюдаем за списком избранных блюд
            override fun onChanged(t: List<MealDB>?) {
                myAdapter.setFavoriteMealsList(t!!) // Устанавливаем список избранных блюд в адаптер
                if(t.isEmpty())
                    fBinding.tvFavEmpty.visibility = View.VISIBLE // Показываем сообщение об отсутствии избранных блюд
                else
                    fBinding.tvFavEmpty.visibility = View.GONE // Прячем сообщение об отсутствии избранных блюд
            }
        })

        myAdapter.setOnFavoriteMealClickListener(object : FavoriteMealsRecyclerAdapter.OnFavoriteClickListener{
            override fun onFavoriteClick(meal: MealDB) {
                navigateToMealDetails(meal) // Переходим к деталям выбранного блюда
            }
        })

        myAdapter.setOnFavoriteLongClickListener(object : FavoriteMealsRecyclerAdapter.OnFavoriteLongClickListener{
            override fun onFavoriteLongCLick(meal: MealDB) {
                detailsMVVM.getMealByIdBottomSheet(meal.mealId.toString()) // Запрашиваем дополнительные данные о блюде
            }
        })
    }

    private fun prepareRecyclerView(view: View) {
        recView = view.findViewById<RecyclerView>(R.id.fav_rec_view) // Находим RecyclerView в макете
        recView.adapter = myAdapter // Устанавливаем адаптер
        recView.layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false) // Устанавливаем LayoutManager
    }

    private fun navigateToMealDetails(meal: MealDB) {
        val intent = Intent(context, MealDetailesActivity::class.java) // Создаем Intent для перехода к деталям блюда
        intent.putExtra(MEAL_ID,meal.mealId.toString()) // Передаем идентификатор блюда
        intent.putExtra(MEAL_STR,meal.mealName) // Передаем название блюда
        intent.putExtra(MEAL_THUMB,meal.mealThumb) // Передаем изображение блюда
        startActivity(intent) // Запускаем активити деталей блюда
    }

    private fun observeSwipeToDelete() {
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback( // Создаем объект ItemTouchHelper для обработки свайпов
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition // Получаем позицию свайпнутого элемента
                val deletedMeal = myAdapter.getMelaByPosition(position) // Получаем удаленное блюдо
                detailsMVVM.deleteMeal(deletedMeal) // Удаляем блюдо из базы данных
                showDeleteSnackBar(deletedMeal) // Показываем Snackbar с опцией отмены удаления
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recView) // Привязываем ItemTouchHelper к RecyclerView
    }

    private fun showDeleteSnackBar(deletedMeal: MealDB) {
        Snackbar.make(requireView(), "Блюдо удалено!", Snackbar.LENGTH_LONG).apply { // Показываем Snackbar с оповещением об удалении
            setAction("Undo") {
                detailsMVVM.insertMeal(deletedMeal) // Если пользователь нажимает на "Отмена", восстанавливаем удаленное блюдо
            }
            show()
        }
    }
}
