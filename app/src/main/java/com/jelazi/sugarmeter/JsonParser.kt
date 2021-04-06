package com.jelazi.sugarmeter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken


object JsonParser {
    fun listTypeIngredientsToJson ():String {
        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        val jsonListBookPretty: String = gsonPretty.toJson(TypeIngredientsManager.listTypeIngredients)
        return jsonListBookPretty
    }

    fun jsonToListTypeIngredients (json: String):ArrayList<TypeIngredient> {
        val gson = Gson()
        val itemType = object : TypeToken<ArrayList<TypeIngredient>>() {}.type
        val jsonObject = gson.fromJson<ArrayList<TypeIngredient>>(json, itemType)
        for (ingredient in jsonObject) {
            ingredient.controlCorrect()
        }
        return jsonObject
    }

    fun listFoodsToJson ():String {
        val gsonPretty = GsonBuilder().setPrettyPrinting().create()
        val jsonListBookPretty: String = gsonPretty.toJson(FoodManager.foodsList)
        return jsonListBookPretty
    }

    fun jsonToListFoods (json: String):ArrayList<Food> {
        val gson = Gson()
        val itemType = object : TypeToken<ArrayList<Food>>() {}.type
        val jsonObject = gson.fromJson<ArrayList<Food>>(json, itemType)
        for (food in jsonObject) {
            food.controlCorrect()
        }
        return jsonObject
    }
}