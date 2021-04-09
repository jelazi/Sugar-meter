package com.jelazi.sugarmeter

class Ingredient (typeIngredient: TypeIngredient, weight: Double) {
    var typeIngredient: TypeIngredient? = null
    var weight: Double = 0.0
    private set
    var name: String? = null
    private set

    init {
        this.typeIngredient = typeIngredient
        this.name = typeIngredient.name
        this.weight = weight
    }



    fun changeWeight (newWeight: Double) : Boolean{
        if (newWeight < 0.0) return false
        weight = newWeight
        return true
    }

    fun getWeightSugar(): Double {
        if (weight == 0.0 || typeIngredient?.valueSugar == 0.0) return 0.0
        return typeIngredient?.valueSugar!! / 100 * weight
    }

    fun isCorrect():Boolean {
        if (typeIngredient == null ) return false
        if (weight <= 0.0) return false
        if (name.isNullOrEmpty()) return false
        return true
    }



}