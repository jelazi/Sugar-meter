package com.jelazi.sugarmeter

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.avatarfirst.avatargenlib.AvatarConstants
import com.avatarfirst.avatargenlib.AvatarGenerator
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


open class CustomListAdapter (context: Context, arrayList: ArrayList<HashMap<String, String>>) : BaseAdapter()  {
    var arrayList = arrayList
    var context = context
    var tempNameVersionList = ArrayList(arrayList)

    //must be override
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var myview = convertView
        return myview!!
    }

    override fun getItem(p0: Int): Any {
        return arrayList.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return arrayList.size
    }


    fun filter(text: String?) {

        val text = text!!.toLowerCase(Locale.getDefault())
        val arrayText = text!!.toLowerCase(Locale.getDefault()).split(" ")

        arrayList.clear()

        if (text.length == 0) {
            arrayList.addAll(tempNameVersionList)
        } else {
            for (i in 0..tempNameVersionList.size - 1) {
                var isCorrect = true
                for (tex in arrayText) {
                    if (!tempNameVersionList.get(i).get("name")!!.toLowerCase(Locale.getDefault()).contains(tex)) {
                        isCorrect = false
                    }
                }
                if (isCorrect) {
                    arrayList.add(tempNameVersionList.get(i))
                }
            }
        }
        notifyDataSetChanged()
    }
}
