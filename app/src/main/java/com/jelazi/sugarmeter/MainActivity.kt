package com.jelazi.sugarmeter

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.SearchView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    var floatingActionBtnAddFood: FloatingActionButton? = null
    var floatingActionBtnAddIngredient: FloatingActionButton? = null
    var listViewFood: ListView? = null
    var searchView: SearchView? = null
    var info = ArrayList<HashMap<String, String>>()
    var foodListAdapter: FoodListAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        floatingActionBtnAddFood = findViewById(R.id.floatingActionButtonAddFood)
        floatingActionBtnAddIngredient = findViewById(R.id.floatingActionButtonEditIngredient)
        searchView = findViewById(R.id.searchViewMain) as SearchView
        listViewFood = findViewById(R.id.listViewFood) as ListView

        floatingActionBtnAddFood?.setOnClickListener{
            val intent = Intent (this, CookActivity::class.java)
            startActivity(intent)
        }

        floatingActionBtnAddIngredient?.setOnClickListener{
            val intent = Intent (this, TypeIngredientsListActivity::class.java)
            startActivity(intent)
        }
        loadData()

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Měřič cukru"
        createHashMap()

        foodListAdapter = FoodListAdapter(this, info)
        listViewFood?.adapter = (foodListAdapter)

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

        listViewFood?.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val name = info[i].get("name")
                startCookActivity(name.toString())
            }
        })

      //  TypeIngredientsManager.clearIngredients()
    }

    private fun startCookActivity(name: String) {
        val intent = Intent (this, CookActivity::class.java)
        startActivity(intent)
    }
    private fun loadData() {
        TypeIngredientsManager.getListTypeIngredientsFromPreferences(this)

        FoodManager.getListFoodsFromPreferences(this)
    }


    private fun createHashMap () {
        var hashMap: HashMap<String, String> = HashMap<String, String>()
        info = ArrayList<HashMap<String, String>>()
        for (i in 0..FoodManager.foodsList.size - 1) {
            hashMap = HashMap()
            FoodManager.foodsList[i].name?.let { hashMap.put("name", it) }
            info.add(hashMap)
        }
    }
}