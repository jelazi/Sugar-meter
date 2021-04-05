package com.jelazi.sugarmeter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.WindowManager
import android.widget.*
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.jelazi.sugarmeter.R.color
import kotlinx.android.synthetic.main.activity_ingredient.*


@Suppress("DEPRECATION")
class TypeIngredientActivity : AppCompatActivity() {
    var typeIngredient: TypeIngredient? = null
    var isChangeName = false
    var isChangeValue = false
    var valueSugar : Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingredient)


        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Úprava suroviny"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        if (intent.extras?.get("ingredient") != null) {
            typeIngredient = intent.extras?.get("ingredient") as TypeIngredient
        }
        this.initItems()
        reloadActivity()
    }


    fun initItems () {
        name_ingredient.text

        if (typeIngredient != null) {
            name_ingredient.text = (typeIngredient?.name)
            value_ingredient.text = (typeIngredient?.valueSugar.toString())
        } else {
            typeIngredient = TypeIngredientsManager.getNewTypeIngredient("", 0.0)
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


    fun reloadActivity() {
        if (typeIngredient?.valueSugar == 0.0) {
            value_ingredient.setTextColor(this.getResources().getColor(color.gray))
        } else {
            value_ingredient.setTextColor(this.getResources().getColor(color.black))
        }
        if (typeIngredient?.name == "") {
            name_ingredient.setTextColor(this.getResources().getColor(color.gray))
        } else {
            name_ingredient.setTextColor(this.getResources().getColor(color.black))
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
        if (typeIngredient?.name =="") {
            input.setText("Surovina")
        } else {
            input.setText(typeIngredient?.name)
        }
        input.setSelectAllOnFocus(true)
        input.requestFocus()
        builder.setView(input)

        builder.setPositiveButton("ANO"){ dialog, which ->
            val newName = input.text.toString()
                if (!TypeIngredientsManager.isSameName(newName)) {
                    name_ingredient.setText(input.text)
                    typeIngredient?.name = input.text.toString()
                    isChangeName = true
                    reloadActivity()
                } else {
                    Toast.makeText(this@TypeIngredientActivity, "Jméno se již používá v jiné položce.", Toast.LENGTH_SHORT).show()
                }
        }

        builder.setNeutralButton("Zrušit"){ _, _ ->
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
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        var text:String = value_ingredient.text.toString()

        text = text.replace(" g/100g", "")
        input.setText(text)
        input.setSelectAllOnFocus(true)
        input.requestFocus()
        builder.setView(input)

        builder.setPositiveButton("ANO"){ dialog, which ->
            isChangeValue = true
            valueSugar = input.text.toString().toDouble()
            typeIngredient?.valueSugar = input.text.toString().toDouble()
            value_ingredient.setText(input.text.toString() + " g/100g")
            reloadActivity()

        }

        builder.setNeutralButton("Zrušit"){ _, _ ->
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
        }
        if (isChangeValue) {
            typeIngredient?.valueSugar = valueSugar
        }
        if (typeIngredient != null && typeIngredient?.isCorrect() == true) {
            TypeIngredientsManager.addIngredient(typeIngredient)
            TypeIngredientsManager.setListTypeIngredientsToPreferences(this)
        }


        val resultIntent = Intent()
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    companion object {
        @JvmField
        val name = ""
        val value = 0.0
    }
}