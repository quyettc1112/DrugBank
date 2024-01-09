package com.example.drugbank.ui.activity.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.drugbank.R
import com.example.drugbank.databinding.ActivityMainBinding
import com.example.healthcarecomp.base.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import me.ibrahimsn.lib.NiceBottomBar

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.let {
            it.bottomBar.setActiveItem(0)
            it.bottomBar.setBadge(2)
        }

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        binding.bottomBar.let { bt ->
            bt.onItemSelected  = {

            }

        }

       // navController.navigate(R.id.savedFragment)









    }
}