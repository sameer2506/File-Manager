package com.file.manager.ui.fragments.category

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
import com.file.manager.databinding.FilesListBinding
import com.file.manager.model.FileModel
import com.file.manager.model.FileType
import com.file.manager.ui.adapter.FileListRecyclerAdapter
import com.file.manager.utils.convertFileSizeToMB
import com.file.manager.utils.launchFileIntent
import java.io.File

class ArchivesListFragment : Fragment(), FileListRecyclerAdapter.OnItemClick {

    private lateinit var fragmentContext: Context
    private lateinit var fragmentActivity: FragmentActivity

    private lateinit var binding: FilesListBinding

    private val fileList: ArrayList<FileModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentContext = requireContext()
        fragmentActivity = requireActivity()

        binding = FilesListBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvCategory.text = "Archives"

        listExternalStorage()

        binding.filesList.apply {
            layoutManager = LinearLayoutManager(fragmentContext)
            setHasFixedSize(true)
            adapter = FileListRecyclerAdapter(fileList, this@ArchivesListFragment)
        }

    }

    private fun listExternalStorage() {
        val state = Environment.getExternalStorageState()

        if (Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state) {
            listFiles(Environment.getExternalStorageDirectory())
        }
    }

    private fun listFiles(directory: File) {
        val files = directory.listFiles()
        if (files != null) {
            for (file in files) {
                if (file != null) {
                    if (file.isDirectory)
                        listFiles(file)
                    else {
                        if (file.name.endsWith("zip")
                        ) {
                            val data = FileModel(
                                file.path,
                                FileType.getFileType(file),
                                file.name,
                                convertFileSizeToMB(file.length()),
                                file.extension,
                                file.listFiles()?.size
                                    ?: 0,
                                R.drawable.zip.toString()
                            )
                            fileList.add(data)
                        }
                        if (file.name.endsWith("rar")
                        ) {
                            val data = FileModel(
                                file.path,
                                FileType.getFileType(file),
                                file.name,
                                convertFileSizeToMB(file.length()),
                                file.extension,
                                file.listFiles()?.size
                                    ?: 0,
                                R.drawable.rar.toString()
                            )
                            fileList.add(data)
                        }
                    }
                }
            }
        }
    }

    override fun onClick(fileModel: FileModel) {
        fragmentContext.launchFileIntent(fileModel)
    }

    override fun onLongClick(fileModel: FileModel) {
    }

}