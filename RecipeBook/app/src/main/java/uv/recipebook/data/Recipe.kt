package uv.recipebook.data

import android.graphics.Bitmap
import java.io.Serializable

data class Recipe(
    var id: Int,
    var image: Bitmap,
    var title: String,
    var description: String,
    var ingredientList: MutableList<Ingredient>,
    var numPeople: Int
) : Serializable {
    constructor(
        id: Int,
        image: Bitmap,
        title: String,
        description: String,
        ingredientList: MutableList<Ingredient>
    ) : this(
        id = id,
        image = image,
        title = title,
        description = description,
        ingredientList = ingredientList,
        numPeople = 1
    )

    constructor() : this(
        0,
        Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
        "",
        "",
        ArrayList()
    )
}