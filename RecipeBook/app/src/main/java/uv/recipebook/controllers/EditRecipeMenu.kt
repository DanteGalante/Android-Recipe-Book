package uv.recipebook.controllers

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.SQLException
import android.database.sqlite.SQLiteException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import uv.recipebook.R
import uv.recipebook.data.DBAdmin
import uv.recipebook.data.Ingredient
import uv.recipebook.data.Recipe
import uv.recipebook.utils.RecyclerAdapter_Ingredient
import java.io.ByteArrayOutputStream

class EditRecipeMenu : AppCompatActivity() {
    var recipe_id: Int = 0
    var recipe: Recipe? = null

    lateinit var iv_image: ImageView
    lateinit var et_title: EditText
    lateinit var et_description: EditText
    lateinit var et_ingredientName: EditText
    lateinit var et_ingAmountGr: EditText
    lateinit var rv_ingredients: RecyclerView
    var adapter: RecyclerAdapter_Ingredient = RecyclerAdapter_Ingredient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recipe_menu)

        recipe_id = intent.getSerializableExtra("recipe_edit_id") as Int
        recipe = getRecipe(recipe_id)

        if(recipe != null) {
            setupComponents(recipe!!)
        } else {
            Toast.makeText(this, "No se encuentra la receta en la base de datos",Toast.LENGTH_SHORT)
        }
    }

    private fun setupComponents(recipe: Recipe) {
        iv_image = this.findViewById(R.id.iv_recipeImage)
        et_title = this.findViewById(R.id.et_title)
        et_description = this.findViewById(R.id.et_description)
        et_ingredientName = this.findViewById(R.id.et_ingredient)
        et_ingAmountGr = this.findViewById(R.id.nt_amountGr)
        rv_ingredients = this.findViewById(R.id.rv_ingredients)
        val uploadImageFAB = this.findViewById<FloatingActionButton>(R.id.fb_upload)
        val addIngredientBTN = this.findViewById<ImageButton>(R.id.ib_addIngredient)
        val saveEditsBTN = this.findViewById<Button>(R.id.btn_saveEdits)

        iv_image.setImageBitmap(recipe.image)
        et_title.setText(recipe.title)
        et_description.setText(recipe.description)
        setupRecyclerView()

        uploadImageFAB.setOnClickListener {
            selectImage()
        }
        addIngredientBTN.setOnClickListener {
            if (et_ingredientName.text.isNotEmpty()) {
                if (et_ingAmountGr.text.isNotEmpty()) {
                    //ingredientsList.add(Ingredient(ingredientName.text.toString(),amount.text.toString().toInt()))
                    adapter.addIngrediente(
                        Ingredient(
                            et_ingredientName.text.toString(),
                            et_ingAmountGr.text.toString().toInt()
                        )
                    )
                    adapter.notifyDataSetChanged()
                    et_ingredientName.setText("")
                    et_ingAmountGr.setText("")
                } else {
                    Toast.makeText(this, "Favor de agregar la cantidad del ingrediente (en gramos)", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Favor de agregar el nombre del ingrediente", Toast.LENGTH_SHORT).show()
            }
        }
        saveEditsBTN.setOnClickListener {
            //TODO WIP: This must save the changes made to the recipe in the database
            if (validIntroducedText()) {
                val newRecipeData = Recipe(
                    0,
                    iv_image.drawable.toBitmap(),
                    et_title.text.toString(),
                    et_description.text.toString(),
                    adapter.ingredientList
                )

                try {
                    val admin = DBAdmin(this, "recetorium", null, 1)
                    val bd = admin.writableDatabase
                    val edit = ContentValues()

                    edit.put("imagen", bitmapToArray(newRecipeData.image))
                    edit.put("titulo", newRecipeData.title)
                    edit.put("descripcion",newRecipeData.description)
                    edit.put("ingredientes",getIngredients())

                    val changedRows = bd.update(
                        "recetas",
                        edit,
                        "id = '$recipe_id'",
                        null
                    )

                    bd.close()

                    if (changedRows == 1) {
                        Toast.makeText(this, "Se han realizado los cambios con éxito", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "No se encontro la receta que desea cambiar", Toast.LENGTH_SHORT).show()
                    }

                } catch (sqlException: SQLException) {
                    sqlException.printStackTrace()
                    Toast.makeText(this, "Error al hacer conexion con la base de datos, favor de intentarlo más tarde", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getIngredients(): String {
        var ingredients: String = ""

        for (item in adapter.ingredientList) {
            ingredients += "${item.ingredient},${item.amountInGr};"
        }

        return ingredients
    }

    private fun bitmapToArray(image: Bitmap): ByteArray {
        var outStream = ByteArrayOutputStream()

        image.compress(Bitmap.CompressFormat.PNG,0,outStream)

        return outStream.toByteArray()
    }

    private fun validIntroducedText(): Boolean {
        var valid = false

        when {
            (iv_image.drawable == null) -> {
                Toast.makeText(this, "Falta introducir una imagen para la receta", Toast.LENGTH_SHORT).show()
            }
            (et_title.text.isEmpty()) -> {
                Toast.makeText(this, "Falta introducir un titulo para la receta", Toast.LENGTH_SHORT).show()
            }
            (et_description.text.isEmpty()) -> {
                Toast.makeText(this, "Falta introducir una descripción para la receta", Toast.LENGTH_SHORT).show()
            }
            (adapter.ingredientList.isEmpty()) -> {
                Toast.makeText(this, "Falta introducir los ingredientes para la receta", Toast.LENGTH_SHORT).show()
            } else -> {
                valid = true
            }
        }

        return valid
    }

    private fun setupRecyclerView() {
        rv_ingredients.layoutManager = LinearLayoutManager(this)
        rv_ingredients.setHasFixedSize(true)

        adapter.RecyclerAdapter_Ingredient(recipe!!.ingredientList, this)
        rv_ingredients.adapter = adapter
    }

    private fun getRecipe(recipeId: Int): Recipe? {
        var dbRecipe: Recipe? = null

        try {
            val admin = DBAdmin(this, "recetorium", null, 1)
            val bd = admin.writableDatabase

            val dataset = bd.rawQuery(
                "SELECT imagen, titulo, descripcion, ingredientes " +
                    "FROM recetas " +
                    "WHERE id = '$recipeId'",
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

    private fun selectImage() {
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                iv_image.setImageURI(data?.data)
            }
        }.launch(intent)
    }
}