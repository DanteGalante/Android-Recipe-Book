package uv.recipebook.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import uv.recipebook.data.Ingredient
import uv.recipebook.R

class RecyclerAdapter_Ingredient : RecyclerView.Adapter<RecyclerAdapter_Ingredient.ViewHolder_Ingredient>() {
    var ingredientList: MutableList<Ingredient> = ArrayList()
    lateinit var context: Context

    fun addIngrediente(ingredient: Ingredient) {
        ingredientList.add(ingredient)
        notifyItemInserted(itemCount)
    }

    fun RecyclerAdapter_Ingredient(ingredients: MutableList<Ingredient>, context: Context){
        this.ingredientList = ingredients
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder_Ingredient {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_ingredient, parent, false)
        return ViewHolder_Ingredient(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder_Ingredient, position: Int) {
        val currentItem = ingredientList[position]
        holder.bind(currentItem, context)

        println("\n\n${ingredientList}\n\n")

        holder.itemView.findViewById<ImageButton>(R.id.ib_deleteIngredient).setOnClickListener {
            Toast.makeText(context, "Ingrediente eliminado",Toast.LENGTH_SHORT).show()
            ingredientList.remove(currentItem)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

    class ViewHolder_Ingredient(view: View) : RecyclerView.ViewHolder(view) {
        val ingredientName = view.findViewById<EditText>(R.id.et_ingredientName)
        val amountGr = view.findViewById<EditText>(R.id.et_amountGr)

        fun bind(ingredient: Ingredient, context: Context) {
            ingredientName.setText(ingredient.ingredient)
            amountGr.setText(ingredient.amountInGr.toString())
        }
    }
}