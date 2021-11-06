package uv.recipebook

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import uv.recipebook.controllers.CreateRecipeMenu
import uv.recipebook.controllers.EditRecipeMenu
import uv.recipebook.controllers.RecipeDisplayMenu
import uv.recipebook.data.DBAdmin
import uv.recipebook.data.Recipe

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    var recipes: MutableList<Recipe> = ArrayList()
    lateinit var context: Context
    lateinit var dbAdmin: DBAdmin

    fun RecyclerAdapter(recipes: MutableList<Recipe>, context: Context, dbAdmin: DBAdmin) {
        this.recipes = recipes
        this.context = context
        this.dbAdmin = dbAdmin
        notifyDataSetChanged()
    }

    fun addIngrediente(recipe: Recipe) {
        recipes.add(recipe)
        notifyItemInserted(itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_recipe,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        val item = recipes[position]
        holder.bind(item, context)
        holder.delete(recipes[position], position, context, this, dbAdmin)
        holder.goToEdit(recipes[position], context)
        holder.goToDisplayMenu(recipes[position], context)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recipeTitle = view.findViewById<TextView>(R.id.tv_recipeTitle)
        val image = view.findViewById<ImageView>(R.id.iv_recipe)
        val btn_delete = view.findViewById<ImageButton>(R.id.ib_deleteRecipe)
        val btn_edit = view.findViewById<ImageButton>(R.id.ib_editRecipe)
        val recipeDisplay = view.findViewById<LinearLayout>(R.id.llayout_customRecipeCRUD)

        fun bind(recipe: Recipe, context: Context) {
            recipeTitle.text = recipe.title
            image.setImageBitmap(recipe.image)
        }

        fun delete(
            recipe: Recipe,
            position: Int,
            context: Context,
            recyclerAdapter: RecyclerAdapter,
            dbAdmin: DBAdmin
        ) {
            btn_delete.setOnClickListener {
                recyclerAdapter.recipes.remove(recipe)
                recyclerAdapter.notifyItemRemoved(position)
                recyclerAdapter.notifyDataSetChanged()

                try {

                    println(dbAdmin.databaseName)
                    println("id = ${recipe.id}")

                    val db = dbAdmin.writableDatabase

                    val numDeleteRows = db.delete(
                        "recetas",
                        "id = '${recipe.id}'",
                        //null,
                        null
                    )

                    db.close()

                    if(numDeleteRows >= 1) {
                        Toast.makeText(context, "Ingrediente eliminado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            context,
                            "No se encontro la receta en la base de datos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (sqlException: SQLiteException) {

                }
            }
        }

        fun goToEdit(recipe: Recipe, context: Context) {
            btn_edit.setOnClickListener {
                val intent = Intent(context, EditRecipeMenu::class.java)
                intent.putExtra("recipe_edit_id",recipe.id)
                startActivity(context, intent, null)
            }
        }

        fun goToDisplayMenu(recipe: Recipe, context: Context) {
            recipeDisplay.setOnClickListener {
                val intent = Intent(context, RecipeDisplayMenu::class.java)
                intent.putExtra("recipe_display_id", recipe.id)
                startActivity(context, intent, null)
            }
        }
    }
}