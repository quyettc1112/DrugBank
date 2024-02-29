package com.example.drugbank.ui.search.childeFragment.ProductFragment.ProductDetail

import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.drugbank.R
import com.example.drugbank.common.Token.TokenManager
import com.example.drugbank.common.constant.Constant
import com.example.drugbank.databinding.FragmentProductDetailBinding
import com.example.drugbank.repository.Admin_ProductDetail_Repository
import com.example.drugbank.repository.Admin_ProductDetail_Service
import com.example.drugbank.repository.Admin_ProductM_Repository
import com.example.drugbank.respone.ProductDetailRespone
import com.example.drugbank.ui.search.childeFragment.ProductFragment.ProductViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {


    private lateinit var _binding : FragmentProductDetailBinding
    private var productID: Int = 0
    private lateinit var tokenManager: TokenManager
    private lateinit var _productDetailViewModel: ProductDetailViewModel

    @Inject
    lateinit var adminProductDetailRepository: Admin_ProductDetail_Repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        backToSearch()
        val sharedPreferences = requireActivity().getSharedPreferences(Constant.CURRENT_PRODUCT_ID, Context.MODE_PRIVATE)
        productID = sharedPreferences.getInt(Constant.CURRENT_PRODUCT_ID_VALUE, 0)
        tokenManager = TokenManager(requireContext())
        CallProductDetail(productID)
        setUpUiStatic()

        return _binding.root
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _productDetailViewModel = ViewModelProvider(this).get(ProductDetailViewModel::class.java)
    }

    private fun setUpUiStatic() {
        var isExpanded = false

        _binding.drugIExapnded.setOnClickListener {
            _binding.layoutShowDrugIngredient.visibility = if (isExpanded) View.VISIBLE else View.VISIBLE
            isExpanded = !isExpanded
            val expandedHeight = 200.dpToPx()
            TransitionManager.beginDelayedTransition(_binding.root, AutoTransition())
            val newHeight = if (isExpanded) expandedHeight else 0.dpToPx()
            val layoutParams = _binding.layoutShowDrugIngredient.layoutParams
            layoutParams.height = newHeight
            _binding.layoutShowDrugIngredient.layoutParams = layoutParams
            //_binding.drugIExapnded.visibility = if (isExpanded) View.GONE else View.VISIBLE

        }
    }
    fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
    private fun loadingUI() {
        _productDetailViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                _binding.pgIsLoading.visibility = View.VISIBLE
            } else {
                _binding.pgIsLoading.visibility = View.GONE
            }
        }
    }


    private fun CallProductDetail(id: Int){
        adminProductDetailRepository.getProductDetail(
            authorization = "Bearer ${tokenManager.getAccessToken()}",
            id = id
        ).enqueue(object: Callback<ProductDetailRespone>{
            override fun onResponse(
                call: Call<ProductDetailRespone>,
                response: Response<ProductDetailRespone>
            ) {
                _productDetailViewModel.setLoading(false)
                if (response.isSuccessful) {
                    val productDetailRespone: ProductDetailRespone? = response.body()
                    val productDetail: ProductDetailRespone? = productDetailRespone
                    Log.d("CheckValue", productDetail.toString())
                    handleSuccess(productDetailRespone)
                } else {
                    handleError()
                }
            }
            override fun onFailure(call: Call<ProductDetailRespone>, t: Throwable) {
                _productDetailViewModel.setLoading(false)
            }
        })
    }
    private fun backToSearch() {
        _binding.customToolbar.onStartIconClick = {
            val navController = requireActivity().findNavController(R.id.nav_host_fragment_activity_main)
            navController.navigate(Constant.getNavSeleted(Constant.SEARCH_NAV_ID))
        }
    }
    private fun handleSuccess(productDetail: ProductDetailRespone?) {
        loadingUI()
    }
    // Hàm xử lý khi gặp lỗi
    private fun handleError() {
        // Xử lý khi gặp lỗi, ví dụ hiển thị thông báo lỗi, thử lại, vv...
    }




}