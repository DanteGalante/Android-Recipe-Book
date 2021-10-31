package uv.recipebook

import android.graphics.Bitmap

class Recipe {
    var image: Bitmap
    var title: String
    var description: String
    var ingredientList: List<Ingredient>

    constructor(image: Bitmap, title: String,description: String, ingredientList: List<Ingredient>) {
        this.image = image
        this.title = title
        this.description = description
        this.ingredientList = ingredientList
    }
}