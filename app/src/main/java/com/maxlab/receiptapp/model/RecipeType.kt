package com.maxlab.receiptapp.model


class RecipeType {

  private var name : String = "";

    constructor(name : String){
        this.name = name
    }

    constructor()


    fun getName() : String{
        return name
    }

    fun setName(name : String){
        this.name = name
    }

    override fun toString(): String {
        return getName()
    }

}