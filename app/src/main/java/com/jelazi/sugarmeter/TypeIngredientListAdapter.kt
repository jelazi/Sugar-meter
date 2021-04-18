package com.jelazi.sugarmeter

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.avatarfirst.avatargenlib.AvatarConstants
import com.avatarfirst.avatargenlib.AvatarGenerator
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TypeIngredientListAdapter(context: Context, arrayList: ArrayList<HashMap<String, String>>) : CustomListAdapter(context, arrayList)  {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var myview = convertView
        val holder: ViewHolder

        if (convertView == null) {
            val mInflater = (context as Activity).layoutInflater

            myview = mInflater!!.inflate(R.layout.type_ingredient_item, parent, false)

            holder = ViewHolder()
            holder.name = myview!!.findViewById<TextView>(R.id.type_ingredient_name_part) as TextView
            holder.value = myview!!.findViewById<TextView>(R.id.type_ingredient_value_part) as TextView
            holder.image = myview!!.findViewById<TextView>(R.id.type_ingredient_picture_part) as ImageView

            myview.setTag(holder)
        } else {
            holder = myview!!.getTag() as ViewHolder
        }

        val map = arrayList.get(position)

        holder.name!!.setText(map.get("name"))
        holder.value!!.setText(map.get("value"))
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
        var value: TextView? = null
    }
}