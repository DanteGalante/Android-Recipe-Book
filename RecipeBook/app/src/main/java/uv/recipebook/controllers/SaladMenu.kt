package uv.recipebook.controllers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import uv.recipebook.R

class SaladMenu : AppCompatActivity() {
    lateinit var mixSaladBtn: LinearLayout
    lateinit var caesarSaladBtn: LinearLayout
    lateinit var greekSaladBtn: LinearLayout
    lateinit var capresaSaladBtn: LinearLayout
    lateinit var panzanellaSaladBtn: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salad_menu)

        setupComponents()
    }

    private fun setupComponents() {
        mixSaladBtn = findViewById(R.id.llayout_mixSalad)
        caesarSaladBtn = findViewById(R.id.llayout_caesarSalad)
        greekSaladBtn = findViewById(R.id.llayout_greekSalad)
        capresaSaladBtn = findViewById(R.id.llayout_capresaSalad)
        panzanellaSaladBtn = findViewById(R.id.llayout_panzanellaSalad)

        mixSaladBtn.setOnClickListener {
            val intent = Intent(this, RecipeDisplayMenu::class.java)
            intent.putExtra("option", 1)
            intent.putExtra("salad_id",1)
            startActivity(intent)
        }

        caesarSaladBtn.setOnClickListener {
            val intent = Intent(this, RecipeDisplayMenu::class.java)
            intent.putExtra("option", 1)
            intent.putExtra("salad_id",2)
            startActivity(intent)
        }

        greekSaladBtn.setOnClickListener {
            val intent = Intent(this, RecipeDisplayMenu::class.java)
            intent.putExtra("option", 1)
            intent.putExtra("salad_id",3)
            startActivity(intent)
        }

        capresaSaladBtn.setOnClickListener {
            val intent = Intent(this, RecipeDisplayMenu::class.java)
            intent.putExtra("option", 1)
            intent.putExtra("salad_id",4)
            startActivity(intent)
        }

        panzanellaSaladBtn.setOnClickListener {
            val intent = Intent(this, RecipeDisplayMenu::class.java)
            intent.putExtra("option", 1)
            intent.putExtra("salad_id",5)
            startActivity(intent)
        }
    }
}