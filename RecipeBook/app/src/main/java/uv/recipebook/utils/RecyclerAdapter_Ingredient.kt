package uv.recipebook.utils

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import uv.recipebook.data.Ingredient
import uv.recipebook.R

class RecyclerAdapter_Ingredient : RecyclerView.Adapter<RecyclerAdapter_Ingredient.ViewHolder_Ingredient>() {
    var ingredientList: MutableList<Ingredient> = ArrayList()
    lateinit var context: Context

    fun RecyclerAdapter_Ingredient(ingredients: MutableList<Ingredient>, context: Context){
        this.ingredientList = ingredients
        this.context = context
        notifyDataSetChanged()
    }

    fun addIngrediente(ingredient: Ingredient) {
        ingredientList.add(ingredient)
        notifyItemInserted(itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder_Ingredient {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_ingredient, parent, false)
        return ViewHolder_Ingredient(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder_Ingredient, position: Int) {
        val currentItem = ingredientList[position]
        holder.bind(currentItem, context)
        holder.delete(currentItem, position, context, this)
        holder.listenEdits(this)
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

    class ViewHolder_Ingredient(view: View) : RecyclerView.ViewHolder(view) {
        val ingredientName = view.findViewById<EditText>(R.id.et_ingredientName)
        val amountGr = view.findViewById<EditText>(R.id.et_amountGr)
        val delete = view.findViewById<ImageButton>(R.id.ib_deleteIngredient)

        fun bind(ingredient: Ingredient, context: Context) {
            ingredientName.setText(ingredient.ingredient)
            amountGr.setText(ingredient.amountInGr.toString())
        }

        fun listenEdits(adapter: RecyclerAdapter_Ingredient) {
            ingredientName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    adapter.ingredientList[adapterPosition].ingredient = s.toString()
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })
            amountGr.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(s!!.isBlank()) {
                        adapter.ingredientList[adapterPosition].amountInGr = 0.0
                    } else {
                        adapter.ingredientList[adapterPosition].amountInGr = s.toString().toDouble()
                    }

                    println("id = $adapterPosition")
                    println("amount in gr = ${adapter.ingredientList[adapterPosition].amountInGr}")
                    println("ingredient list:")
                    println(adapter.ingredientList)
                }

                override fun afterTextChanged(s: Editable?) {
                    println("AFTER TEXT CHANGED")
                    println("id = $adapterPosition")
                    //adapter.notifyItemInserted(adapterPosition)
                }

            })
        }

        fun delete(ingredient: Ingredient, position: Int, context: Context, adapter: RecyclerAdapter_Ingredient) {
            delete.setOnClickListener {
                Toast.makeText(context, "Ingrediente eliminado",Toast.LENGTH_SHORT).show()
                adapter.ingredientList.remove(ingredient)
                adapter.notifyItemRemoved(position)
                adapter.notifyDataSetChanged()
            }
        }
    }
}