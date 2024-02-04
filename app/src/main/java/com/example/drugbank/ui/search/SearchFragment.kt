package com.example.drugbank.ui.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.drugbank.R
import com.google.android.material.tabs.TabLayout

class SearchFragment : Fragment() {


    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_search, container, false)

        // Tạo ViewPager
        val viewPager: ViewPager = rootView.findViewById(R.id.view_pager)

        // Tạo và thiết lập SectionsPagerAdapter
        val sectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager)
        viewPager.adapter = sectionsPagerAdapter

        // Tạo TabLayout và liên kết với ViewPager
        val tabLayout: TabLayout = rootView.findViewById(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)

        return  rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        // TODO: Use the ViewModel
    }

}