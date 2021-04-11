package com.jelazi.sugarmeter

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_food.*
import kotlinx.android.synthetic.main.activity_ingredient.*
import kotlin.properties.Delegates

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
    var sugarInOneGramTextView: TextView? = null
    var headFoodLayout: LinearLayout? = null
    var separator1: View? = null
    var separator2: View? = null

    var info = ArrayList<HashMap<String, String>>()
    var listView: ListView? = null
    var addIngredientFloatBtn: FloatingActionButton? = null
    var textViewName: TextView? = null
    var ingredientsListAdapter: IngredientsListAdapter? = null
    var sumVisibility = View.INVISIBLE
    var weightSugarPartVisibility = View.INVISIBLE
    private val CHOICE_INGREDIENT = 1
    var typeIntent = ""
    var isChange :Boolean by Delegates.observable(false) { prop, old, new ->
        isChangeValue()
    }
    val listen : MutableLiveData<Boolean> =  MutableLiveData()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)
        if (intent.getStringExtra("typeIntent") != null) {
            typeIntent = intent.getStringExtra("typeIntent").toString()
            if (typeIntent == "edit") {
                if (intent.getStringExtra("id") != null) {
                    val id = intent.getStringExtra("id").toString().toInt()
                    var f = FoodsManager.getFoodById(id)
                    if (f != null) {
                        food = f
                        nameFood = food?.name.toString()
                    }
                }
            }
        }


        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = resources.getString(R.string.food_activity_title)
        actionbar.setDisplayHomeAsUpEnabled(true)
        listView = findViewById(R.id.listViewIngredient) as ListView

        headFoodLayout = findViewById(R.id.head_part_food_item)
        sumWeightFoodTextView = findViewById(R.id.sum_food)
        sumWeightSugarTextView = findViewById(R.id.sum_suggar)
        addIngredientFloatBtn = findViewById(R.id.floatingActionButtonAddIngredient)
        sugarInOneGramTextView = findViewById(R.id.sugar_in_one_gram)
        btnPartFood = findViewById(R.id.btn_part_food)

        weightSugarPartFoodTextView = findViewById(R.id.weight_sugar_part_food)
        separator1 = findViewById(R.id.separator1)
        separator2 = findViewById(R.id.separator2)

        textViewName = findViewById(R.id.name_food_text_view)
        if (nameFood.isEmpty()) {
            textViewName?.setText(resources.getString(R.string.set_food_name))
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
        if (!isChange) {
            super.onBackPressed()
            return
        }
        if (food?.isCorrect() != true) {
            super.onBackPressed()
            return
        }
        val builder = AlertDialog.Builder(this@FoodActivity)
        builder.setTitle(resources.getString(R.string.save_food_title))
        builder.setMessage(resources.getString(R.string.save_food_question_part_one) + food?.name.toString() + resources.getString(R.string.save_food_question_part_two))

        builder.setPositiveButton(resources.getString(R.string.yes)){ dialog2, which ->
            changeFood()
            super.onBackPressed()
        }

        builder.setNeutralButton(resources.getString(R.string.no)){ dialog2, which ->
            super.onBackPressed()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.food_menu, menu)


        return true
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_food_item -> {
                changeFood()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }



    private fun isChangeValue() {
        weightPartFood = 0.0
        reloadActivity()
    }

    fun changeFood() {
        if (typeIntent == "edit") {
            FoodsManager.changeFood(food!!)
        } else {
            FoodsManager.addFood(food!!)
        }
        FoodsManager.setListFoodsToPreferences(this)
        isChange = false
    }


    fun reloadActivity() {
        createHashMap()
        ingredientsListAdapter = IngredientsListAdapter(this, info)
        listView?.adapter = (ingredientsListAdapter)
        sumResult()
        sumWeightSugarTextView?.setText(resources.getString(R.string.sum_sugar) + "%.2f".format(sumWeightSugar) + resources.getString(R.string.g_item))
        sumWeightFoodTextView?.setText(resources.getString(R.string.weight_food) + "%.2f".format(sumWeightFood) + resources.getString(R.string.g_item))


        if (sumWeightFood == 0.0){
            sumVisibility = View.INVISIBLE
        } else{
            sumVisibility = View.VISIBLE
        }
        if (weightPartFood == 0.0) {
            weightSugarPartVisibility = View.INVISIBLE
            btnPartFood?.setText("Porce")
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
        sugarInOneGramTextView?.visibility = weightSugarPartVisibility
        weightSugarPartFoodTextView?.setText(resources.getString(R.string.sugar_in_part) + "%.2f".format(food?.weightSugarInPartFood(weightPartFood)) + resources.getString(R.string.g_item))
        sugarInOneGramTextView?.setText(resources.getString(R.string.sugar_one_gramm_food) + "%.2f".format(food?.sugarInOneGramFood()) + resources.getString(R.string.g_item))

        if (nameFood.isEmpty()) {
            name_food_text_view.setTextColor(this.getResources().getColor(R.color.gray))
        } else {
            name_food_text_view.setTextColor(this.getResources().getColor(R.color.black))
        }
    }

    fun setWeightPartFood() {
        val builder = AlertDialog.Builder(this@FoodActivity)
        val weight = food?.sumWeightFood()
        builder.setTitle(resources.getString(R.string.part_food))
        builder.setMessage(resources.getString(R.string.set_part_food) + weight.toString() + resources.getString(R.string.g_item) + ")")
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

        builder.setPositiveButton(resources.getString(R.string.yes)){ dialog, which ->
            weightPartFood = input.text.toString().toDouble()
            if (weightPartFood != 0.0) {
                if (weightPartFood > weight!!) {
                    Toast.makeText(this@FoodActivity, resources.getString(R.string.warning_part_is_bigger), Toast.LENGTH_SHORT).show()
                } else {
                    btnPartFood?.setText(resources.getString(R.string.part_food_is) + "%.2f".format(weightPartFood) + " g")
                    weightSugarPartFoodTextView?.setText(resources.getString(R.string.sugar_in_part) + "%.2f".format(food?.weightSugarInPartFood(weightPartFood)) + resources.getString(R.string.g_item))
                    sugarInOneGramTextView?.setText(resources.getString(R.string.sugar_one_gramm_food) + "%.2f".format(food?.sugarInOneGramFood()) + resources.getString(R.string.g_item))
                    reloadActivity()
                }
            } else {
                Toast.makeText(this@FoodActivity, resources.getString(R.string.warning_is_null), Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNeutralButton(resources.getString(R.string.cancel)){ _, _ ->
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
        builder.setTitle(resources.getString(R.string.select_choice))
        builder.setMessage(resources.getString(R.string.what_do_you_with_ingredient) + ingredient.name + resources.getString(R.string.do_with_food))

        builder.setPositiveButton(resources.getString(R.string.to_change_value) ){ dialog, which ->
            changeWeightIngredient(ingredient)
        }

        builder.setNeutralButton(resources.getString(R.string.erase_from_food) ){ dialog, which ->
            deleteIngredientAlert(ingredient)
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }


    private fun deleteIngredientAlert(ingredient: Ingredient) {
        val builder2 = AlertDialog.Builder(this@FoodActivity)
        builder2.setTitle(resources.getString(R.string.erase_ingr_food))
        builder2.setMessage(resources.getString(R.string.realy_erase_ingr_question) + ingredient.name + resources.getString(R.string.erase_question))

        builder2.setPositiveButton(resources.getString(R.string.yes)){ dialog2, which ->
            food?.deleteIngredient(ingredient)
            isChange = true
            reloadActivity()
        }

        builder2.setNeutralButton(resources.getString(R.string.no)){ dialog2, which ->

        }

        val dialog2: AlertDialog = builder2.create()
        dialog2.show()
        dialog2.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    private fun changeName(isFromAddIngredient: Boolean) {
        val builder = AlertDialog.Builder(this@FoodActivity)
        builder.setTitle(resources.getString(R.string.set_name_food))
        builder.setMessage(resources.getString(R.string.set_this_name_question))
        val input = EditText(this@FoodActivity)
        val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        input.setText(nameFood)
        input.requestFocus()
        builder.setView(input)

        builder.setPositiveButton(resources.getString(R.string.yes)) { dialog, which ->
            if (input.text.toString().isNullOrEmpty()) {
                Toast.makeText(this@FoodActivity, resources.getString(R.string.warning_is_empty), Toast.LENGTH_SHORT).show()
            } else {
                nameFood = input.text.toString()
                if (food == null) {
                    food = Food(FoodsManager.getLastUsableId(), nameFood)
                } else {
                    food?.name = nameFood
                }
                textViewName?.setText(nameFood)
                if (isFromAddIngredient) {
                    addIngredient()
                }
                isChange = true
            }
            reloadActivity()
        }
        builder.setNeutralButton(resources.getString(R.string.cancel)){ _, _ ->
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
                Toast.makeText(this@FoodActivity, resources.getString(R.string.warning_is_null), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun changeWeightIngredient(ingredient: Ingredient) {
        val builder = AlertDialog.Builder(this@FoodActivity)
        val nameTypeIngredient = ingredient.name
        var weight = ingredient.weight
        builder.setTitle(resources.getString(R.string.change_value_ingredient))
        builder.setMessage(resources.getString(R.string.set_value_ingredient) + nameTypeIngredient)
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

        builder.setPositiveButton(resources.getString(R.string.yes)){ dialog, which ->
            weight = input.text.toString().toDouble()
            if (weight != 0.0) {
                ingredient.changeWeight(weight)
                isChange = true
                reloadActivity()
            } else {
                Toast.makeText(this@FoodActivity, resources.getString(R.string.warning_is_null), Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNeutralButton(resources.getString(R.string.cancel)){ _, _ ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    fun addIngredientDialog(typeIngredient: TypeIngredient) {
        val builder = AlertDialog.Builder(this@FoodActivity)
        val nameTypeIngredient = typeIngredient.name
        var weight = 0.0
        builder.setTitle(resources.getString(R.string.value_ingredient))
        builder.setMessage(resources.getString(R.string.set_value_ingredient) + nameTypeIngredient)
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

        builder.setPositiveButton(resources.getString(R.string.yes)){ dialog, which ->
            weight = input.text.toString().toDouble()
            if (weight != 0.0) {
                var ingredient = Ingredient(typeIngredient, weight)
                food?.addIngredient(ingredient)
                isChange = true
                reloadActivity()
            } else {
                Toast.makeText(this@FoodActivity, resources.getString(R.string.warning_is_null), Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNeutralButton(resources.getString(R.string.cancel)){ _, _ ->
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }
}