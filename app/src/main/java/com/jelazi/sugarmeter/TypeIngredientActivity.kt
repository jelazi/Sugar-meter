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
    var typeIntent = ""
    var isChangeName = false
    var isChangeValue = false
    var valueSugar : Double = 0.0



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingredient)


        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = resources.getString(R.string.type_ingredient_activity_title)
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        if (intent.extras?.get("typeIntent") != null) {
            typeIntent = intent.getStringExtra("typeIntent").toString()
            if (typeIntent == "edit") {
                if (intent.extras?.get("typeIngredient") != null) {
                    var idIngredient = intent.getStringExtra("typeIngredient").toString().toInt()
                    typeIngredient = TypeIngredientsManager.getTypeIngredientById(idIngredient)
                }
            }
        }
        this.initItems()
        reloadActivity()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    override fun onBackPressed() {
        if (!isChangeName && !isChangeValue) {
            super.onBackPressed()
            return
        }
        if (typeIngredient?.isCorrect() != true) {
            super.onBackPressed()
            return
        }
        val builder = AlertDialog.Builder(this@TypeIngredientActivity)
        builder.setTitle("Uložení suroviny")
        builder.setMessage("Chcete surovinu: " + typeIngredient?.name.toString() + resources.getString(R.string.save_food_question_part_two))

        builder.setPositiveButton(resources.getString(R.string.yes)){ dialog2, which ->
            saveIngredient()
            super.onBackPressed()
        }

        builder.setNeutralButton(resources.getString(R.string.no)){ dialog2, which ->
            super.onBackPressed()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

    }


    fun initItems () {
        name_ingredient.text

        if (typeIngredient != null) {
            name_ingredient.text = (typeIngredient?.name)
            value_ingredient.text = (typeIngredient?.valueSugar.toString() + resources.getString(R.string.gram_to_houndred_gram))
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
        builder.setTitle(resources.getString(R.string.set_name_ingredient))
        val input = EditText(this@TypeIngredientActivity)
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        if (typeIngredient?.name =="") {
            input.setText(resources.getString(R.string.ingredient))
        } else {
            input.setText(typeIngredient?.name)
        }
        input.setSelectAllOnFocus(true)
        input.requestFocus()
        builder.setView(input)

        builder.setPositiveButton(resources.getString(R.string.save)){ dialog, which ->
            val newName = input.text.toString()
                if (!TypeIngredientsManager.isSameName(newName)) {
                    typeIngredient?.name = newName
                    name_ingredient.setText(newName)
                    isChangeName = true
                    reloadActivity()
                } else {
                    Toast(this).showCustomToast (resources.getString(R.string.name_is_same),this)

                }
        }

        builder.setNeutralButton(resources.getString(R.string.cancel)){ _, _ ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }


    fun changeValue() {
        val builder = AlertDialog.Builder(this@TypeIngredientActivity)
        builder.setTitle(resources.getString(R.string.change_value_sugar))
        val input = EditText(this@TypeIngredientActivity)
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        var text:String = value_ingredient.text.toString()

        text = text.replace(resources.getString(R.string.gram_to_houndred_gram), "")
        input.setText(text)
        input.setSelectAllOnFocus(true)
        input.requestFocus()
        builder.setView(input)

        builder.setPositiveButton(resources.getString(R.string.save)){ dialog, which ->
            isChangeValue = true
            valueSugar = input.text.toString().toDouble()
            typeIngredient?.valueSugar = input.text.toString().toDouble()
            value_ingredient.setText(input.text.toString() + resources.getString(R.string.gram_to_houndred_gram))
            reloadActivity()

        }

        builder.setNeutralButton(resources.getString(R.string.cancel)){ _, _ ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    fun saveIngredient () {
        if (typeIngredient?.name.isNullOrEmpty()) {
            Toast(this).showCustomToast (resources.getString(R.string.field_name_empty),this)
            return
        }

        if (typeIngredient?.valueSugar == null || typeIngredient?.valueSugar == 0.0) {
            Toast(this).showCustomToast (resources.getString(R.string.field_value_empty),this)
            return
        }

        if (isChangeName) {
            typeIngredient?.name = name_ingredient.text.toString()
        }
        if (isChangeValue) {
            typeIngredient?.valueSugar = valueSugar
        }
        val resultIntent = Intent()
        if (typeIngredient != null && typeIngredient?.isCorrect() == true) {
        if (typeIntent == "edit" && typeIngredient != null ) {
            typeIngredient?.id?.let { typeIngredient?.name?.let { it1 -> TypeIngredientsManager.changeIngredient(it, it1, typeIngredient?.valueSugar!!) } }
        } else {
                TypeIngredientsManager.addIngredient(typeIngredient)
            }
        }
        TypeIngredientsManager.setListTypeIngredientsToPreferences(this)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}