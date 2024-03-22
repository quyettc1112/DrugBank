package com.example.drugbank.ui.search.childeFragment.ProductFragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
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
import com.example.drugbank.databinding.LayoutChoseCountryBinding
import com.example.drugbank.repository.Admin_ProductM_Repository
import com.example.drugbank.respone.DrugMListRespone
import com.example.drugbank.respone.ProductListRespone
import com.example.drugbank.respone.UserListResponse
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.card.MaterialCardView
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
    private var currentIDClcik: Int = 0

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
        _productViewModel.setLoading(true)

        showBottomSheet()
        loadingUI()
        val sharedPreferences = requireActivity().getSharedPreferences(Constant.CURRENT_FDA, Context.MODE_PRIVATE)
        val isBackFromDetail = sharedPreferences.getInt(Constant.CURRENT_FDA_VALUE, 0)

        if (isBackFromDetail != 0) {
            RESET_VIEWMODEL_VALUE()
            _productViewModel.resetAllValue()
            _binding.layoutIncludeHolder.visibility = View.GONE
            if (isBackFromDetail == 1) {
                _productViewModel.resetCheckCardValue()
                _productViewModel.isCheckedCard1.value = true
                _productViewModel.administration.value = 1
            }

            if (isBackFromDetail == 2) {
                _productViewModel.resetCheckCardValue()
                _productViewModel.isCheckedCard2.value = true
                _productViewModel.administration.value = 2

            }

            if (isBackFromDetail == 3) {
                _productViewModel.resetCheckCardValue()
                _productViewModel.isCheckedCard3.value = true
                _productViewModel.administration.value = 3
            }
            setUpRecycleViewList()
            showBottomSheet()
            setUpSearchQueries()
        } else {
            uiChooseFDA()
        }
        //uiChooseFDA()

        _binding.imvFlag.setOnClickListener {
            RESET_VIEWMODEL_VALUE()
            uiChooseFDA()
        }

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun uiChooseFDA() {
        _binding.layoutIncludeHolder.visibility = View.VISIBLE
        val includedLayout = _binding.layoutChoose
        var selectedCardId: Int? = null // Biến để lưu ID của card được chọn

        fun updateSaveButtonState() {
            // Nếu không có card nào được chọn, vô hiệu hóa nút save. Nếu không, kích hoạt nút đó.
            includedLayout.saveFDA.isEnabled = selectedCardId != null
            if (selectedCardId == null) {
                includedLayout.saveFDA.visibility = View.GONE
            } else includedLayout.saveFDA.visibility = View.VISIBLE
        }
        includedLayout.materialCardView2.setOnClickListener {
            if (selectedCardId != R.id.materialCardView2) { // Kiểm tra xem card hiện tại đã được chọn chưa
                selectedCardId = R.id.materialCardView2 // Đặt ID của card được chọn
                updateCardState(includedLayout, R.id.materialCardView2) // Cập nhật trạng thái của các card và checkbox
            } else { // Nếu card đang được chọn mà bạn click vào nó lần nữa
                selectedCardId = null // Bỏ chọn card
                includedLayout.materialCardView2.isChecked = false // Bỏ chọn checkbox của card
                includedLayout.checkboxcard1.visibility = View.GONE // Ẩn checkbox
                _productViewModel.resetAllValue()
            }
            updateSaveButtonState()
        }
        includedLayout.materialCardView3.setOnClickListener {
            if (selectedCardId != R.id.materialCardView3) {
                selectedCardId = R.id.materialCardView3
                updateCardState(includedLayout, R.id.materialCardView3)
            } else {
                selectedCardId = null
                includedLayout.materialCardView3.isChecked = false
                includedLayout.checkboxcard2.visibility = View.GONE
                _productViewModel.resetAllValue()
            }
            updateSaveButtonState()
        }
        includedLayout.materialCardView4.setOnClickListener {
            if (selectedCardId != R.id.materialCardView4) {
                selectedCardId = R.id.materialCardView4
                updateCardState(includedLayout, R.id.materialCardView4)
            } else {
                selectedCardId = null
                includedLayout.materialCardView4.isChecked = false
                includedLayout.checkboxcard3.visibility = View.GONE
                _productViewModel.resetAllValue()
            }
            updateSaveButtonState()
        }

        updateSaveButtonState()
        includedLayout.saveFDA.setOnClickListener {
            _binding.layoutIncludeHolder.visibility = View.GONE

            val sharedPreferences = requireActivity().getSharedPreferences(Constant.CURRENT_PRODUCT_ID, Context.MODE_PRIVATE)
            val currentFDA = requireActivity().getSharedPreferences(Constant.CURRENT_FDA, Context.MODE_PRIVATE)
            val isBackFromDetail = sharedPreferences.getInt(Constant.CURRENT_FDA_VALUE, 0)
            if (isBackFromDetail != currentIDClcik) {
                val editorFDA = currentFDA.edit()
                editorFDA.putInt(Constant.CURRENT_FDA_VALUE, currentIDClcik)
                editorFDA.apply()
            }

            setUpRecycleViewList()
            showBottomSheet()
            setUpSearchQueries()
            _productViewModel.setLoading(true)
            loadingUI()
        }
    }

    private fun updateCardState(includedLayout: LayoutChoseCountryBinding, selectedId: Int) {
        // Tắt tất cả các card và checkbox
        includedLayout.materialCardView2.isChecked = false
        includedLayout.materialCardView3.isChecked = false
        includedLayout.materialCardView4.isChecked = false
        includedLayout.checkboxcard1.visibility = View.GONE
        includedLayout.checkboxcard1.isChecked = false
        includedLayout.checkboxcard2.visibility = View.GONE
        includedLayout.checkboxcard2.isChecked = false
        includedLayout.checkboxcard3.visibility = View.GONE
        includedLayout.checkboxcard3.isChecked = false



        // Bật card và checkbox tương ứng với card được chọn
        when (selectedId) {
            R.id.materialCardView2 -> {
                includedLayout.materialCardView2.isChecked = true
                includedLayout.checkboxcard1.visibility = View.VISIBLE
                includedLayout.checkboxcard1.isChecked = true
                _productViewModel.resetCheckCardValue()
                _productViewModel.isCheckedCard1.value = true
                currentIDClcik = 1
                _productViewModel.administration.value = 1
            }
            R.id.materialCardView3 -> {
                includedLayout.materialCardView3.isChecked = true
                includedLayout.checkboxcard2.visibility = View.VISIBLE
                includedLayout.checkboxcard2.isChecked = true
                _productViewModel.resetCheckCardValue()
                _productViewModel.isCheckedCard2.value = true
                currentIDClcik = 2
                _productViewModel.administration.value = 2
            }
            R.id.materialCardView4 -> {
                includedLayout.materialCardView4.isChecked = true
                includedLayout.checkboxcard3.visibility = View.VISIBLE
                includedLayout.checkboxcard3.isChecked = true
                _productViewModel.resetCheckCardValue()
                _productViewModel.isCheckedCard3.value = true
                currentIDClcik = 3
                _productViewModel.administration.value = 3
            }
        }
    }

    private fun showBottomSheet() {
        _binding.imbFilter.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
            val view = layoutInflater.inflate(R.layout.layout_bottom_sheet_prouduct, null)
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
            var currentCheckedCheckbox: CheckBox? = null

            val materialCardViewFDA = view.findViewById<MaterialCardView>(R.id.materialCardView2_dialog)
            val checkBoxFDA = view.findViewById<CheckBox>(R.id.checkboxcard1_dia)
            materialCardViewFDA.setOnClickListener {
                if (currentCheckedCheckbox != checkBoxFDA) {
                    currentCheckedCheckbox?.isChecked = false
                    checkBoxFDA.isChecked = true
                    currentCheckedCheckbox = checkBoxFDA
                }
            }

            val materialCardViewANSM = view.findViewById<MaterialCardView>(R.id.materialCardView3_ANSM)
            val checkBoxANSM = view.findViewById<CheckBox>(R.id.checkboxcard2_ANSM)
            materialCardViewANSM.setOnClickListener {
                if (currentCheckedCheckbox != checkBoxANSM) {
                    currentCheckedCheckbox?.isChecked = false
                    checkBoxANSM.isChecked = true
                    currentCheckedCheckbox = checkBoxANSM
                }
            }

            val materialCardViewDAV = view.findViewById<MaterialCardView>(R.id.materialCardView4_DAV)
            val checkBoxDAV = view.findViewById<CheckBox>(R.id.checkboxcard3_DAV)
            materialCardViewDAV.setOnClickListener {
                if (currentCheckedCheckbox != checkBoxDAV) {
                    currentCheckedCheckbox?.isChecked = false
                    checkBoxDAV.isChecked = true
                    currentCheckedCheckbox = checkBoxDAV
                }
            }

            view.findViewById<AppCompatButton>(R.id.btn_save).setOnClickListener {
                bottomSheetDialog.dismiss()
                if (currentCheckedCheckbox == checkBoxFDA) {
                    _productViewModel.resetCheckCardValue()
                    RESET_VIEWMODEL_VALUE()
                    _productViewModel.isCheckedCard1.value = true
                    currentIDClcik = 1
                    _productViewModel.administration.value = 1
                    CallProductList()
                } else if (currentCheckedCheckbox == checkBoxANSM) {
                    _productViewModel.resetCheckCardValue()
                    RESET_VIEWMODEL_VALUE()
                    _productViewModel.isCheckedCard2.value = true
                    currentIDClcik = 2
                    _productViewModel.administration.value = 2
                    CallProductList()
                } else if (currentCheckedCheckbox == checkBoxDAV) {
                    _productViewModel.resetCheckCardValue()
                    RESET_VIEWMODEL_VALUE()
                    _productViewModel.isCheckedCard3.value = true
                    currentIDClcik = 3
                    _productViewModel.administration.value = 3
                    CallProductList()
                }
            }

        }
    }

    private fun setUpRecycleViewList() {
        _binding.rvItemUserHome.adapter = _productAdapter
        RESET_VIEWMODEL_VALUE()
        _productViewModel.resetAllValue()
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
        if (_productViewModel.isCheckedCard1.value == true) {
            CallProductList_NoFIll()
            _binding.imvFlag.setBackgroundResource(R.drawable.fda)
            _binding.textFlag.setText("FDA")
        } else if (_productViewModel.isCheckedCard2.value == true){
            CallProductList_NoFIll()
            _binding.imvFlag.setBackgroundResource(R.drawable.ansm)
            _binding.textFlag.setText("ANSM")
        } else if (_productViewModel.isCheckedCard3.value == true) {
            CallProductList_NoFIll()
            _binding.imvFlag.setBackgroundResource(R.drawable.dav)
            _binding.textFlag.setText("DAV")
        }

    }

    private fun CallProductList_NoFIll() {
        adminProductRepository.getProductList(
            authorization = "Bearer ${tokenManager.getAccessToken()}",
            pageNo = _productViewModel.currentPage.value!!,
            pageSize = PAGE_SIZE,
            sortField = _productViewModel.currentSorField.value.toString(),
            sortOrder = _productViewModel.currentSortBy.value.toString(),
            search = _productViewModel.currentSearchValue.value.toString(),
            administration = _productViewModel.administration.value!!
        ).enqueue(object : Callback<ProductListRespone> {
            override fun onResponse(
                call: Call<ProductListRespone>,
                response: Response<ProductListRespone>
            ) {
                if (response.isSuccessful) {
                    val productRespone: ProductListRespone? = response.body()
                    val productList: List<ProductListRespone.Content> =
                        productRespone?.content?.map { product ->
                            ProductListRespone.Content(
                                id = product.id,
                                category = product.category,
                                name = product.name,
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