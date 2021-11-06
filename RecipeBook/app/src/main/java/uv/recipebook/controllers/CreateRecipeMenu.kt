package uv.recipebook.controllers

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.SQLException
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import uv.recipebook.*
import uv.recipebook.data.DBAdmin
import uv.recipebook.data.Ingredient
import uv.recipebook.data.Recipe
import uv.recipebook.utils.RecyclerAdapter_Ingredient
import java.io.ByteArrayOutputStream

class CreateRecipeMenu : AppCompatActivity() {
    lateinit var image: ImageView
    lateinit var title: EditText
    lateinit var description: EditText
    lateinit var recyclerView: RecyclerView
    var adapter: RecyclerAdapter_Ingredient = RecyclerAdapter_Ingredient()

    var activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            image.setImageURI(data?.data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_recipe_menu)
        image = findViewById(R.id.iv_recipeImage)
        title = findViewById(R.id.et_title)
        description = findViewById(R.id.et_description)
        setupRecyclerView()

        val upload = findViewById<FloatingActionButton>(R.id.fb_upload)
        val addIngredient = findViewById<ImageButton>(R.id.ib_addIngredient)
        val ingredientName = findViewById<EditText>(R.id.et_ingredient)
        val amount = findViewById<EditText>(R.id.nt_amountGr)
        val saveRecipe = findViewById<Button>(R.id.btn_saveRecipe)


        upload.setOnClickListener {
            selectImage()
        }

        addIngredient.setOnClickListener {
            if (ingredientName.text.isNotEmpty()) {
                if (amount.text.isNotEmpty()) {
                    //ingredientsList.add(Ingredient(ingredientName.text.toString(),amount.text.toString().toInt()))
                    adapter.addIngrediente(Ingredient(ingredientName.text.toString(),amount.text.toString().toDouble()))
                    adapter.notifyDataSetChanged()
                    ingredientName.setText("")
                    amount.setText("")
                } else {
                    Toast.makeText(this, "Favor de agregar la cantidad del ingrediente (en gramos)", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Favor de agregar el nombre del ingrediente", Toast.LENGTH_SHORT).show()
            }
        }

        saveRecipe.setOnClickListener {
            if (validIntroducedText()) {
                var recipe = Recipe(
                    0,
                    getImage(),
                    title.text.toString(),
                    description.text.toString(),
                    adapter.ingredientList
                )
                try {
                    val admin = DBAdmin(this, "recetorium", null, 1)
                    val bd = admin.writableDatabase
                    val register = ContentValues()
                    //register.put("id", null)
                    register.put("imagen", bitmapToArray(recipe.image))
                    register.put("titulo",recipe.title)
                    register.put("descripcion",recipe.description)
                    register.put("ingredientes",getIngredients())

                    println(bd.insert("recetas",null,register))
                    bd.close()

                    image.setImageBitmap(null)
                    title.setText("")
                    description.setText("")
                    adapter.ingredientList.clear()

                    Toast.makeText(this, "La receta se ha registrado con éxito", Toast.LENGTH_SHORT).show()
                } catch (sqlException: SQLException) {
                    sqlException.printStackTrace()
                    Toast.makeText(this, "Error al hacer conexion con la base de datos, favor de intentarlo más tarde", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getIngredients(): String? {
        var ingredients: String = ""

        for (item in adapter.ingredientList) {
            ingredients += "${item.ingredient},${item.amountInGr};"
        }
        return ingredients;
    }

    fun setupRecyclerView() {
        recyclerView = findViewById(R.id.rv_ingredients)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        adapter.RecyclerAdapter_Ingredient(ArrayList(), this)
        recyclerView.adapter = adapter

    }

    private fun bitmapToArray(image: Bitmap): ByteArray {
        var outStream = ByteArrayOutputStream()

        image.compress(Bitmap.CompressFormat.PNG,0,outStream)

        return outStream.toByteArray()
    }

    private fun getImage(): Bitmap {
        return image.drawable.toBitmap()
    }

    private fun validIntroducedText(): Boolean {
        var valid = false
        if(image.drawable != null && title.text.isNotEmpty() && description.text.isNotEmpty() && adapter.ingredientList.isNotEmpty()) {
            valid = true
        }else when {
            (image.drawable == null) -> {
                Toast.makeText(this, "Falta introducir una imagen para la receta", Toast.LENGTH_SHORT).show()
            }
            (title.text.isEmpty()) -> {
                Toast.makeText(this, "Falta introducir un titulo para la receta", Toast.LENGTH_SHORT).show()
            }
            (description.text.isEmpty()) -> {
                Toast.makeText(this, "Falta introducir una descripción para la receta", Toast.LENGTH_SHORT).show()
            }
            (adapter.ingredientList.isEmpty()) -> {
                Toast.makeText(this, "Falta introducir los ingredientes para la receta", Toast.LENGTH_SHORT).show()
            }
        }

        return valid
    }

    private fun selectImage() {
        var intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activityResultLauncher.launch(intent)
    }
}