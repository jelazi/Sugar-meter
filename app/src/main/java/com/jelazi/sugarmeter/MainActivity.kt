package com.jelazi.sugarmeter

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    var floatingActionBtnAddFood: FloatingActionButton? = null
    var listViewFoods: ListView? = null
    var searchView: SearchView? = null
    var info = ArrayList<HashMap<String, String>>()
    var foodListAdapter: FoodsListAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        floatingActionBtnAddFood = findViewById(R.id.floatingActionButtonAddFood)
        searchView = findViewById(R.id.searchViewMain) as SearchView
        listViewFoods = findViewById(R.id.listViewFoods) as ListView

        floatingActionBtnAddFood?.setOnClickListener{
            newFoodActivity()
        }

        loadData()
       // FoodsManager.clearFoods()
       // FoodsManager.setListFoodsToPreferences(this)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = resources.getString(R.string.main_activity_title)
        createHashMap()

        foodListAdapter = FoodsListAdapter(this, info)
        listViewFoods?.adapter = (foodListAdapter)

        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val text = newText
                foodListAdapter!!.filter(text)
                return false
            }
        })

        listViewFoods?.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val name = info[i].get("name")
                editFoodActivity(name.toString())
            }
        })

        listViewFoods?.setOnItemLongClickListener(object : AdapterView.OnItemLongClickListener {
            override fun onItemLongClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long): Boolean {
                val clickFood = FoodsManager.getFoodByName(info[i].get("name").toString())
                if (clickFood != null)
                    alertDialogChoiceActivity(clickFood)
                return true
            }
        })

        if (FoodsManager.foodsList.isEmpty()) {
            Toast(this).showCustomToast (resources.getString(R.string.create_first_food),this)
        }
    }


    private fun alertDialogChoiceActivity(food: Food) {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle(resources.getString(R.string.select_choice))
        builder.setMessage(resources.getString(R.string.what_do_you_with_food) + food.name + resources.getString(R.string.do_with_food))

        builder.setPositiveButton(resources.getString(R.string.to_edit)){ dialog, which ->
            editFoodActivity(food.name.toString())
        }

        builder.setNeutralButton(resources.getString(R.string.to_erase)){ dialog, which ->
            deleteFoodAlert(food)
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    private fun deleteFoodAlert(food: Food) {
        val builder2 = AlertDialog.Builder(this@MainActivity)
        builder2.setTitle(resources.getString(R.string.erase_food))
        builder2.setMessage(resources.getString(R.string.do_you_realy_food) + food.name + resources.getString(R.string.erase_question_mark))

        builder2.setPositiveButton(resources.getString(R.string.yes)){ dialog2, which ->
            food.id.let { it?.let { it1 -> FoodsManager.deleteFood(it1) } }
            FoodsManager.setListFoodsToPreferences(this)
            onResume()
        }

        builder2.setNeutralButton(resources.getString(R.string.no)){ dialog2, which ->

        }

        val dialog2: AlertDialog = builder2.create()
        dialog2.show()
        dialog2.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.list_ingredients -> {
                val intent = Intent (this, TypeIngredientsListActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }



    private fun reloadActivity() {
        createHashMap()
        foodListAdapter = FoodsListAdapter(this, info)
        listViewFoods?.adapter = (foodListAdapter)
    }

    override fun onResume() {
        super.onResume()
        reloadActivity()
    }

    private fun editFoodActivity(name: String) {
        val food = FoodsManager.getFoodByName(name)
        val intent
        = Intent (this, FoodActivity::class.java)
        intent.putExtra("typeIntent", "edit")
        intent.putExtra("id", food?.id.toString())
        startActivity(intent)
    }

    private fun newFoodActivity() {
        val intent
                = Intent (this, FoodActivity::class.java)
        intent.putExtra("typeIntent", "new")
        startActivity(intent)
    }

    private fun loadData() {
        TypeIngredientsManager.getListTypeIngredientsFromPreferences(this)

        FoodsManager.getListFoodsFromPreferences(this)
    }


    private fun createHashMap () {
        var hashMap: HashMap<String, String> = HashMap<String, String>()
        info = ArrayList<HashMap<String, String>>()
        for (i in 0..FoodsManager.foodsList.size - 1) {
            hashMap = HashMap()
            FoodsManager.foodsList[i].name?.let { hashMap.put("name", it) }
            info.add(hashMap)
        }
    }
}