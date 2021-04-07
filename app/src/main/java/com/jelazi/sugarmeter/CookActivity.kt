package com.jelazi.sugarmeter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_ingredient.*

class CookActivity : AppCompatActivity() {
    var food : Food? = null
    var nameFood : String = ""
    var sum: Double = 0.0
    var listView: ListView? = null
    var addIngredientFloatBtn: FloatingActionButton? = null
    var textViewName: TextView? = null
    private val CHOICE_INGREDIENT = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cook)
        listView = findViewById(R.id.listViewIngredient) as ListView
        addIngredientFloatBtn = findViewById(R.id.floatingActionButtonAddIngredient)
        textViewName = findViewById(R.id.name_food_text_view)
        if (nameFood.isEmpty()) {
            textViewName?.setText("Zadejte jméno jídla")
        } else {
            textViewName?.setText(nameFood)
        }
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
            textViewName?.setText(nameFood)
        }
        builder.setNeutralButton("Zrušit"){_,_ ->
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    private fun addIngredient() {
        val intent = Intent(this, TypeIngredientsListActivity::class.java)
        intent.putExtra("typeIntent", "choice")
        startActivityForResult(intent, CHOICE_INGREDIENT)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOICE_INGREDIENT && resultCode == RESULT_OK) {
            val name = data?.getStringExtra("name")
            val id = data?.getStringExtra("id").toString().toInt()
            val typeIngredient: TypeIngredient? = TypeIngredientsManager.getTypeIngredientById(id)
            Log.d("TAG", typeIngredient.toString())
            if (typeIngredient != null) {
                var ingredient: Ingredient = Ingredient(typeIngredient)
                food?.addIngredient(ingredient)
            }
        }
    }
}