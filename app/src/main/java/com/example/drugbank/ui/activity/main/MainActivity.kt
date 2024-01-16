package com.example.drugbank.ui.activity.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.drugbank.R
import com.example.drugbank.common.constant.Constant
import com.example.drugbank.databinding.ActivityMainBinding
import com.example.healthcarecomp.base.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import me.ibrahimsn.lib.NiceBottomBar

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding:ActivityMainBinding
    private lateinit var _mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpBottomNavigation()
//        _mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
//        _mainViewModel.setUpNav()
//        _mainViewModel.currentNav.observe(this, Observer {currentId->
//            if (currentId != null) {
//                val navController = findNavController(R.id.nav_host_fragment_activity_main)
//                navController.navigate(Constant.getNavSeleted(currentId))
//            }
//
//        })
//
//        binding.bottomBar.let { bt ->
//            bt.onItemSelected = {
//                _mainViewModel.ChangeNav(it)
//            }
//
//
//
//        }


       // setUpBottomNavigation()

    }
    private fun setUpBottomNavigation() {
        binding.let {
            it.bottomBar.setActiveItem(0)
            it.bottomBar.setBadge(2)

        }
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.bottomBar.let { bt ->
            bt.onItemSelected = {
                navController.navigate(Constant.getNavSeleted(it))
            }



        }
    }
}