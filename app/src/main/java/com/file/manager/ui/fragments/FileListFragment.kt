package com.file.manager.ui.fragments

import android.content.Context
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.file.manager.R
import com.file.manager.databinding.FragmentFileListBinding
import com.file.manager.model.FileModel
import com.file.manager.model.FileType
import com.file.manager.ui.adapter.FileListRecyclerAdapter
import com.file.manager.utils.convertFileSizeToMB
import com.file.manager.utils.launchFileIntent
import com.file.manager.utils.log
import java.io.File

class FileListFragment : Fragment(), FileListRecyclerAdapter.OnItemClick {

    private lateinit var fragmentContext: Context
    private lateinit var fragmentActivity: FragmentActivity

    private lateinit var binding: FragmentFileListBinding

    private var fileList: ArrayList<FileModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentContext = requireContext()
        fragmentActivity = requireActivity()

        binding = FragmentFileListBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listExternalStorage()

        binding.fileRecyclerView.apply {
            layoutManager = LinearLayoutManager(fragmentContext)
            setHasFixedSize(true)
            adapter = FileListRecyclerAdapter(fileList, this@FileListFragment)
        }

    }

    override fun onClick(fileModel: FileModel) {
        if (fileModel.fileType == FileType.FOLDER){
            log("open sub folder")
        }
        else{
            fragmentContext.launchFileIntent(fileModel)
        }
    }

    override fun onLongClick(fileModel: FileModel) {
        log("onLong function called")
    }


    private fun listExternalStorage(){
        val state = Environment.getExternalStorageState()

        if (Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state){
            listFiles(Environment.getExternalStorageDirectory())
        }
    }

    private fun listFiles(directory: File){
        val files = directory.listFiles()
        if (files != null){
            for (file in files){
                if (file != null){
                    if (file.isDirectory){
                        val data = FileModel(
                            file.path,
                            FileType.getFileType(file),
                            file.name,
                            convertFileSizeToMB(file.length()),
                            file.extension,
                            file.listFiles()?.size
                                ?: 0,
                            R.drawable.folder_list_icon.toString()
                        )
                        fileList.add(data)
                    }
                    else{
                        val data = FileModel(
                            file.path,
                            FileType.getFileType(file),
                            file.name,
                            convertFileSizeToMB(file.length()),
                            file.extension,
                            file.listFiles()?.size
                                ?: 0,
                            file.toURI().toString()
                        )
                        fileList.add(data)
                    }
                }
            }
        }
    }


}