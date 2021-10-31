package uv.recipebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CRUD_RecipesMenu : AppCompatActivity() {
    //private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_recipes_menu)

        //linearLayoutManager = LinearLayoutManager(this)

        //recipes.layoutManager = linearLayoutManager
    }

    fun setUpRecyclerView() {
        var recipes = findViewById<RecyclerView>(R.id.rv_recipes)
        if (recipes.isEmpty()) {
            var tv_dinamyc = TextView(this)
            tv_dinamyc.textSize = 20f
            tv_dinamyc.text = "No hay recetas personalizadas. ¡Crea una! :)"
            //recipes.addView(tv_dinamyc)
        }
    }

    //fun getRecipes() :
}