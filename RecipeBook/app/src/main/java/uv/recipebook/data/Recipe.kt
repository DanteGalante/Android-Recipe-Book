package uv.recipebook.data

import android.graphics.Bitmap
import uv.recipebook.data.Ingredient

data class Recipe(
    var image: Bitmap,
    var title: String,
    var description: String,
    var ingredientList: MutableList<Ingredient>
)