package com.example.drugbank.ui.activity.main

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.drugbank.R
import com.example.drugbank.common.Token.TokenManager
import com.example.drugbank.common.constant.Constant
import com.example.drugbank.databinding.ActivityMainBinding
import com.example.drugbank.repository.Admin_UserM_Repository
import com.example.healthcarecomp.base.BaseActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivity() , OnMapReadyCallback {

    private lateinit var binding:ActivityMainBinding
    private lateinit var _mainViewModel: MainViewModel

    private  var currentEmailUser: String? = null
    private lateinit var tokenManager: TokenManager

    private lateinit var map: GoogleMap

    @Inject
    lateinit var userRepository: Admin_UserM_Repository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tokenManager = TokenManager(this@MainActivity)
        currentEmailUser = Constant.getSavedUsername(this@MainActivity)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // setUpBottomNav()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        if (mapFragment != null) {
            mapFragment.getMapAsync { googleMap ->
                map = googleMap
                val hoChiMinhLatLng = LatLng(10.762622, 106.660172 )
                map.addMarker(MarkerOptions().position(hoChiMinhLatLng).title("What"))
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(hoChiMinhLatLng, 20F))
                Log.e("TAG", "Not Null")
            }
        } else {
            Log.e("TAG", "Map fragment is not found in the layout or it is not a SupportMapFragment")
        }
    }

//    private fun setUpBottomNav() {
//        _mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
//        _mainViewModel.setUpNav()
//        _mainViewModel.currentNav.observe(this, Observer { currentId ->
//            if (currentId != null) {
//                val navController = findNavController(com.example.drugbank.R.id.nav_host_fragment_activity_main)
//                navController.navigate(Constant.getNavSeleted(currentId))
//            }
//        })
//        binding.bottomBar.let { bt ->
//            bt.onItemSelected = {
//                    _mainViewModel.ChangeNav(it)
//            }
//            bt.setBadge(2)
//        }
//    }

    fun showLoginDialog() {
        showLoginDialog(this, this, 0)
    }
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val hoChiMinhLatLng = LatLng(0.0, 0.0 )
        map.addMarker(MarkerOptions().position(hoChiMinhLatLng).title("What"))
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(hoChiMinhLatLng, 20F))
    }




}