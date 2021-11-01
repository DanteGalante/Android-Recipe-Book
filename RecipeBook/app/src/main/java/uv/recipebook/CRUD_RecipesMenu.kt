package uv.recipebook

import android.database.SQLException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CRUD_RecipesMenu : AppCompatActivity() {
    lateinit var mRecyclerView: RecyclerView
    val mAdapter: RecyclerAdapter = RecyclerAdapter()
    lateinit var alert: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_recipes_menu)
        setUpRecyclerView()
    }

    fun setUpRecyclerView() {
        alert = findViewById<TextView>(R.id.tv_alert)
        mRecyclerView = findViewById<RecyclerView>(R.id.rv_recipes)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter.RecyclerAdapter(getRecipes(),this)
        mRecyclerView.adapter = mAdapter
    }

    fun getRecipes() : MutableList<Recipe> {
        var recipes: MutableList<Recipe> = ArrayList()
        try {
            val admin = DBAdmin (this, "administracion", null, 1)
            val bd = admin.writableDatabase
            val row = bd.rawQuery("SELECT imagen, titulo, descripcion, ingredientes FROM recetas",null)
            if (row.moveToFirst()) {
                do {
                    recipes.add(Recipe(
                        getImage(row.getBlob(0)),
                        row.getString(1),
                        row.getString(2),
                        getIngredients(row.getString(3))))
                } while (row.moveToNext())
            } else {
                alert.text = "No hay recetas personales,¡Crea una!"
            }
            bd.close()
        } catch (sqlException: SQLException) {
            sqlException.printStackTrace()
            alert.text = "Error al hacer conexion con la base de datos, favor de intentarlo más tarde"
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
                ingredients[i].equals(",") -> {
                    switcher = 1
                }
                //; Represents the end of the description of an ingredient
                ingredients[i].equals(";") -> {
                    switcher = 0
                    ingredientsList.add(Ingredient(ingredientName,amount.toInt()))
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