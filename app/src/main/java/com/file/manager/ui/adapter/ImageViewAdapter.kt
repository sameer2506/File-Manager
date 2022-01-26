package com.file.manager.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.file.manager.R
import com.file.manager.model.FileModel
import com.squareup.picasso.Picasso

internal class ImageViewAdapter(
    private val context: Context,
    private val fileList: ArrayList<FileModel>
) : BaseAdapter() {

    private var layoutInflater: LayoutInflater? = null
    private lateinit var imageView: ImageView

    override fun getCount(): Int {
        return fileList.size
    }

    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertViewS: View?, parent: ViewGroup?): View {
        var convertView = convertViewS
        if (layoutInflater == null)
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (convertView == null)
            convertView = layoutInflater!!.inflate(R.layout.image_item_view, null)
        imageView = convertView!!.findViewById(R.id.img_item)
        Picasso.get().load(fileList[position].image).into(imageView)
        return convertView
    }

}