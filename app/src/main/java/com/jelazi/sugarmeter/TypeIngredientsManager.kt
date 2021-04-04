package com.jelazi.sugarmeter


object TypeIngredientsManager {
    var typeIngredients : ArrayList<TypeIngredient> = arrayListOf()

    init {
        loadTestData()
    }

    fun loadTestData() {
        val nameIngredients = arrayOf("bread", "butter", "roll", "cheese", "water", "beans", "potatoes", "powder", "banana", "apple", "peach", "pasta")
        val valueIngredients = arrayOf(120.5, 145.8, 150.3, 98.6, 102.4, 150.0, 120.5, 145.8, 150.3, 98.6, 102.4, 150.0)
        for (i in 0..nameIngredients.size -1) {
            newTypeIngredient(nameIngredients[i], valueIngredients[i])
        }
    }

    fun newTypeIngredient(name:String, valueSugar: Double) {
        var newIngr: TypeIngredient = TypeIngredient(getLastUsableId(), name, valueSugar)
        typeIngredients.add(newIngr)
    }

    fun changeNameTypeIngredient(id: Int, newName: String) : Boolean{
        if (typeIngredients.isEmpty()) return false
        for (typeIngredient in typeIngredients) {
            if (typeIngredient.id == id) {
                typeIngredient.name = newName
                return true
            }
        }
        return false

    }

    fun changeValueTypeIngredient(id: Int, newValue: Double) : Boolean {
        if (typeIngredients.isEmpty()) return false
        for (ingredient in typeIngredients) {
            if (ingredient.id == id) {
                ingredient.valueSugar = newValue
                return true
            }
        }
        return false
    }

    fun deleteIngredient(id: Int) : Boolean {
        if (typeIngredients.isEmpty()) return false
        for(i in 0..typeIngredients.size - 1) {
            if (typeIngredients[i].id == id) {
                typeIngredients.removeAt(i)
                return true
            }
        }
        return false
    }

    fun getTypeIngredientByName (name: String) :TypeIngredient?{
        for (typeIngredient in typeIngredients) {
            if (typeIngredient.name === name) {
                return typeIngredient
            }
        }
        return null
    }


    private fun getLastUsableId() : Int {
        if (typeIngredients.isEmpty()) return 1
        var id = 1
        while (containsID(id)) {
            id++
        }
        return id
    }

    private fun containsID(id: Int) : Boolean {
        for (ingredient in typeIngredients) {
            if (ingredient.id == id) return true
        }
        return false
    }

    fun isSameName(newName: String) : Boolean {
        if (typeIngredients.isEmpty()) return false
        for (ingredient in typeIngredients) {
            if (ingredient.name?.compareTo(newName, true) == 0) return true
        }
        return false
    }




}