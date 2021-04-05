package com.jelazi.sugarmeter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    var cookBtn: Button? = null
    var changeIngredientsBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cookBtn = findViewById(R.id.cook_btn) as Button
        changeIngredientsBtn = findViewById(R.id.create_ingredients_btn) as Button

        cookBtn?.setOnClickListener {
            val intent = Intent (this, CookActivity::class.java)
            startActivity(intent)
        }
        changeIngredientsBtn?.setOnClickListener {
            val intent = Intent (this, TypeIngredientsListActivity::class.java)
            startActivity(intent)
        }
        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Měřič cukru"
        loadData()
    }
    private fun loadData() {
        TypeIngredientsManager.getListTypeIngredientsFromPreferences(this)
    }
}