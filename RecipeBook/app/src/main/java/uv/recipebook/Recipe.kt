package uv.recipebook

import android.graphics.Bitmap

class Recipe {
    var image: Bitmap
    var title: String
    var description: String
    var ingredientList: MutableList<Ingredient>

    constructor(image: Bitmap, title: String,description: String, ingredientList: MutableList<Ingredient>) {
        this.image = image
        this.title = title
        this.description = description
        this.ingredientList = ingredientList
    }
}