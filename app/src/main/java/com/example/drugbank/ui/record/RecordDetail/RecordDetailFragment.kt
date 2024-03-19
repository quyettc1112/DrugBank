package com.example.drugbank.ui.record.RecordDetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.drugbank.R
import com.example.drugbank.common.constant.Constant
import com.example.drugbank.databinding.FragmentRecordBinding
import com.example.drugbank.databinding.FragmentRecordDetailBinding

class RecordDetailFragment : Fragment() {


    private lateinit var viewModel: RecordDetailViewModel
    private lateinit var _binding: FragmentRecordDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentRecordDetailBinding.inflate(inflater, container, false)

        _binding.toolblarCustome.onStartIconClick = {
            val navController = requireActivity().findNavController(R.id.nav_host_fragment_activity_main)
            navController.navigate(Constant.getNavSeleted(Constant.RECORD_NAV_ID))
        }


        return  _binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RecordDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}