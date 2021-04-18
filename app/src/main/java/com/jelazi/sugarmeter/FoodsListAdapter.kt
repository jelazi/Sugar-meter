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

class FoodsListAdapter (context: Context, arrayList: ArrayList<HashMap<String, String>>) : CustomLIstAdapter(context, arrayList)  {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var myview = convertView
        val holder: ViewHolder

        if (convertView == null) {
            val mInflater = (context as Activity).layoutInflater

            myview = mInflater!!.inflate(R.layout.food_item, parent, false)

            holder = ViewHolder()
            holder.name = myview!!.findViewById(R.id.food_name_part) as TextView
            holder.image = myview!!.findViewById(R.id.food_picture_part) as ImageView

            myview.setTag(holder)
        } else {
            holder = myview!!.getTag() as ViewHolder
        }

        val map = arrayList.get(position)

        holder.name!!.setText(map.get("name"))
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
    }
}