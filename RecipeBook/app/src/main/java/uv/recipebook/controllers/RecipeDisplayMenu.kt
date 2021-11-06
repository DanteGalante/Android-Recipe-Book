package uv.recipebook.controllers

import android.content.res.Resources
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
    lateinit var et_peopleNum: EditText
    lateinit var rv_ingredients: RecyclerView
    var adapter: RecyclerAdapter_Ingredient = RecyclerAdapter_Ingredient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_display_menu)

        val option = intent.getSerializableExtra("option") as Int
        when (option) {
            0 -> { //To display custom recipes
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

            1 -> { //To display salad recipes
                recipeID = intent.getSerializableExtra("salad_id") as Int

                if (recipeID > 0) {
                    when (recipeID) {
                        1 -> {
                            recipeDisplayed = Recipe(
                                recipeID,
                                BitmapFactory.decodeResource(resources, R.drawable.salad_mix),
                                "Ensalada mixta",
                                "1. Para la vinagreta, licúa los pimientos junto con el aceite de oliva, sal, pimienta y un poco de vinagre de vino tinto.\n" +
                                "2. Divide en dos recipientes y reserva.\n" +
                                "3. Para el aderezo, mezcla la mitad de la vinagreta con mayonesa y un poco de sal. Reserva.\n" +
                                "4. Blanquea los esparragos calentando suficiente agua para cubrir el medio manojo, agrega sal y cocina hasta hervir.\n" +
                                "5. Retira del agua y pasa a papel absorbente para eliminar todo el liquido.\n" +
                                "6. Calienta el aceite en un sartén, coloca los espárragos, sal, ajonjolí y saltea.\n" +
                                "7. Para la ensalada, sirve las lechugas, encima palmitos, elotitos cambray, salame en rollizos, salmon en rollitos, queso roquefort y aguacate.\n" +
                                "8. Acompaña la ensalada con el aderezo y la vinagreta.",
                                mutableListOf(
                                    Ingredient("pimiento morron", 1.0),
                                    Ingredient("aceite de oliva", 4.0),
                                    Ingredient("sal de mesa", 3.0),
                                    Ingredient("pimienta en polvo", 4.0),
                                    Ingredient("vinagre tinto", 2.0),
                                    Ingredient("Lechuga", 3.0),
                                    Ingredient("aguacate", 1.0)
                                ),
                                4
                            )
                        }

                        2 -> {
                            recipeDisplayed = Recipe(
                                recipeID,
                                BitmapFactory.decodeResource(resources, R.drawable.caesar_salad),
                                "Ensalada Cesar",
                                "1. Lavar bien las verduras , dejarla en agua por 15 minutos con un poco de sal o hipoclorito luego escurrirlas y hacerle los cortes respectivos, dejarlas con hielo para que conserven su frescura y crocancia.\n" +
                                "2. El pollo se cocina y se desmenuza, el pan se corta en cubos y se llevan al horno previamente calentado por 5 minutos hasta que doren.\n" +
                                "3. Llevar las verduras a una bandeja y decorar con el jitomate cherry, el pollo y los crotones de pan.\n" +
                                "4. Vinagreta: en un bol colocar el aceite y vinagre balsámico, batir en forma envolvente, agregarle sal, pimienta y perejil crespo desangrado, aderezar la ensalada con esta vinagreta.\n" +
                                "5. Servir acompañado de vino tinto o blanco.",
                                mutableListOf(
                                    Ingredient("lechugas verdes", 2.0),
                                    Ingredient("lechuga morada", 3.0),
                                    Ingredient("lechugas batavia", 4.0),
                                    Ingredient("jitomates cherry", 3.0),
                                    Ingredient("lechugas arugula", 2.0),
                                    Ingredient("lechugas romanas", 1.0),
                                    Ingredient("panes en rebanadas", 2.0),
                                    Ingredient("aceite de oliva", 5.0),
                                    Ingredient("pechugas de pollo", 15.0),
                                    Ingredient("perejiles chinos", 5.0),
                                    Ingredient("sal", 2.0),
                                    Ingredient("pimienta", 4.0)
                                ),
                                12
                            )
                        }
                        3 -> {
                            recipeDisplayed = Recipe(
                                recipeID,
                                BitmapFactory.decodeResource(resources, R.drawable.greek_salad),
                                "Ensalada griega",
                                "1. Corta la lechuga romana en trozos medianos y métela en un bowl.\n" +
                                    "2. Sin pelar el pepino, corta el pepino en cubos y agrégalos al bowl.\n" +
                                    "3. Corta el jitomate en rodajas gruesas y agrégalos al bowl.\n" +
                                    "4. Corta la cebolla morada en julianas y agrégalas.\n" +
                                    "5. Prepara el aderezo para la ensalada griega y después de revolver todos los " +
                                    "ingredientes junto con el aderezo, agréga el queso feta " +
                                    "(cortado en cubos) y las aceitunas negras.",
                                mutableListOf(
                                    Ingredient("lechuga romana", 1.0),
                                    Ingredient("pepino", 1.0),
                                    Ingredient("jitomate", 3.0),
                                    Ingredient("cebollas moradas", 0.5),
                                    Ingredient("queso feta", 250.0),
                                    Ingredient("aceituna negra", 125.0),
                                ),
                                6
                            )
                        }
                        4 -> {
                            recipeDisplayed = Recipe(
                                recipeID,
                                BitmapFactory.decodeResource(resources, R.drawable.greek_salad),
                                "Ensalada caprese",
                                "1. Ponga a hervir en un olla pequeña a fuego alto el vinagre balsámico por 5-10 minutos para que se vuelva en 1/4 de taza. (esto se llama reducir el vinagre)\n" +
                                "2. En un recipiente hondo mezcle la reducción de vinagre con sal y pimienta y agregue el aceite de oliva.\n" +
                                "3. En un bowl grande mezcle el queso, el jitomate y el aderezo.",
                                mutableListOf(
                                    Ingredient("jitomate cherry", 300.0),
                                    Ingredient("queso mozarella", 300.0),
                                    Ingredient("aceite de oliva", 125.0),
                                    Ingredient("vinagre balsamico", 125.0),
                                    Ingredient("hojas de albahaca", 125.0),
                                    Ingredient("pimienta", 125.0),
                                ),
                                10
                            )
                        }
                        5 -> {
                            recipeDisplayed = Recipe(
                                recipeID,
                                BitmapFactory.decodeResource(resources, R.drawable.caesar_salad),
                                "Ensalada Panzanella",
                                "1. Pre caliente el horno a 180 grados centígrados.\n" +
                                "2. En una charola para hornear coloque el pan en cubos y hornee por 10 " +
                                    "minutos o hasta que el pan esté ligeramente tostado.\n" +
                                "3. En un bowl grande combine los crutones con los pepinos, el jitomate, " +
                                    "la cebolla, las aceitunas y la albahaca.\n" +
                                "4. En un recipiente pequeño mezcle la vinagreta: el aceite, el vinagre, " +
                                    "el jugo de limón, el perejil, y el ajo.\n" +
                                "5. Agregue la vinagreta a la ensalada y mezcle bien.",
                                mutableListOf(
                                    Ingredient("lechugas verdes", 1250.0),
                                    Ingredient("aceite de oliva", 83.33),
                                    Ingredient("vinagre tinto", 83.33),
                                    Ingredient("jugo de limon", 45.0),
                                    Ingredient("perejil fresco", 45.0),
                                    Ingredient("ajo", 15.0),
                                    Ingredient("pepinos", 2.0),
                                    Ingredient("cebolla morada", 1.0),
                                    Ingredient("aceituna kalama", 83.33),
                                    Ingredient("albahaca", 83.33)
                                ),
                                6
                            )
                        }
                    }

                    setupComponents(recipeDisplayed!!)
                }
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
                    ingredientsList.add(Ingredient(ingredientName,amount.toDouble()))
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