package com.jelazi.sugarmeter

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_food.*
import kotlinx.android.synthetic.main.activity_ingredient.*

class FoodActivity : AppCompatActivity() {
    var food : Food? = null
    var nameFood : String = ""
    var sumWeightFood: Double = 0.0
    var sumWeightSugar: Double = 0.0
    var weightPartFood: Double = 0.0

    var sumWeightFoodTextView: TextView? = null
    var sumWeightSugarTextView: TextView? = null
    var btnPartFood: Button? = null
    var weightSugarPartFoodTextView: TextView? = null
    var headFoodLayout: LinearLayout? = null
    var separator1: View? = null
    var separator2: View? = null

    var info = ArrayList<HashMap<String, String>>()
    var listView: ListView? = null
    var addIngredientFloatBtn: FloatingActionButton? = null
    var textViewName: TextView? = null
    var foodListAdapter: FoodListAdapter? = null
    var sumVisibility = View.INVISIBLE
    var weightSugarPartVisibility = View.INVISIBLE
    private val CHOICE_INGREDIENT = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Zadání jídla"
        actionbar.setDisplayHomeAsUpEnabled(true)
        listView = findViewById(R.id.listViewIngredient) as ListView

        headFoodLayout = findViewById(R.id.head_part_food_item)
        sumWeightFoodTextView = findViewById(R.id.sum_food)
        sumWeightSugarTextView = findViewById(R.id.sum_suggar)
        addIngredientFloatBtn = findViewById(R.id.floatingActionButtonAddIngredient)
        btnPartFood = findViewById(R.id.btn_part_food)

        weightSugarPartFoodTextView = findViewById(R.id.weight_sugar_part_food)
        separator1 = findViewById(R.id.separator1)
        separator2 = findViewById(R.id.separator2)

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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if (food?.isCorrect() != true) {
            super.onBackPressed()
            return
        }
        val builder = AlertDialog.Builder(this@FoodActivity)
        builder.setTitle("Neuložené jídlo")
        builder.setMessage("Chcete jídlo: "+ food?.name.toString() + " uložit?")

        builder.setPositiveButton("Ano"){ dialog2, which ->
            FoodManager.addFood(food!!)
            super.onBackPressed()
        }

        builder.setNeutralButton("Ne"){ dialog2, which ->
            super.onBackPressed()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

    }


    fun reloadActivity() {
        createHashMap()
        foodListAdapter = FoodListAdapter(this, info)
        listView?.adapter = (foodListAdapter)
        sumResult()
        sumWeightSugarTextView?.setText("Celkem cukru: " + "%.2f".format(sumWeightSugar) + " g")
        sumWeightFoodTextView?.setText("Váha jídla: " + "%.2f".format(sumWeightFood) + " g")


        if (sumWeightFood == 0.0){
            sumVisibility = View.INVISIBLE
        } else{
            sumVisibility = View.VISIBLE
        }
        if (weightPartFood == 0.0) {
            weightSugarPartVisibility = View.INVISIBLE
        } else {
            weightSugarPartVisibility = View.VISIBLE
        }
        headFoodLayout?.visibility = sumVisibility
        sumWeightSugarTextView?.visibility = sumVisibility
        sumWeightFoodTextView?.visibility = sumVisibility
        btnPartFood?.visibility = sumVisibility
        separator1?.visibility = sumVisibility
        separator2?.visibility = sumVisibility
        weightSugarPartFoodTextView?.visibility = weightSugarPartVisibility
        weight_sugar_part_food.setText("Cukru v porci: " + "%.2f".format(food?.weightSugarInPartFood(weightPartFood)) + " g")

        if (nameFood.isEmpty()) {
            name_food_text_view.setTextColor(this.getResources().getColor(R.color.gray))
        } else {
            name_food_text_view.setTextColor(this.getResources().getColor(R.color.black))
        }
    }

    fun setWeightPartFood() {
        val builder = AlertDialog.Builder(this@FoodActivity)
        builder.setTitle("Porce jídla")
        builder.setMessage("Zadejte množství porce: ")
        val input = EditText(this@FoodActivity)
        input.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp

        input.setText(weightPartFood.toString())
        input.setSelectAllOnFocus(true)
        input.requestFocus()
        builder.setView(input)

        builder.setPositiveButton("ANO"){ dialog, which ->
            weightPartFood = input.text.toString().toDouble()
            if (weightPartFood != 0.0) {
                btnPartFood?.setText("Porce: " + "%.2f".format(weightPartFood) + " g")
                weight_sugar_part_food.setText("Cukru v porci: " + "%.2f".format(food?.weightSugarInPartFood(weightPartFood)) + " g")
                reloadActivity()
            } else {
                Toast.makeText(this@FoodActivity, "Pole hodnota nesmí být nula.", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNeutralButton("Zrušit"){ _, _ ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

    }

    fun sumResult () {
        sumWeightFood = 0.0
        sumWeightSugar = 0.0
        if (food != null && !food?.listIngredients?.isEmpty()!!) {
            sumWeightFood = food?.sumWeightFood()!!
            sumWeightSugar = food?.sumWeightSugar()!!
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
                var sugarWeight: Double = food?.listIngredients!![i].getWeightSugar()
                var sugarWeghtRound: String = "%.2f".format(sugarWeight)
                hashMap.put("valueSugar", sugarWeghtRound)
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
        btnPartFood?.setOnClickListener {
            setWeightPartFood()
        }

        listView?.setOnItemLongClickListener(object : AdapterView.OnItemLongClickListener {
            override fun onItemLongClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long): Boolean {
                val ingredient = food?.getIngredientByName(info[i].get("name").toString())
                if (ingredient != null)
                    alertDialogChoiceActivity(ingredient)
                return true
            }
        })

    }
    private fun alertDialogChoiceActivity(ingredient: Ingredient) {
        val builder = AlertDialog.Builder(this@FoodActivity)
        builder.setTitle("Výběr možnosti")
        builder.setMessage("Co chcete s touto surovinou udělat?")

        builder.setPositiveButton("Změnit množství"){ dialog, which ->
            changeWeightIngredient(ingredient)
        }

        builder.setNeutralButton("Odstranit z jídla"){ dialog, which ->
            deleteIngredientAlert(ingredient)
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }


    private fun deleteIngredientAlert(ingredient: Ingredient) {
        val builder2 = AlertDialog.Builder(this@FoodActivity)
        builder2.setTitle("Odstranění suroviny z jídla")
        builder2.setMessage("Opravdu chcete surovinu: " + ingredient.name + " vymazat?")

        builder2.setPositiveButton("Ano"){ dialog2, which ->
            food?.deleteIngredient(ingredient)
            reloadActivity()
        }

        builder2.setNeutralButton("Ne"){ dialog2, which ->

        }

        val dialog2: AlertDialog = builder2.create()
        dialog2.show()
        dialog2.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
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
            reloadActivity()
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
        var listId = food?.getUsableTypeIntents()
        intent.putIntegerArrayListExtra("listId", listId)
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

    fun changeWeightIngredient(ingredient: Ingredient) {
        val builder = AlertDialog.Builder(this@FoodActivity)
        val nameTypeIngredient = ingredient.name
        var weight = ingredient.weight
        builder.setTitle("Změnit množství suroviny")
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
                ingredient.changeWeight(weight)
                reloadActivity()
            } else {
                Toast.makeText(this@FoodActivity, "Pole hodnota nesmí být nula.", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNeutralButton("Zrušit"){ _, _ ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
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