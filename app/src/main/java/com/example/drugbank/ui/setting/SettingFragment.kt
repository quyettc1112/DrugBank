package com.example.drugbank.ui.setting

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.drugbank.R
import com.example.drugbank.common.constant.Constant
import com.example.drugbank.databinding.FragmentSearchBinding
import com.example.drugbank.databinding.FragmentSettingBinding
import com.example.drugbank.ui.activity.auth.login.LoginActivity
import com.example.drugbank.ui.activity.main.MainActivity

class SettingFragment : Fragment() {


    private lateinit var viewModel: SettingViewModel
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

//        _binding!!.buttonLogout.setOnClickListener {
//            Constant.removeAllSavedValues(requireContext())
//            val intent = Intent(requireContext(),LoginActivity::class.java)
//            startActivity(intent)
//            requireActivity().finish()
//        }

        val rootView = binding.root

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)

//        val activity = requireActivity() as MainActivity
//        activity.showLoginDialog()
    }

}