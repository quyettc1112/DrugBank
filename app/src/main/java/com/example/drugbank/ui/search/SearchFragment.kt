package com.example.drugbank.ui.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.drugbank.databinding.FragmentSearchBinding
import com.example.drugbank.ui.search.childeFragment.DrugFragment.DrugFragment
import com.example.drugbank.ui.search.childeFragment.ProductFragment.ProductFragment

class SearchFragment : Fragment() {


    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SearchViewModel
    lateinit var fragment: ProductFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)


    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        val rootView = binding.root
        setUpTabWithSearchView()
        return rootView
    }

    private fun setUpTabWithSearchView() {

        val sectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager)
        val currentItem = binding.viewPager.currentItem
        fragment = (sectionsPagerAdapter.instantiateItem(binding.viewPager, currentItem) as? ProductFragment)!!
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)

    }





    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        // TODO: Use the ViewModel
    }



}