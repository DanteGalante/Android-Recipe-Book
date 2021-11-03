package uv.recipebook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uv.recipebook.data.Recipe

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    var recipes: MutableList<Recipe> = ArrayList()
    lateinit var context: Context

    fun RecyclerAdapter(recipes : MutableList<Recipe>, context: Context) {
        this.recipes = recipes
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_recipe,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        val item = recipes.get(position)
        holder.bind(item, context)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recipeTitle = view.findViewById<TextView>(R.id.tv_recipeTitle)
        val image = view.findViewById<ImageView>(R.id.iv_recipe)

        fun bind(recipe: Recipe, context: Context) {
            recipeTitle.text = recipe.title
            //itemView.setOnClickListener(View.OnClickListener { Toast.makeText(context, recipe.recipe, Toast.LENGTH_SHORT).show() })
            image.setImageBitmap(recipe.image)
        }
    }
}