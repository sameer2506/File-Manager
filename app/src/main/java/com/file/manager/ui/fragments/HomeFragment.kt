package com.file.manager.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.navigation.fragment.findNavController
import com.file.manager.R
import com.file.manager.databinding.FragmentHomeBinding
import com.file.manager.model.CategoryData
import com.file.manager.ui.adapter.CategoryAdapter
import com.file.manager.utils.log

class HomeFragment : Fragment() {

    private lateinit var fragmentContext: Context

    private lateinit var binding: FragmentHomeBinding

    private var categoryList: ArrayList<CategoryData> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        fragmentContext = requireContext()

        binding = FragmentHomeBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpArray()

        binding.phoneStorageCardView.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_file_list_fragment)
        }

        val mainAdapter = CategoryAdapter(fragmentContext, categoryList)

        binding.categoryGridView.adapter = mainAdapter

        binding.categoryGridView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                when(position){
                    0 -> findNavController().navigate(R.id.image_list_fragment)
                }
            }

    }

    private fun setUpArray(){

        if (categoryList.size > 0)
            return

        var data = CategoryData(R.drawable.image_icon, "Photos")
        categoryList.add(data)

        data = CategoryData(R.drawable.video_icon, "Videos")
        categoryList.add(data)

        data = CategoryData(R.drawable.music_icon, "Audio")
        categoryList.add(data)

        data = CategoryData(R.drawable.docs_icon, "Documents")
        categoryList.add(data)

        data = CategoryData(R.drawable.folder, "APKs")
        categoryList.add(data)

        data = CategoryData(R.drawable.zip_icons, "Archives")
        categoryList.add(data)

        data = CategoryData(R.drawable.download_icons, "Downloads")
        categoryList.add(data)

    }

}