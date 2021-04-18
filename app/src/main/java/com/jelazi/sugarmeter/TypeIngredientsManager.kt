package com.jelazi.sugarmeter

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager


object TypeIngredientsManager {
    var listTypeIngredients : ArrayList<TypeIngredient> = arrayListOf()
    private val PREF_INGREDIENTS_LIST = "ingredients_list"

    init {
    }

    fun loadTestData() {
        val nameIngredients = arrayOf("bread", "butter", "roll", "cheese", "water", "beans", "potatoes", "powder", "banana", "apple", "peach", "pasta")
        val valueIngredients = arrayOf(120.5, 145.8, 150.3, 98.6, 102.4, 150.0, 120.5, 145.8, 150.3, 98.6, 102.4, 150.0)
        for (i in 0..nameIngredients.size -1) {
           var newIngr =  getNewTypeIngredient(nameIngredients[i], valueIngredients[i])
        }
    }

    fun getListTypeIngredientsFromPreferences (activity: Activity):Boolean {

        val preference = PreferenceManager.getDefaultSharedPreferences(activity)
        val listIngredientsString: String? = preference.getString(PREF_INGREDIENTS_LIST, "")
        if (listIngredientsString?.let { it.isEmpty() } == true) return false
        listIngredientsString?.let {listTypeIngredients = JsonParser.jsonToListTypeIngredients(it)}
        return true
    }

    fun setListTypeIngredientsToPreferences (activity: Activity): Boolean {
        val listTypeIngredientsString: String = JsonParser.listTypeIngredientsToJson()
        val preference = PreferenceManager.getDefaultSharedPreferences(activity)
        val editor = preference.edit()
        editor.putString(PREF_INGREDIENTS_LIST, listTypeIngredientsString)
        editor.apply()
        return true
    }

    fun getNewTypeIngredient(name:String, valueSugar: Double):TypeIngredient {
        var newIngr: TypeIngredient = TypeIngredient(getLastUsableId(), name, valueSugar)
        return newIngr;
    }

    fun addIngredient(ingredient: TypeIngredient?):Boolean {
        if (ingredient == null) return false
        if (ingredient.isCorrect()) {
            listTypeIngredients.add(ingredient)
            return true
        }
        return false
    }


    fun deleteIngredient(id: Int) : Boolean {
        if (listTypeIngredients.isEmpty()) return false
        for(i in 0..listTypeIngredients.size - 1) {
            if (listTypeIngredients[i].id == id) {
                listTypeIngredients.removeAt(i)
                return true
            }
        }
        return false
    }

    fun getTypeIngredientByName (name: String) :TypeIngredient?{
        for (typeIngredient in listTypeIngredients) {
            if (typeIngredient.name === name) {
                return typeIngredient
            }
        }
        return null
    }

    fun getTypeIngredientById (id: Int): TypeIngredient? {
        for (typeIngredient in listTypeIngredients) {
            if (typeIngredient.id == id) {
                return typeIngredient
            }
        }
        return null
    }


    fun getLastUsableId() : Int {
        if (listTypeIngredients.isEmpty()) return 1
        var id = 1
        while (containsID(id)) {
            id++
        }
        return id
    }

    private fun containsID(id: Int) : Boolean {
        for (ingredient in listTypeIngredients) {
            if (ingredient.id == id) return true
        }
        return false
    }

    fun isSameName(newName: String) : Boolean {
        if (listTypeIngredients.isEmpty()) return false
        for (ingredient in listTypeIngredients) {
            if (ingredient.name?.compareTo(newName, true) == 0) return true
        }
        return false
    }

    fun clearIngredients():Boolean {
        if (listTypeIngredients.isEmpty()) return false
        listTypeIngredients.clear()
        return true
    }

    fun changeIngredient(id: Int, newName: String, newValue: Double): Boolean {
        var oldIngredient = getTypeIngredientById(id)
        if (oldIngredient == null) return false
        if (oldIngredient.name != newName) oldIngredient.name = newName
        if (oldIngredient.valueSugar != newValue) oldIngredient.valueSugar = newValue
        return true
    }



}