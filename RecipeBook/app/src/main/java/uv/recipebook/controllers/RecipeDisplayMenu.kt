package uv.recipebook.controllers

import android.database.SQLException
import android.database.sqlite.SQLiteException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import uv.recipebook.R
import uv.recipebook.data.DBAdmin
import uv.recipebook.data.Ingredient
import uv.recipebook.data.Recipe
import uv.recipebook.utils.RecyclerAdapter_Ingredient

class RecipeDisplayMenu : AppCompatActivity() {
    var recipeDisplayed: Recipe? = null
    var recipeID: Int = 0

    lateinit var iv_image: ImageView
    lateinit var et_title: EditText
    lateinit var et_description: EditText
    lateinit var et_ingredientName: EditText
    lateinit var et_ingAmountGr: EditText
    lateinit var et_peopleNum: EditText
    lateinit var rv_ingredients: RecyclerView
    var adapter: RecyclerAdapter_Ingredient = RecyclerAdapter_Ingredient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_display_menu)

        recipeID = intent.getSerializableExtra("recipe_display_id") as Int
        if (recipeID > 0) {
            recipeDisplayed = getRecipe(recipeID)

            if(recipeDisplayed != null) {
                setupComponents(recipeDisplayed!!)
            } else {
                Toast.makeText(this, "No se encuentra la receta en la base de datos", Toast.LENGTH_SHORT)
            }
        }
    }

    private fun setupComponents(recipe: Recipe) {
        iv_image = this.findViewById(R.id.iv_recipeImage)
        et_title = this.findViewById(R.id.et_title)
        et_description = this.findViewById(R.id.et_description)
        et_peopleNum = this.findViewById(R.id.et_peopleNum)
        rv_ingredients = this.findViewById(R.id.rv_ingredients)

        iv_image.setImageBitmap(recipe.image)
        et_title.setText(recipe.title)
        et_description.setText(recipe.description)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        rv_ingredients.layoutManager = LinearLayoutManager(this)
        rv_ingredients.setHasFixedSize(true)

        adapter.RecyclerAdapter_Ingredient(recipeDisplayed!!.ingredientList, this)
        rv_ingredients.adapter = adapter
    }

    private fun getRecipe(recipeID: Int): Recipe? {
        var dbRecipe: Recipe? = null

        try {
            val admin = DBAdmin(this, "recetorium", null, 1)
            val bd = admin.writableDatabase

            val dataset = bd.rawQuery(
                "SELECT imagen, titulo, descripcion, ingredientes " +
                        "FROM recetas " +
                        "WHERE id = '$recipeID'",
                null)

            if (dataset.moveToFirst()) {
                dbRecipe = Recipe()
                dbRecipe.image = getBitmap(dataset.getBlob(0))
                dbRecipe.title = dataset.getString(1)
                dbRecipe.description = dataset.getString(2)
                dbRecipe.ingredientList = getIngredients(dataset.getString(3))
            }
        } catch (sqliteException: SQLiteException) {
            Toast.makeText(
                this,
                "No se pudo realizar la conexión con la base de datos",
                Toast.LENGTH_SHORT
            ).show()
        } catch (sqlException: SQLException) {
            Toast.makeText(
                this,
                sqlException.message,
                Toast.LENGTH_SHORT
            ).show()
        }

        return dbRecipe
    }

    private fun getIngredients(ingredients: String): MutableList<Ingredient> {
        var ingredientName = ""
        var amount = ""
        var ingredientIterator = 0
        var ingredientsList: MutableList<Ingredient> = ArrayList()
        var switcher = 0
        for (i in ingredients.indices) {
            when {
                //, Represents the end of the ingredient's name
                ingredients[i].equals(',') -> {
                    switcher = 1
                }
                //; Represents the end of the description of an ingredient
                ingredients[i].equals(';') -> {
                    switcher = 0
                    ingredientsList.add(Ingredient(ingredientName,amount.toInt()))
                    ingredientName = ""
                    amount = ""
                    ingredientIterator++
                }
                else -> {
                    when (switcher) {
                        0 -> {
                            ingredientName = ingredientName.plus(ingredients[i])
                        }
                        1 -> {
                            amount = amount.plus(ingredients[i])
                        }
                    }
                }
            }
        }
        return ingredientsList
    }

    private fun getBitmap(blob: ByteArray): Bitmap {
        val img: ByteArray = blob
        return BitmapFactory.decodeByteArray(img,0,img.size)
    }
}