package com.example.drugbank.ui.saved

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
import com.example.drugbank.ui.activity.main.MainActivity

class SavedFragment : Fragment() {

    companion object {
        fun newInstance() = SavedFragment()
    }

    private lateinit var viewModel: SavedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        return inflater.inflate(R.layout.fragment_saved, container, false)
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