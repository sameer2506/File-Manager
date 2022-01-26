package com.file.manager.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.file.manager.R
import com.file.manager.databinding.FragmentFileListBinding
import com.file.manager.fileService.FileChangeBroadcastReceiver
import com.file.manager.model.FileModel
import com.file.manager.model.FileType
import com.file.manager.ui.adapter.FileListRecyclerAdapter
import com.file.manager.ui.dialog.FileOptionsDialog
import com.file.manager.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_enter_name.view.*
import java.io.File

class FileListFragment : Fragment(), FileListRecyclerAdapter.OnItemClick {

    private lateinit var fragmentContext: Context
    private lateinit var fragmentActivity: FragmentActivity

    private lateinit var binding: FragmentFileListBinding

    private var fileList: ArrayList<FileModel> = ArrayList()

    private var isCopyModeActive: Boolean = false
    private var selectedFileModel: FileModel? = null

    private var path: String = ""

    companion object{
        private const val OPTIONS_DIALOG_TAG = "com.file.manager.ui.options_dialog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentContext = requireContext()
        fragmentActivity = requireActivity()

        binding = FragmentFileListBinding.inflate(layoutInflater)

        (fragmentActivity as AppCompatActivity).setSupportActionBar(binding.toolbar)

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
            addFileFragment(fileModel)
        }
        else{
            fragmentContext.launchFileIntent(fileModel)
        }
    }

    override fun onLongClick(fileModel: FileModel) {
        val optionsDialog = FileOptionsDialog.build {  }

        optionsDialog.onDeleteClickListener = {
            path = fileModel.path
            deleteFile(path)
            updateContentOfCurrentFragment()
            fragmentContext.toast("${fileModel.name} deleted successfully.")
        }

        optionsDialog.onCopyClickListener = {
            isCopyModeActive = true
            selectedFileModel = fileModel
            fragmentActivity.invalidateOptionsMenu()
        }

        optionsDialog.show(fragmentActivity.supportFragmentManager, OPTIONS_DIALOG_TAG)
    }

    private fun updateContentOfCurrentFragment(){
        val broadcastIntent = Intent()
        broadcastIntent.action = fragmentContext.getString(R.string.file_change_broadcast)
        broadcastIntent.putExtra(FileChangeBroadcastReceiver.EXTRA_PATH, path)
        fragmentActivity.sendBroadcast(broadcastIntent)
    }

    private fun addFileFragment(fileModel: FileModel){
        val bundle = bundleOf(
            "pathId" to fileModel.path
        )

        findNavController().navigate(R.id.action_file_list_to_add_file_fragment, bundle)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.main_menu, menu)

        val subMenu = menu.findItem(R.id.subMenu)
        val pasteItem = menu.findItem(R.id.menuPasteFile)
        val cancelItem = menu.findItem(R.id.menuCancel)

        subMenu.isVisible = !isCopyModeActive
        pasteItem.isVisible = isCopyModeActive
        cancelItem.isVisible = isCopyModeActive

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuNewFile -> {
                createNewFileInDirectory()
            }
            R.id.menuNewFolder -> {
                createNewFolderInDirectory()
            }
            R.id.menuCancel -> {
                isCopyModeActive = false
                fragmentActivity.invalidateOptionsMenu()
            }
            R.id.menuPasteFile -> {
                log("Paste file")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createNewFileInDirectory(){
        val bottomSheetDialog = BottomSheetDialog(fragmentContext)
        val view = LayoutInflater.from(fragmentContext).inflate(R.layout.dialog_enter_name, null)
        view.createButton.setOnClickListener {
            val fileName = view.nameEditText.text.toString()
            if (fileName.isNotEmpty()){
                createNewFile(fileName, path) {_, message ->
                    bottomSheetDialog.dismiss()
                    fragmentContext.toast(message)
                    updateContentOfCurrentFragment()
                }
            }
        }
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }

    private fun createNewFolderInDirectory(){
        val bottomSheetDialog = BottomSheetDialog(fragmentContext)
        val view = LayoutInflater.from(fragmentContext).inflate(R.layout.dialog_enter_name, null)
        view.createButton.setOnClickListener {
            val fileName = view.nameEditText.text.toString()
            if (fileName.isNotEmpty()){
                createNewFolder(fileName, path) {_, message ->
                    bottomSheetDialog.dismiss()
                    fragmentContext.toast(message)
                    updateContentOfCurrentFragment()
                }
            }
        }
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }


}