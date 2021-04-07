package com.jelazi.sugarmeter

import android.app.Activity
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
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TypeIngredientsListActivity : AppCompatActivity() {
    var listview: ListView? = null
    var searchView: SearchView? = null
    var info = ArrayList<HashMap<String, String>>()
    var typeIngredientListAdapter: TypeIngredientListAdapter? = null
    var floatingBtnAddTypeIngredient: FloatingActionButton? = null
    var typeIntent = ""
    private val NEW_INGREDIENT = 1
    private val EDIT_INGREDIENT = 2

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

        typeIntent = intent.getStringExtra("typeIntent").toString()

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
                if (typeIntent == "choice") {
                    val ingredient = TypeIngredientsManager.getTypeIngredientByName(info[i].get("name").toString())
                    val id = ingredient?.id.toString()
                    val name = ingredient?.name
                    val resultIntent = Intent()
                    resultIntent.putExtra( "id", id)
                    resultIntent.putExtra("name", name)
                    setResult(Activity.RESULT_OK, resultIntent)
                   finish()
                }
            }
        })

        listview?.setOnItemLongClickListener(object : AdapterView.OnItemLongClickListener {
            override fun onItemLongClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long): Boolean {
                val ingredient = TypeIngredientsManager.getTypeIngredientByName(info[i].get("name").toString())
                if (ingredient != null)
                    alertDialogChoiceActivity(ingredient)
                return true
            }
        })

        if (TypeIngredientsManager.listTypeIngredients.isEmpty()) {
            Toast.makeText(this@TypeIngredientsListActivity, "Nejdříve vytvořte nějakou surovinu.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun alertDialogChoiceActivity(ingredient: TypeIngredient) {
        val builder = AlertDialog.Builder(this@TypeIngredientsListActivity)
        builder.setTitle("Výběr možnosti")
        builder.setMessage("Co chcete se surovinou udělat?")

        builder.setPositiveButton("Editovat"){ dialog, which ->
            openIngredientActivityForEdit(ingredient)
        }

        builder.setNeutralButton("Vymazat"){ dialog, which ->
            deleteIngredientAlert(ingredient)
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    private fun openIngredientActivityForEdit(ingredient: TypeIngredient) {
        val intent = Intent(this, TypeIngredientActivity::class.java)
        intent.putExtra("typeIntent", "edit")
        intent.putExtra("typeIngredient", ingredient.id.toString())
        startActivityForResult(intent, EDIT_INGREDIENT)
    }

    private fun deleteIngredientAlert(ingredient: TypeIngredient) {
        val builder2 = AlertDialog.Builder(this@TypeIngredientsListActivity)
        builder2.setTitle("Vymazání suroviny")
        builder2.setMessage("Opravdu chcete surovinu: " + ingredient.name + " vymazat?")

        builder2.setPositiveButton("Ano"){ dialog2, which ->
            ingredient.id.let { it?.let { it1 -> TypeIngredientsManager.deleteIngredient(it1) } }
            onResume()
        }

        builder2.setNeutralButton("Ne"){ dialog2, which ->

        }

        val dialog2: AlertDialog = builder2.create()
        dialog2.show()
        dialog2.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    override fun onResume() {
        super.onResume()
        createHashMap()
        typeIngredientListAdapter = TypeIngredientListAdapter(this, info)
        listview?.setAdapter(typeIngredientListAdapter)
    }


    private fun createHashMap () {
        var hashMap: HashMap<String, String> = HashMap<String, String>()
        info = ArrayList<HashMap<String, String>>()
        for (i in 0..TypeIngredientsManager.listTypeIngredients.size - 1) {
            hashMap = HashMap()
            TypeIngredientsManager.listTypeIngredients[i].name?.let { hashMap.put("name", it) }
            TypeIngredientsManager.listTypeIngredients[i].valueSugar?.let { hashMap.put("value", it.toString()) }
            info.add(hashMap)
        }
    }

    private fun addTypeIngredient() {
        val intent = Intent(this, TypeIngredientActivity::class.java)
        startActivity(intent)
    }
}