package com.file.manager.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.file.manager.R
import com.file.manager.model.CategoryData
import com.squareup.picasso.Picasso

internal class CategoryAdapter(
    private val context: Context,
    private val arrayList: ArrayList<CategoryData>
) : BaseAdapter() {

    private var layoutInflater: LayoutInflater? = null
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView

    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView

        val data = arrayList[position]

        if (layoutInflater == null){
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null){
            convertView = layoutInflater!!.inflate(R.layout.category_item, null)
        }
        imageView = convertView!!.findViewById(R.id.category_image)
        Picasso.get().load(data.image).into(imageView)
        textView = convertView.findViewById(R.id.category_text_view)
        textView.text = data.category

        return convertView
    }

}