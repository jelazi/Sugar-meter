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

    public fun addPathPicture(pathPicture: String) {
        this.pathPicture = pathPicture
    }


}