package com.example.drugbank.ui.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.drugbank.ui.search.childeFragment.DrugFragment.DrugFragment
import com.example.drugbank.ui.search.childeFragment.ProductFragment.ProductFragment

class SectionsPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> DrugFragment()
            1 -> ProductFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Drug"
            1 -> "Product"
            else -> null
        }
    }
}