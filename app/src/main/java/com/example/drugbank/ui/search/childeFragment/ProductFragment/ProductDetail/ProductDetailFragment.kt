package com.example.drugbank.ui.search.childeFragment.ProductFragment.ProductDetail

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.findNavController
import com.example.drugbank.R
import com.example.drugbank.base.dialog.ConfirmDialog
import com.example.drugbank.base.dialog.ErrorDialog
import com.example.drugbank.base.dialog.NotifyDialog
import com.example.drugbank.common.Token.TokenManager
import com.example.drugbank.common.constant.Constant
import com.example.drugbank.databinding.FragmentProductDetailBinding
import com.example.drugbank.repository.API_User_Repository
import com.example.drugbank.repository.Admin_ProductDetail_Repository
import com.example.drugbank.respone.ProductDetailRespone
import com.example.drugbank.respone.ProductListRespone
import com.example.drugbank.respone.UserListResponse
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {


    private lateinit var _binding : FragmentProductDetailBinding
    private var productID: Int = 0
    private lateinit var tokenManager: TokenManager
    private lateinit var _productDetailViewModel: ProductDetailViewModel

    private lateinit var productTableAdapter: ProductTableAdapter
    private lateinit var producDetail_Authorites_Adapter: ProductDetail_Authorites_Adapter

    @Inject
    lateinit var adminProductDetailRepository: Admin_ProductDetail_Repository


    @Inject
    lateinit var apiUserRepository: API_User_Repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        _productDetailViewModel = ViewModelProvider(this).get(ProductDetailViewModel::class.java)
        backToSearch()
        val sharedPreferences = requireActivity().getSharedPreferences(Constant.CURRENT_PRODUCT_ID, Context.MODE_PRIVATE)
        productID = sharedPreferences.getInt(Constant.CURRENT_PRODUCT_ID_VALUE, 0)
        tokenManager = TokenManager(requireContext())
        _productDetailViewModel.setLoading(true)
        loadingUI()
        CallProductDetail(productID)
        setUpUiStatic()
        CallDeleteProduct()

        _binding.ivProductDetail.setOnClickListener {
            contract.launch("image/*")
        }


        return _binding.root
    }



    private val contract = registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
        if (result != null) {
            upLoadImage(result)
        } else {
            Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun upLoadImage(imageUri: Uri) {
        val filesDir = requireContext().applicationContext.filesDir
        val file = File(filesDir, "image.png")
        val inputStream = requireContext().contentResolver.openInputStream(imageUri)
        val outputStream = FileOutputStream(file)
        inputStream!!.copyTo(outputStream)

        val image = file.asRequestBody("image/*".toMediaTypeOrNull())
        val partImage = MultipartBody.Part.createFormData("file", file.name, image)

        apiUserRepository.upLoadImageProduct(
            authorization = "Bearer ${tokenManager.getAccessToken()}",
            ApprovalProductID = productID,
            image = partImage
        ).enqueue(object : Callback<ProductListRespone.Content?> {
            override fun onResponse(
                call: Call<ProductListRespone.Content?>,
                response: Response<ProductListRespone.Content?>
            ) {
                if (response.isSuccessful) {
                    //CallGetUserByEmail()
                    _productDetailViewModel.setLoading(true)
                    CallProductDetail(productID)
                    Log.d("CheckSuss", "Yes")
                } else {
                    Toast.makeText(requireContext(), "Upload failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProductListRespone.Content?>, t: Throwable) {
                Toast.makeText(requireContext(), "Upload failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun CallDeleteProduct() {
        _binding.acbtnProductDelete.setOnClickListener {
            val confirmDialog = ConfirmDialog(
                requireContext(),
                object : ConfirmDialog.ConfirmCallback {
                    override fun negativeAction() {}
                    override fun positiveAction() {
                        CallDeleteProduct(productID)
                    }
                },
                title = "Confirm",
                message = "Delete Product",
                positiveButtonTitle = "Yes",
                negativeButtonTitle = "No"
            )
            confirmDialog.show()
            confirmDialog.setOnDismissListener {
                val navController =
                    requireActivity().findNavController(R.id.nav_host_fragment_activity_main)
                navController.navigate(Constant.getNavSeleted(Constant.SEARCH_NAV_ID))
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun setUpUiStatic() {

        _binding.drugIExapnded?.let { drugExpanded ->

            drugExpanded.setUpExpansionToggle(_binding.layoutShowDrugIngredient, _binding.lineDrugIn, drugExpanded)
        }

        _binding.manufatoerExapnded?.let { manufacturerExpanded ->
            manufacturerExpanded.setUpExpansionToggle(_binding.layoutManufator, _binding.lineManufactor, manufacturerExpanded)
        }

        _binding.authoritiesExapnded?.let { authorExpane ->
            authorExpane.setUpExpansionToggle(_binding.layoutAuthorites, _binding.lineAuthorities, authorExpane)
        }

        _binding.pharmacogenomicExapnded?.let { phacrmarExpa ->
            phacrmarExpa.setUpExpansionToggle(_binding.layoutpharmacogenomic, _binding.linePharmacogenomic, phacrmarExpa)
        }

        _binding.productAllergyDetailExapnded?.let {pAllegry ->
            pAllegry.setUpExpansionToggle(_binding.layoutproductAllergyDetail, _binding.lineProductAllergyDetail, pAllegry)
        }

        _binding.contraindicationExapnded?.let { contrain ->
            contrain.setUpExpansionToggle(_binding.layoutcontraindication, _binding.lineContraindicationl, contrain)
        }
    }
    // Biến để lưu trữ dữ liệu height cho từng view
    private val originalHeights = mutableMapOf<View, Int>()

    private fun AppCompatImageView.setUpExpansionToggle(targetLayout: View, lineLayout: View, imageView: AppCompatImageView) {
        var isExpanded = false
        this.setOnClickListener {
            isExpanded = !isExpanded
            setExpandedStateAndToggleVisibility(targetLayout, lineLayout, isExpanded, imageView)
        }
    }

    private fun View.setExpandedStateAndToggleVisibility(targetLayout: View, lineLayout: View, isExpanded: Boolean, imageView: AppCompatImageView) {
        var originalHeight = originalHeights[targetLayout] ?: 0
        if (isExpanded) {
            targetLayout.visibility = View.VISIBLE
            targetLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    targetLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val expandedHeight = if (originalHeight != 0) originalHeight else targetLayout.height
                    if (originalHeight == 0) {
                        originalHeights[targetLayout] = expandedHeight
                    }
                    TransitionManager.beginDelayedTransition(_binding.root, AutoTransition())
                    val newHeight = expandedHeight
                    val layoutParams = targetLayout.layoutParams
                    layoutParams.height = newHeight
                    targetLayout.layoutParams = layoutParams
                    lineLayout.visibility = View.VISIBLE
                    imageView.setImageResource(R.drawable.baseline_arrow_drop_up_24)
                }
            })
        } else {
            TransitionManager.beginDelayedTransition(_binding.root, AutoTransition())
            // Đặt lại chiều cao của targetLayout thành chiều cao ban đầu
            val layoutParams = targetLayout.layoutParams
            layoutParams.height = originalHeight
            targetLayout.layoutParams = layoutParams

            targetLayout.visibility = View.GONE
            lineLayout.visibility = View.GONE
            imageView.setImageResource(R.drawable.baseline_arrow_drop_down_24)
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


    private fun CallDeleteProduct(id: Int) {
        adminProductDetailRepository.deleleProduct(
            authorization = "Bearer ${tokenManager.getAccessToken()}",
            id = id
        ).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                _productDetailViewModel.setLoading(true)
                if (response.isSuccessful) {
                    _productDetailViewModel.setLoading(false)
                    val notifyDialog = NotifyDialog(
                        requireContext(),
                        title =  "Delete Product",
                        message = "Delete Success",
                        textButton = "Back"
                    )
                    notifyDialog.show()

                } else{
                    val errorDialog = ErrorDialog(
                        requireContext(),
                        textButton = "Back",
                        errorContent = "${response.code()}"
                    )
                    errorDialog.show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

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
                if (response.isSuccessful) {
                    val productDetailRespone: ProductDetailRespone? = response.body()
                    val productDetail: ProductDetailRespone? = productDetailRespone
                    bindDataProductInfo(productDetail)
                    bindDataManufactor(productDetail)
                    bindDataPharmarcogenomic(productDetail)
                    bindDataAroductAllergyDetail(productDetail)
                    Picasso.get()
                        .load(productDetailRespone?.image) // Assuming item.img is the URL string
                        .placeholder(R.drawable.dafult_product_img) // Optional: Placeholder image while loading
                        .error(R.drawable.loading) // Optional: Error image to display on load failure
                        .into(_binding.ivProductDetail)
                    productTableAdapter = ProductTableAdapter(productDetailRespone!!.drugIngredients.toList())
                    _binding.rvDrugIngredients.adapter = productTableAdapter
                    Log.d("CheckDrugIn", productDetail!!.drugIngredients.toList().toString())

                    producDetail_Authorites_Adapter = ProductDetail_Authorites_Adapter(productDetailRespone!!.authorities.toList())
                    _binding.rvAuthorities.adapter = producDetail_Authorites_Adapter

                    _productDetailViewModel.setLoading(false)
                } else {
                    Log.d("CheckRsul", "${response.code()}, ${response.toString()}")
                }
            }
            override fun onFailure(call: Call<ProductDetailRespone>, t: Throwable) {
                _productDetailViewModel.setLoading(false)
                Log.d("CheckRsul T", "${t.message}")
            }
        })
    }

    private fun bindDataProductInfo(productResponse: ProductDetailRespone?) {
        _binding.productId.text = productResponse!!.id.toString()
        _binding.productName.text = productResponse!!.name.toString()
        _binding.productPrename.text = productResponse!!.prescriptionName.toString()
        _binding.prouductNameSmall.text = productResponse!!.name.toString()
        _binding.productLabeller.text = productResponse!!.labeller.toString()
        _binding.cateId.text = productResponse!!.category.id.toString()
        _binding.cateTitlee.text = productResponse!!.category.title.toString()
        _binding.cateSlug.text = productResponse!!.category.slug.toString()
    }

    private fun bindDataManufactor(productDetail: ProductDetailRespone?) {
        if (productDetail!!.manufactor != null) {
            _binding.manufactorName.text = productDetail.manufactor.name.toString()
            _binding.manufactorCompany.text = productDetail.manufactor.company.toString()
            _binding.manufactorScore.text = productDetail.manufactor.score.toString()
            _binding.manufactorSource.text = productDetail.manufactor.source.toString()
            _binding.manufactorCountryID.text = productDetail.manufactor.countryId.toString()
            _binding.manufactorCountryName.text = productDetail.manufactor.countryName.toString()
        }
    }
    private fun bindDataPharmarcogenomic(productDetail: ProductDetailRespone?) {
        if (productDetail!!.pharmacogenomic != null) {
            _binding.pharmarPharmar.text = productDetail.pharmacogenomic.pharmacodynamic.toString()
            _binding.pharmarAsorption.text = productDetail.pharmacogenomic.asorption.toString()
            _binding.pharmarIndication.text = productDetail.pharmacogenomic.indication.toString()
            _binding.pharmarToxicity.text = productDetail.pharmacogenomic.toxicity.toString()
            _binding.pharmarMechinbism.text = productDetail.pharmacogenomic.mechanismOfAction.toString()

        }
    }

    private fun bindDataAroductAllergyDetail(productDetail: ProductDetailRespone?) {
        if (productDetail!!.productAllergyDetail != null) {
            _binding.aadDetail.text = productDetail.productAllergyDetail.detail.toString()
            _binding.aadSumary.text = productDetail.productAllergyDetail.summary.toString()
        }
    }

    private fun backToSearch() {
        _binding.customToolbar.onStartIconClick = {
            val navController = requireActivity().findNavController(R.id.nav_host_fragment_activity_main)
            navController.navigate(Constant.getNavSeleted(Constant.SEARCH_NAV_ID))
        }
    }





}