package com.jelazi.sugarmeter

class Ingredient (typeIngredient: TypeIngredient) {
    var typeIngredient: TypeIngredient? = null
    var weight: Double? = 0.0
    private set
    var name: String? = null
    private set

    init {
        this.typeIngredient = typeIngredient
        this.name = typeIngredient.name
    }



    fun changeWeight (newWeight: Double) : Boolean{
        if (newWeight < 0.0) return false
        weight = newWeight
        return true
    }

}