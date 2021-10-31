package uv.recipebook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.title = null

        val btn_salad = findViewById<LinearLayout>(R.id.llayout_salad)
        btn_salad.setOnClickListener {
            val intent = Intent(this,SaladMenu::class.java)
            startActivity(intent)
        }
        val btn_custom = findViewById<LinearLayout>(R.id.llayout_customRecipe)
        btn_custom.setOnClickListener {
            val intent = Intent(this, CRUD_RecipesMenu::class.java)
            startActivity(intent)
        }
    }
}