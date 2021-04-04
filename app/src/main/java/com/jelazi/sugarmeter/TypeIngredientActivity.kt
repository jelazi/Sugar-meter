package com.jelazi.sugarmeter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.text.InputType
import android.view.WindowManager
import android.widget.*
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_ingredient.*


class TypeIngredientActivity : AppCompatActivity() {
    var typeIngredient: TypeIngredient? = null
    var isChangeName = false
    var isChangeValue = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingredient)


        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Úprava suroviny"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        typeIngredient = intent.extras?.get("ingredient") as TypeIngredient
        this.initItems()
    }


    fun initItems () {
        name_ingredient.text

        if (typeIngredient != null) {
            name_ingredient.text = (typeIngredient?.name)
            value_ingredient.text = (typeIngredient?.valueSugar.toString())
        }
        initListeners()
    }

    fun initListeners () {
        name_ingredient.setOnClickListener {
            changeName()
        }
        value_ingredient.setOnClickListener {
            changeValue()
        }
        btn_save_ingredient.setOnClickListener {
            saveIngredient()
        }
    }

    fun changeName() {

        val builder = AlertDialog.Builder(this@TypeIngredientActivity)
        builder.setTitle("Změna jména")
        builder.setMessage("Uložit toto jméno?")
        val input = EditText(this@TypeIngredientActivity)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        input.setText(name_ingredient.text)
        input.requestFocus()
        builder.setView(input)

        builder.setPositiveButton("ANO"){dialog, which ->
            val newName = input.text.toString()
            if (!typeIngredient!!.name.equals(newName)) {
                if (!TypeIngredientsManager.isSameName(newName)) {
                    name_ingredient.setText(input.text)
                    isChangeName = true
                } else {
                    Toast.makeText(this@TypeIngredientActivity, "Jméno se již používá v jiné položce.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        builder.setNeutralButton("Zrušit"){_,_ ->
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    fun changeValue() {
        val builder = AlertDialog.Builder(this@TypeIngredientActivity)
        builder.setTitle("Změna obsah cukru v 100 g")
        builder.setMessage("Uložit tento obsah cukru?")
        val input = EditText(this@TypeIngredientActivity)
        input.inputType = InputType.TYPE_CLASS_NUMBER
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        input.setText(value_ingredient.text)
        input.requestFocus()
        builder.setView(input)

        builder.setPositiveButton("ANO"){dialog, which ->
            isChangeValue = true
        }

        builder.setNeutralButton("Zrušit"){_,_ ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    fun saveIngredient () {
        if (name_ingredient.text.isEmpty()) {
            Toast.makeText(this@TypeIngredientActivity, "Pole jméno nesmí být prázdné.", Toast.LENGTH_SHORT).show()
            return
        }

        if (value_ingredient.text.isEmpty()) {
            Toast.makeText(this@TypeIngredientActivity, "Pole hodnota cukru nesmí být prázdná.", Toast.LENGTH_SHORT).show()
            return
        }

        if (isChangeName) {
            typeIngredient?.name = name_ingredient.text.toString()
                typeIngredient!!.name?.let {
                    typeIngredient!!.id?.let { it1 ->
                        TypeIngredientsManager.changeNameTypeIngredient(
                            it1,
                            it
                        )
                    }
                }
        }
        if (isChangeValue) {
            typeIngredient?.valueSugar = value_ingredient.text.toString().toDouble()
            typeIngredient!!.valueSugar?.let {
                typeIngredient!!.id?.let { it1 ->
                    TypeIngredientsManager.changeValueTypeIngredient(
                        it1,
                        it
                    )
                }
            }
        }
        val resultIntent = Intent()
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}