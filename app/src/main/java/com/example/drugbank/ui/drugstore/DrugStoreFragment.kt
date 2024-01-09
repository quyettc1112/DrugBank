package com.example.drugbank.ui.drugstore

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.drugbank.R

class DrugStoreFragment : Fragment() {

    companion object {
        fun newInstance() = DrugStoreFragment()
    }

    private lateinit var viewModel: DrugStoreViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_drug_store, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DrugStoreViewModel::class.java)
        // TODO: Use the ViewModel
    }

}