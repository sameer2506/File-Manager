package com.file.manager.ui.fragments.category

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.file.manager.databinding.FragmentImageListBinding
import com.file.manager.model.FileModel
import com.file.manager.model.FileType
import com.file.manager.ui.adapter.ImageViewAdapter
import com.file.manager.utils.convertFileSizeToMB
import com.file.manager.utils.launchFileIntent
import java.io.File

class ImageListFragment : Fragment() {

    private lateinit var fragmentContext: Context
    private lateinit var fragmentActivity: FragmentActivity

    private lateinit var binding: FragmentImageListBinding

    private val fileList: ArrayList<FileModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentContext = requireContext()
        fragmentActivity = requireActivity()

        binding = FragmentImageListBinding.inflate(layoutInflater)

        (fragmentActivity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        listExternalStorage()

        val mainAdapter = ImageViewAdapter(fragmentContext, fileList)
        binding.imageGridView.adapter = mainAdapter

        binding.imageGridView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                fragmentContext.launchFileIntent(fileList[position])
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
                        if (file.name.endsWith("jpg") || file.name.endsWith("jpeg") ||
                            file.name.endsWith("png")
                        ) {
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

}