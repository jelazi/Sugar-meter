package com.jelazi.sugarmeter

import android.app.Activity
import android.util.Log
import androidx.preference.PreferenceManager

object FoodManager {
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

    fun addFood(food: Food): Boolean {
        if (!food.isCorrect()) return false
        foodsList.add(food)
        return true
    }

    fun getListFoodsFromPreferences (activity: Activity):Boolean {
        val preference = PreferenceManager.getDefaultSharedPreferences(activity)
        val listFoodsString: String? = preference.getString(PREF_FOODS_LIST, "")
        if (listFoodsString?.let { it.isEmpty() } == true) return false
        listFoodsString?.let { FoodManager.foodsList = JsonParser.jsonToListFoods(it)}
        return true
    }

    fun setListTypeIngredientsToPreferences (activity: Activity): Boolean {
        val listFoodsString: String = JsonParser.listFoodsToJson()
        val preference = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor = preference.edit()
        editor.putString(PREF_FOODS_LIST, listFoodsString)
        editor.apply()
        return true
    }

}