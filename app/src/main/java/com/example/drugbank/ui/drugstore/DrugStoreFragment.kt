package com.example.drugbank.ui.drugstore

import android.content.Intent
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
import com.example.drugbank.databinding.FragmentDrugStoreBinding
import com.example.drugbank.ui.activity.map.MapsActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DrugStoreFragment : Fragment() {


    private lateinit var viewModel: DrugStoreViewModel
    private lateinit var _binding: FragmentDrugStoreBinding

   // private lateinit var map: GoogleMap
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       _binding = FragmentDrugStoreBinding.inflate(inflater, container, false)

       _binding.changeToMap.setOnClickListener {
           val intent = Intent(requireContext(), MapsActivity::class.java)
           startActivity(intent)
       }

        return _binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DrugStoreViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//         val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
//        if (mapFragment != null) {
//            mapFragment.getMapAsync { googleMap ->
//                map = googleMap
//                val hoChiMinhLatLng = LatLng(10.762622, 106.660172 )
//                map.addMarker(MarkerOptions().position(hoChiMinhLatLng).title("What"))
//                map.animateCamera(CameraUpdateFactory.newLatLngZoom(hoChiMinhLatLng, 20F))
//                Log.e("TAG", "Not Null")
//            }
//        } else {
//            Log.e("TAG", "Map fragment is not found in the layout or it is not a SupportMapFragment")
//        }
    }

//    override fun onMapReady(googleMap: GoogleMap) {
//        map = googleMap
//        val hoChiMinhLatLng = LatLng(0.0, 0.0 )
//        map.addMarker(MarkerOptions().position(hoChiMinhLatLng).title("What"))
//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(hoChiMinhLatLng, 20F))
//    }


}