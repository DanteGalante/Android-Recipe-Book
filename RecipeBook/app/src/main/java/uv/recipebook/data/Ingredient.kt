package uv.recipebook.data

import java.io.Serializable

data class Ingredient (
    var ingredient : String,
    var amountInGr : Int
) : Serializable