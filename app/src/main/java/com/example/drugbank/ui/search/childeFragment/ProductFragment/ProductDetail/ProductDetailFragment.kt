package com.example.drugbank.ui.search.childeFragment.ProductFragment.ProductDetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.drugbank.R
import com.example.drugbank.common.constant.Constant
import com.example.drugbank.databinding.FragmentProductDetailBinding

class ProductDetailFragment : Fragment() {


    private lateinit var _binding : FragmentProductDetailBinding
    private lateinit var _viewModel: ProductDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)

        _binding.customToolbar.onStartIconClick = {
            val navController = requireActivity().findNavController(R.id.nav_host_fragment_activity_main)
            navController.navigate(Constant.getNavSeleted(Constant.SEARCH_NAV_ID))
        }

        return _binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        _viewModel = ViewModelProvider(this).get(ProductDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

    

}