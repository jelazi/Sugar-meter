package com.jelazi.sugarmeter

import android.widget.Button
import android.widget.ListView

class Food (name: String){
    var name: String? = null
    var listIngredients = arrayListOf<Ingredient>()


    init {
        this.name = name
    }

    fun addIngredient(ingredient: Ingredient): Boolean {
        if (containsIngredient(ingredient)) return false
        listIngredients.add(ingredient)
        return true
    }

    fun getIngredientByName (name: String): Ingredient? {
        for (ingredient in listIngredients) {
            if (ingredient.name === name) return ingredient
        }
        return null
    }

    fun containsIngredient(newIngredient: Ingredient) : Boolean {
        if (listIngredients.isEmpty()) return false
        for (ingredient in listIngredients) {
            if (ingredient.name === newIngredient.name) return true
        }
        return false
    }

    fun isCorrect():Boolean {
        if (name.isNullOrEmpty()) return false
        if (listIngredients.isEmpty()) return false
        for (ingredient in listIngredients) {
            if (!ingredient.isCorrect()) return false
        }
        return true
    }

    fun controlCorrect() {
        if (name.isNullOrEmpty()) name = "name"

    }
}