package uv.recipebook.controllers

import android.content.Intent
import android.database.SQLException
import android.database.sqlite.SQLiteException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import uv.recipebook.*
import uv.recipebook.data.DBAdmin
import uv.recipebook.data.Ingredient
import uv.recipebook.data.Recipe

class CRUD_RecipesMenu : AppCompatActivity() {
    lateinit var mRecyclerView: RecyclerView
    val mAdapter: RecyclerAdapter = RecyclerAdapter()
    lateinit var alert: TextView
    lateinit var dbAdmin : DBAdmin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_recipes_menu)
        alert = findViewById(R.id.tv_alert)

        try {
            dbAdmin = DBAdmin (this, "recetorium", null, 1)
        } catch (sqlException: SQLException) {
            sqlException.printStackTrace()
            alert.text = "Error al hacer conexion con la base de datos, favor de intentarlo más tarde"
        }
        setUpRecyclerView()

        val addRecipe = findViewById<FloatingActionButton>(R.id.fab_addRecipe)
        addRecipe.setOnClickListener {
            val intent = Intent(this, CreateRecipeMenu::class.java)
            startActivity(intent)
        }

        val refreshRecipe = findViewById<FloatingActionButton>(R.id.fab_refresh)
        refreshRecipe.setOnClickListener {
            setUpRecyclerView()
        }
    }

    fun setUpRecyclerView() {
        mRecyclerView = findViewById(R.id.rv_recipes)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter.RecyclerAdapter(getRecipes(),this, dbAdmin)
        mRecyclerView.adapter = mAdapter
    }

    fun getRecipes() : MutableList<Recipe> {
        var recipes: MutableList<Recipe> = ArrayList()
        try {
            val bd = dbAdmin.writableDatabase
            val row = bd.rawQuery("SELECT id, imagen, titulo, descripcion, ingredientes " +
                                      "FROM recetas",null)
            if (row.moveToFirst()) {
                do {
                    recipes.add(
                        Recipe(
                        row.getInt(0),
                        getImage(row.getBlob(1)),
                        row.getString(2),
                        row.getString(3),
                        getIngredients(row.getString(4)))
                    )
                } while (row.moveToNext())
            } else {
                alert.text = "No hay recetas personales,¡Crea una!"
            }

            bd.close()
        } catch (sqlException: SQLiteException) {
            sqlException.printStackTrace()

            Toast.makeText(
                this,
                "Error al tratar de obtener las recetas de la base de datos," +
                     " favor de intentarlo más tarde",
                Toast.LENGTH_SHORT
            ).show()
        }

        return recipes
    }

    private fun getIngredients(ingredients: String): MutableList<Ingredient> {
        var ingredientName = ""
        var amount = ""
        var ingredientIterator = 0
        var ingredientsList: MutableList<Ingredient> = ArrayList()
        var switcher = 0
        for (i in ingredients.indices ) {
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

    private fun getImage(blob: ByteArray): Bitmap {
        val img: ByteArray = blob
        return BitmapFactory.decodeByteArray(img,0,img.size)
    }
}