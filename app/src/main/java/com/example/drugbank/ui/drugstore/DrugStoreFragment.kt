package com.example.drugbank.ui.drugstore

import android.content.pm.PackageManager
import android.location.Location
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.drugbank.R
import com.google.android.gms.maps.GoogleMap


import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DrugStoreFragment : Fragment() {


    private lateinit var viewModel: DrugStoreViewModel
    private lateinit var map: GoogleMap
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }





}