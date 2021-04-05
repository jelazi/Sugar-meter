package com.jelazi.sugarmeter

class TypeIngredient (id: Int, name: String, valueSugar: Double ){
    var name : String? = null

    var id: Int? = null
    private set

    var valueSugar: Double? = null

    var pathPicture: String? = null
    private set

    init {
        this.id = id
        this.name = name
        this.valueSugar = valueSugar
    }

    fun addPathPicture(pathPicture: String) {
        this.pathPicture = pathPicture
    }

    fun controlCorrect() {
        if (name==null) name = ""
        if (id == null) id = TypeIngredientsManager.getLastUsableId()
        if (valueSugar == null) valueSugar = 0.0
        if (pathPicture == null) pathPicture = ""
    }

    fun isCorrect():Boolean {
        if (name== null || name?.let { it.isEmpty() } == true) return false
        if (id == null || id?.let { it < 1} == true) return false
        if (valueSugar == null || valueSugar?.let { it < 0} == true) return false
        return true
    }



}