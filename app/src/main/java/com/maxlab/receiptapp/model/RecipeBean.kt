package com.maxlab.receiptapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class RecipeBean : Serializable{

    @SerializedName("@id")
    private var id: String = ""
    private var imgPath : String = ""
    private var name: String = ""
    private var description : String = ""
    private var ingredient : String = ""
    private var step : String = ""
    private var type : String = ""

    constructor()

    constructor(
        id: String,
        imgPath: String,
        name: String,
        description: String,
        ingredient: String,
        step: String,
        type : String
    ) {
        this.id = id
        this.imgPath = imgPath
        this.name = name
        this.description = description
        this.ingredient = ingredient
        this.step = step
        this.type = type
    }


    fun getId(): String {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getImgPath(): String {
        return imgPath
    }

    fun setImgPath(imgPath: String) {
        this.imgPath = imgPath
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun setDescription(description : String){
        this.description = description
    }

    fun getDescription() : String{
        return this.description
    }

    fun setIngredient(ingredient : String){
        this.ingredient = ingredient
    }

    fun getIngredient() : String{
        return this.ingredient
    }
    fun setStep(step: String){
        this.step = step
    }

    fun getStep() : String{
        return this.step
    }
    fun setType(type : String){
        this.type = type
    }

    fun getType() : String{
        return this.type
    }



}