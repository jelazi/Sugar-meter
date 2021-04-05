package com.jelazi.sugarmeter

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_ingredient.*

class CookActivity : AppCompatActivity() {
    var food : Food? = null
    var nameFood : String? = ""
    var listView: ListView? = null
    var addIngredientFloatBtn: FloatingActionButton? = null
    var textViewName: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cook)
        listView = findViewById(R.id.listViewIngredient) as ListView
        addIngredientFloatBtn = findViewById(R.id.floatingActionButtonAddIngredient)
        textViewName = findViewById(R.id.name_food_text_view)
        nameFood = "Jídlo 1"
        nameFood.let { textViewName?.setText(it) }
        initListeners()
    }

    private fun initListeners() {
        textViewName?.setOnClickListener {
            changeName()
        }
        addIngredientFloatBtn?.setOnClickListener {
            addIngredient()
        }
    }

    private fun changeName () {
        val builder = AlertDialog.Builder(this@CookActivity)
        builder.setTitle("Změna jména jídla")
        builder.setMessage("Uložit toto jméno?")
        val input = EditText(this@CookActivity)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        input.setText(nameFood)
        input.requestFocus()
        builder.setView(input)

        builder.setPositiveButton("ANO") { dialog, which ->
            nameFood = input.text.toString()
            nameFood.let { textViewName?.setText(it) }
            initListeners()
        }
        builder.setNeutralButton("Zrušit"){_,_ ->
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    private fun addIngredient() {
        val intent = Intent(this, TypeIngredientsListActivity::class.java)
        startActivity(intent)
    }
}