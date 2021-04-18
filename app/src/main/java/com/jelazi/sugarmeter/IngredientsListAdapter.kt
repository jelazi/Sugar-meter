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

class IngredientsListAdapter (context: Context, arrayList: ArrayList<HashMap<String, String>>) : CustomLIstAdapter(context, arrayList)  {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var myview = convertView
        val holder: ViewHolder

        if (convertView == null) {
            val mInflater = (context as Activity).layoutInflater

            myview = mInflater!!.inflate(R.layout.ingredients_item, parent, false)

            holder = ViewHolder()
            holder.name = myview!!.findViewById(R.id.ingredient_name_part) as TextView
            holder.weight = myview!!.findViewById(R.id.ingredient_weight_part) as TextView
            holder.image = myview!!.findViewById(R.id.ingredient_picture_part) as ImageView
            holder.sugar = myview!!.findViewById(R.id.ingredient_sugar_part) as TextView

            myview.setTag(holder)
        } else {
            holder = myview!!.getTag() as ViewHolder
        }

        val map = arrayList.get(position)

        holder.name!!.setText(map.get("name"))
        holder.weight!!.setText(map.get("weight"))
        holder.sugar!!.setText(map.get("valueSugar"))
        holder.image!!.setImageDrawable(
            AvatarGenerator.avatarImage(
                context,
                100,
                AvatarConstants.CIRCLE,
                map.get("name").toString()
            ))

        return myview
    }

    class ViewHolder {

        var image: ImageView? = null
        var name: TextView? = null
        var weight: TextView? = null
        var sugar: TextView? = null
    }

}