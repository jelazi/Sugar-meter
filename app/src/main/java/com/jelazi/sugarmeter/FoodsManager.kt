package com.jelazi.sugarmeter

import android.app.Activity
import androidx.preference.PreferenceManager

object FoodsManager {
    var foodsList: ArrayList<Food> = arrayListOf()
    private val PREF_FOODS_LIST = "foods_list"

    init {
    }

    fun getFoodByName(nameFood: String): Food? {
        if (foodsList.isEmpty()) return null
        for (food in foodsList) {
            if (food.name == nameFood) return food
        }
        return null
    }

    fun getFoodById(id: Int): Food? {
        if (foodsList.isEmpty()) return null
        for (food in foodsList) {
            if (food.id == id) return food
        }
        return null
    }

    fun deleteFood(id: Int): Boolean {
        if (foodsList.isEmpty()) return false
        for(i in 0..foodsList.size - 1) {
            if (foodsList[i].id == id) {
                foodsList.removeAt(i)
                return true
            }
        }
        return false
    }

    fun addFood(food: Food): Boolean {
        if (!food.isCorrect()) return false
        foodsList.add(food)
        return true
    }


    fun changeFood(newFood:Food):Boolean {
        if (!newFood.isCorrect()) return false
        var oldFood = newFood.id?.let { getFoodById(it) }
        if (oldFood?.name != newFood.name) oldFood?.name = newFood.name
        if (oldFood?.listIngredients != newFood.listIngredients) oldFood?.listIngredients = newFood.listIngredients
        if (oldFood?.realWeight != newFood.realWeight) oldFood?.realWeight = newFood.realWeight
        return true
    }

    fun getListFoodsFromPreferences (activity: Activity):Boolean {
        val preference = PreferenceManager.getDefaultSharedPreferences(activity)
        val listFoodsString: String? = preference.getString(PREF_FOODS_LIST, "")
        if (listFoodsString?.let { it.isEmpty() } == true) return false
        listFoodsString?.let { FoodsManager.foodsList = JsonParser.jsonToListFoods(it)}
        return true
    }

    fun setListFoodsToPreferences (activity: Activity): Boolean {
        val listFoodsString: String = JsonParser.listFoodsToJson()
        val preference = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor = preference.edit()
        editor.putString(PREF_FOODS_LIST, listFoodsString)
        editor.apply()
        return true
    }

    private fun containsID(id: Int) : Boolean {
        for (food in foodsList) {
            if (food.id == id) return true
        }
        return false
    }

    fun getLastUsableId() : Int {
        if (foodsList.isEmpty()) return 1
        var id = 1
        while (containsID(id)) {
            id++
        }
        return id
    }

    fun clearFoods():Boolean {
        if (foodsList.isEmpty()) return false
        foodsList.clear()
        return true
    }

}