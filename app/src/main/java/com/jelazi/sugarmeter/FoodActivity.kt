package com.jelazi.sugarmeter

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_ingredient.*

class FoodActivity : AppCompatActivity() {
    var food : Food? = null
    var nameFood : String = ""
    var sum: Double = 0.0
    var sumTextView: TextView? = null
    var info = ArrayList<HashMap<String, String>>()
    var listView: ListView? = null
    var addIngredientFloatBtn: FloatingActionButton? = null
    var textViewName: TextView? = null
    var foodListAdapter: FoodListAdapter? = null
    private val CHOICE_INGREDIENT = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)
        listView = findViewById(R.id.listViewIngredient) as ListView
        sumTextView = findViewById(R.id.sum_suggar)
        addIngredientFloatBtn = findViewById(R.id.floatingActionButtonAddIngredient)
        textViewName = findViewById(R.id.name_food_text_view)
        if (nameFood.isEmpty()) {
            textViewName?.setText("Zadejte jméno jídla")
        } else {
            textViewName?.setText(nameFood)
        }
        initListeners()
        reloadActivity()
    }

    override fun onResume() {
        super.onResume()
        reloadActivity()
    }


    fun reloadActivity() {
        createHashMap()
        foodListAdapter = FoodListAdapter(this, info)
        listView?.adapter = (foodListAdapter)
        sumResult()
        sumTextView?.setText("Celkem cukru: " + sum.toString() + "g")
        sumTextView?.visibility = if (sum == 0.0){
            View.INVISIBLE
        } else{
            View.VISIBLE
        }
    }

    fun sumResult () {
        sum = 0.0
        if (food != null && !food?.listIngredients?.isEmpty()!!) {
            for (ingredients in food?.listIngredients!!) {
                sum += ingredients.weight
            }
        }
    }


    private fun createHashMap () {
        var hashMap: HashMap<String, String> = HashMap<String, String>()
        info = ArrayList<HashMap<String, String>>()
        if (food != null && !food?.listIngredients?.isEmpty()!!) {
            for (i in 0..food?.listIngredients?.size!! - 1) {
                hashMap = HashMap()
                food?.listIngredients!![i].name?.let { hashMap.put("name", it) }
                food?.listIngredients!![i].weight?.let { hashMap.put("weight", it.toString()) }
                info.add(hashMap)
            }
        }


    }



    private fun initListeners() {
        textViewName?.setOnClickListener {
            changeName(false)
        }
        addIngredientFloatBtn?.setOnClickListener {
            addIngredient()
        }
    }

    private fun changeName (isFromAddIngredient: Boolean) {
        val builder = AlertDialog.Builder(this@FoodActivity)
        builder.setTitle("Zadejte jméno jídla")
        builder.setMessage("Uložit toto jméno?")
        val input = EditText(this@FoodActivity)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        input.setText(nameFood)
        input.requestFocus()
        builder.setView(input)

        builder.setPositiveButton("ANO") { dialog, which ->
            if (input.text.toString().isNullOrEmpty()) {
                Toast.makeText(this@FoodActivity, "Pole jméno nesmí být prázdné.", Toast.LENGTH_SHORT).show()
            } else {
                nameFood = input.text.toString()
                if (food == null) {
                    food = Food(nameFood)
                }
                textViewName?.setText(nameFood)
                if (isFromAddIngredient) {
                    addIngredient()
                }
            }
        }
        builder.setNeutralButton("Zrušit"){_,_ ->
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    private fun addIngredient() {
        if (nameFood == "") {
            changeName(true)
            return
        }
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
            if (typeIngredient != null) {
                addIngredientDialog(typeIngredient)
            } else {
                Toast.makeText(this@FoodActivity, "Pole jméno nesmí být prázdné.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun addIngredientDialog(typeIngredient: TypeIngredient) {
        val builder = AlertDialog.Builder(this@FoodActivity)
        val nameTypeIngredient = typeIngredient.name
        var weight = 0.0
        builder.setTitle("Množství suroviny")
        builder.setMessage("Zadejte množství suroviny: " + nameTypeIngredient)
        val input = EditText(this@FoodActivity)
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp

        input.setText(weight.toString())
        input.setSelectAllOnFocus(true)
        input.requestFocus()
        builder.setView(input)

        builder.setPositiveButton("ANO"){ dialog, which ->
            weight = input.text.toString().toDouble()
            if (weight != 0.0) {
                var ingredient = Ingredient(typeIngredient, weight)
                food?.addIngredient(ingredient)
                reloadActivity()
            } else {
                Toast.makeText(this@FoodActivity, "Pole hodnota nesmí být nulová.", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNeutralButton("Zrušit"){ _, _ ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }
}