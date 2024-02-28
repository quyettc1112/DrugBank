package com.example.drugbank.ui.search.childeFragment.ProductFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.drugbank.common.Token.TokenManager
import com.example.drugbank.common.constant.Constant
import com.example.drugbank.databinding.FragmentProductBinding
import com.example.drugbank.repository.Admin_ProductM_Repository
import com.example.drugbank.respone.ProductListRespone
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class ProductFragment : Fragment() {

    lateinit var _binding: FragmentProductBinding
    lateinit var productAdapter: ProductAdapter
    lateinit var  tokenManager: TokenManager
    lateinit var _productViewModel: ProductViewModel

    @Inject
    lateinit var adminProductRepository: Admin_ProductM_Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        tokenManager = TokenManager(requireContext())
        setUpProductList()
        CallProductList()


        return  _binding.root
    }

    private fun setUpProductList() {
        productAdapter = ProductAdapter()
        productAdapter.differ.submitList(Constant.getItemListForRecycleView_UserHome())
        _binding.rvItemUserHome.adapter = productAdapter
    }

    private fun CallProductList() {
        adminProductRepository.getProductList(
            authorization = "Bearer ${tokenManager.getAccessToken()}",
            pageNo = _productViewModel.currentPage.value!!,
            pageSize = PAGE_SIZE,
            sortField = _productViewModel.currentSorField.value.toString(),
            sortOrder = _productViewModel.currentSortBy.value.toString(),
            search =  _productViewModel.currentSearchValue.value.toString()
        ).enqueue( object: Callback<ProductListRespone> {
            override fun onResponse(
                call: Call<ProductListRespone>,
                response: Response<ProductListRespone>
            ) {
                if (response.isSuccessful) {
                    Log.d("CheckValueRespone", response.body().toString())


                } else {
                    Log.d("CheckValueRespone", response.code().toString())

                }
            }
            override fun onFailure(call: Call<ProductListRespone>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })




    }

    companion object {
        private const val PAGE_SIZE = 20
    }





}