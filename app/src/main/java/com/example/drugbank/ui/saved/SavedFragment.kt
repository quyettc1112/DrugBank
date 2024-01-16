package com.example.drugbank.ui.saved

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.drugbank.R
import com.example.drugbank.common.screen.Screen
import com.example.drugbank.databinding.ActivityMainBinding
import com.example.drugbank.ui.activity.main.MainActivity
import com.google.android.material.bottomappbar.BottomAppBar
import me.ibrahimsn.lib.NiceBottomBar

class SavedFragment : Fragment() {

    companion object {
        fun newInstance() = SavedFragment()
    }
    private lateinit var binding:ActivityMainBinding
    private lateinit var viewModel: SavedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SavedViewModel::class.java)

       // loadLocalUserData()
        val dialogBinding = layoutInflater.inflate(R.layout.dialog_login, null)
        val myDialog = Dialog(requireContext())
        myDialog.setContentView(dialogBinding)
        myDialog.setCancelable(true)
        myDialog.window?.setLayout(Screen.width, Screen.height)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(requireContext().getColor(R.color.zxing_transparent)))
        myDialog.show()

        myDialog.setOnDismissListener {
            val activity = requireActivity()
            val navController = activity.findNavController(R.id.nav_host_fragment_activity_main)
            navController.navigate(R.id.searchFragmentNav)
            val bottomBar = activity.findViewById<NiceBottomBar>(R.id.bottomBar)
            bottomBar.setActiveItem(0)

        }



    }

    private fun loadLocalUserData() {
        val  delayMillis: Long = 1250 // 1,25  second
        Handler(Looper.getMainLooper()).postDelayed({
            val activity = requireActivity()
            val navController = activity.findNavController(R.id.nav_host_fragment_activity_main)
            navController.navigate(R.id.action_savedFragmentNav_to_loginFragment)
        }, delayMillis)
    }


}