package com.example.drugbank.ui.activity.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.drugbank.R
import dagger.hilt.android.AndroidEntryPoint
import me.ibrahimsn.lib.NiceBottomBar

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var b  = findViewById<NiceBottomBar>(R.id.bottomBar)
        b.setActiveItem(1)
        b.setBadge(2)
        b.removeBadge(2)

        b.onItemSelected = {
            Toast.makeText(this, "Check Click", Toast.LENGTH_SHORT).show()

        }

        b.onItemReselected = {
            Toast.makeText(this, "Check Click", Toast.LENGTH_SHORT).show()

        }

        b.onItemLongClick = {
            Toast.makeText(this, "Check Click", Toast.LENGTH_SHORT).show()
        }


    }
}