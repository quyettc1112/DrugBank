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
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drugbank.R
import com.example.drugbank.common.constant.Constant
import com.example.drugbank.common.screen.Screen
import com.example.drugbank.databinding.ActivityMainBinding
import com.example.drugbank.databinding.FragmentSavedBinding
import com.example.drugbank.ui.activity.main.MainActivity
import com.google.android.material.bottomappbar.BottomAppBar
import me.ibrahimsn.lib.NiceBottomBar

class SavedFragment : Fragment() {

    companion object {
        fun newInstance() = SavedFragment()
    }
   // private lateinit var binding:ActivityMainBinding
    private lateinit var _binding: FragmentSavedBinding
    private lateinit var viewModel: SavedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)

        val UserAdapter = UserAdapter(Constant.getUserList())
        _binding.rclListUser.adapter = UserAdapter

        return _binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SavedViewModel::class.java)

//        val activity = requireActivity() as MainActivity
//        activity.showLoginDialog()\

    }


}