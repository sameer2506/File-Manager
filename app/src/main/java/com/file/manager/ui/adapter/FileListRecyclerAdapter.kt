package com.file.manager.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.file.manager.R
import com.file.manager.model.FileModel
import com.file.manager.model.FileType
import com.file.manager.utils.log
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_recycler_file.view.*

class FileListRecyclerAdapter(
    private var filesList: ArrayList<FileModel>,
    private val listener: OnItemClick
) : RecyclerView.Adapter<FileListRecyclerAdapter.ViewHolder>() {

    var fileList = filesList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_file, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = fileList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bindView(position)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener, View.OnLongClickListener {

        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        fun bindView(position: Int) {
            val fileModel = fileList[position]
            itemView.nameTextView.text = fileModel.name

            if (fileModel.fileType == FileType.FOLDER) {
                itemView.folderTextView.visibility = View.VISIBLE
                itemView.totalSizeTextView.visibility = View.GONE
                itemView.folderTextView.text = "${fileModel.subFiles} files"
            } else {
                itemView.folderTextView.visibility = View.GONE
                itemView.totalSizeTextView.visibility = View.VISIBLE
                itemView.totalSizeTextView.text = "${String.format("%.2f", fileModel.sizeInMB)} mb"
                itemView.rightIconImage.visibility = View.GONE
                Picasso.get().load(fileModel.image).into(itemView.folder_image)
            }
        }

        override fun onClick(p0: View?) {
            listener.onClick(fileList[adapterPosition])
        }

        override fun onLongClick(p0: View?): Boolean {
            listener.onLongClick(fileList[adapterPosition])
            return true
        }

    }

    interface OnItemClick {
        fun onClick(fileModel: FileModel)
        fun onLongClick(fileModel: FileModel)
    }

}