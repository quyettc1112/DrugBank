package com.example.drugbank.ui.search.childeFragment.ProductFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drugbank.R
import com.example.drugbank.base.dialog.ErrorDialog
import com.example.drugbank.common.Token.TokenManager
import com.example.drugbank.common.constant.Constant
import com.example.drugbank.data.model.Drug
import com.example.drugbank.databinding.FragmentProductBinding
import com.example.drugbank.repository.Admin_ProductM_Repository
import com.example.drugbank.respone.DrugMListRespone
import com.example.drugbank.respone.ProductListRespone
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class ProductFragment : Fragment() {

    lateinit var _binding: FragmentProductBinding
    lateinit var _productAdapter: ProductAdapter
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
        _productAdapter = ProductAdapter()
        setUpRecycleViewList()
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.layout_bottom_sheet_prouduct, null)
        showBottomSheet(bottomSheetDialog,view)
        setUpSearchQueries()
        _productViewModel.setLoading(true)
        loadingUI()



        return _binding.root
    }

    private fun showBottomSheet(bottomSheetDialog:BottomSheetDialog, view: View) {
        _binding.imbFilter.setOnClickListener {

            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()

            val sortFeild_product = resources.getStringArray(R.array.sorrField_product)
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu, sortFeild_product)

            view.findViewById<AutoCompleteTextView>(R.id.atc_sortFeild_product).let {
                it.setAdapter(arrayAdapter)
                it.setOnItemClickListener { parent, view, position, id ->
                    val sortField = sortFeild_product[position]
                    val lowercaseSortField = when (sortField) {
                        "ID" -> "id"
                        "Labeller" -> "labeller"
                        "Name" -> "name"
                        "Route" -> "route"
                        "Pescription Name" -> "prescriptionName"
                        else -> sortField.toLowerCase()
                    }
                    _productViewModel.currentSorField.value = lowercaseSortField
                    RESET_VIEWMODEL_VALUE()
                    CallProductList()
                }
            }
            val sortby_product = resources.getStringArray(R.array.sortByUser)
            val arrayAdapter_SortBy = ArrayAdapter(requireContext(), R.layout.dropdown_menu, sortby_product)
            view.findViewById<AutoCompleteTextView>(R.id.atc_sortBy_product).let{
                it.setAdapter(arrayAdapter_SortBy)
                it.setOnItemClickListener { parent, view, position, id ->
                    _productViewModel.currentSortBy.value = when(sortby_product[position]) {
                        "ACS" -> "asc"
                        "DESC" -> "desc"
                        else -> {
                            ""
                        }
                    }
                    RESET_VIEWMODEL_VALUE()
                    CallProductList()
                }
            }
            view.findViewById<AppCompatButton>(R.id.btn_save).setOnClickListener {
                bottomSheetDialog.dismiss()
            }

        }
    }

    private fun setUpRecycleViewList() {
        _binding.rvItemUserHome.adapter = _productAdapter
        CallProductList()

        _binding.rvItemUserHome.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastCompletelyVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                if (lastCompletelyVisibleItem == totalItemCount - 5) {
                    if (totalItemCount < _productViewModel.totalElement.value!!) {
                        _productViewModel.incrementCurrentPage()
                        CallProductList()
                    }
                }
            }
        })
        _productAdapter.onItemClick = {
            val navController = requireActivity().findNavController(R.id.nav_host_fragment_activity_main)
            _productViewModel.current_ID_Item.value = it.id
            val sharedPreferences = requireActivity().getSharedPreferences(Constant.CURRENT_PRODUCT_ID, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt(Constant.CURRENT_PRODUCT_ID_VALUE, it.id)
            editor.apply()
            navController.navigate(Constant.getNavSeleted(5))
        }
    }

    private fun loadingUI() {
        _productViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                _binding.pgIsLoading.visibility = View.VISIBLE
            } else {
                _binding.pgIsLoading.visibility = View.GONE
            }
        }
    }

    private fun setUpSearchQueries() {
        val searchView = _binding.searchView
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    _productViewModel.currentSearchValue.value = query
                    RESET_VIEWMODEL_VALUE()
                    CallProductList()
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    if (newText?.length!! % 2 == 0) {
                        _productViewModel.currentSearchValue.value= newText
                        RESET_VIEWMODEL_VALUE()
                        CallProductList()
                    }
                }
                if (newText?.length == 0) {
                    _productViewModel.resetSearchValue()
                    RESET_VIEWMODEL_VALUE()
                    CallProductList()
                }
                return true
            }
        })
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
                    val productRespone: ProductListRespone? = response.body()
                    val productList: List<ProductListRespone.Content> = productRespone?.content?.map { product ->
                        ProductListRespone.Content(
                            id = product.id,
                            category = product.category,
                            name =product.name,
                            labeller = product.labeller,
                            company = product.company,
                            prescriptionName = product.prescriptionName,
                            route = product.route.toString(),
                            createdOn = product.createdOn.toString(),
                            image = product.image,
                            productAdministration = product.productAdministration
                        )
                    } ?: emptyList()
                    _productViewModel.loafMoreProductList(productList)
                    _productViewModel.totalElement.value = productRespone?.totalElements
                    _productAdapter.differ.submitList(_productViewModel.currentProductList.value)

                    _productViewModel.setLoading(false)
                } else {
                    val errorDialog = ErrorDialog(
                        requireContext(),
                        errorContent = "${response.code()} , {${response.errorBody()}}",
                        textButton = "Back"
                    )
                    errorDialog.show()
                }
            }
            override fun onFailure(call: Call<ProductListRespone>, t: Throwable) {
                val errorDialog = ErrorDialog(
                    requireContext(),
                    errorContent = "${t.cause} , {${t.message}}",
                    textButton = "Back"
                )
                errorDialog.show()
            }
        })
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
    fun RESET_VIEWMODEL_VALUE() {
        _productViewModel.resetAllValue()
        _productAdapter.differ.submitList(emptyList())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        RESET_VIEWMODEL_VALUE()
    }






}