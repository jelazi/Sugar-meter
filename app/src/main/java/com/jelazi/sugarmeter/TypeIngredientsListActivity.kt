package com.jelazi.sugarmeter

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SearchView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TypeIngredientsListActivity : AppCompatActivity() {
    var listview: ListView? = null
    var searchView: SearchView? = null
    var info = ArrayList<HashMap<String, String>>()
    var typeIngredientListAdapter: TypeIngredientListAdapter? = null
    var floatingBtnAddTypeIngredient: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type_ingredients_list)
        searchView = findViewById(R.id.searchView) as SearchView
        listview = findViewById(R.id.mListView) as ListView
        floatingBtnAddTypeIngredient = findViewById(R.id.floatingActionButtonAddTypeIngredient)
        floatingBtnAddTypeIngredient?.setOnClickListener {
            addTypeIngredient()
        }

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Výběr surovin"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        createHashMap()

        typeIngredientListAdapter = TypeIngredientListAdapter(this, info)
        listview?.adapter = (typeIngredientListAdapter)

        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val text = newText
                typeIngredientListAdapter!!.filter(text)
                return false
            }
        })

        listview?.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val name = info[i].get("name")
                    val resultIntent = Intent()
                    resultIntent.putExtra("name", name)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
            }
        })
    }

    private fun createHashMap () {
        var hashMap: HashMap<String, String> = HashMap<String, String>()
        info = ArrayList<HashMap<String, String>>()
        for (i in 0..TypeIngredientsManager.typeIngredients.size - 1) {
            hashMap = HashMap()
            TypeIngredientsManager.typeIngredients[i].name?.let { hashMap.put("name", it) }
            TypeIngredientsManager.typeIngredients[i].valueSugar?.let { hashMap.put("value", it.toString()) }
            info.add(hashMap)
        }
    }

    private fun addTypeIngredient() {

        val intent = Intent(this, TypeIngredientActivity::class.java)
        Log.d("TAG", "addTypeIngredient")
        startActivity(intent)

    }
}