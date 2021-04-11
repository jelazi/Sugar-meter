package com.jelazi.sugarmeter

import android.widget.Button
import android.widget.ListView

class Food (id: Int, name: String){
    var name: String? = null
    var listIngredients = arrayListOf<Ingredient>()
    var id: Int? = null

    init {
        this.name = name
        this.id = id
    }

    fun addIngredient(ingredient: Ingredient): Boolean {
        if (containsIngredient(ingredient)) return false
        listIngredients.add(ingredient)
        return true
    }

    fun getUsableTypeIntents(): ArrayList<Int> {
        var listId: ArrayList<Int> = arrayListOf()
                if (listIngredients.isEmpty() ) return listId
        for (ingredient in listIngredients) {
            ingredient.typeIngredient?.id?.let { listId.add(it) }
        }
        return listId

    }

    fun deleteIngredient(ingredient: Ingredient):Boolean {
        listIngredients.remove(ingredient)
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

    fun sumWeightFood(): Double {
        if (listIngredients.isEmpty()) return 0.0
        var result = 0.0
        for (ingredient in listIngredients) {
            result += ingredient.weight
        }
        return result
    }

    fun sumWeightSugar(): Double {
        if (listIngredients.isEmpty()) return 0.0
        var result = 0.0
        for (ingredient in listIngredients) {
            result += ingredient.getWeightSugar()
        }
        return result
    }

    fun weightSugarInPartFood(weightPartFood: Double):Double  {
        if (weightPartFood == 0.0 || listIngredients.isEmpty()) return 0.0
        var weightFood = sumWeightFood()
        var weighSugar = sumWeightSugar()
        var ratio = weighSugar / weightFood
        return weightPartFood * ratio
    }

    fun sugarInOneGramFood():Double {
        var weightFood = sumWeightFood()
        var weighSugar = sumWeightSugar()
        return weighSugar / weightFood
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