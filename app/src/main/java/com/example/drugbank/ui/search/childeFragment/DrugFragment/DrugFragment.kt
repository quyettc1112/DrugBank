package com.example.drugbank.ui.search.childeFragment.DrugFragment

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drugbank.R
import com.example.drugbank.base.customView.CustomToolbar
import com.example.drugbank.base.dialog.ConfirmDialog
import com.example.drugbank.base.dialog.ErrorDialog
import com.example.drugbank.common.Resource.Screen
import com.example.drugbank.common.Token.TokenManager
import com.example.drugbank.data.dto.CreateDrugRequestDTO
import com.example.drugbank.data.dto.UpdateUserRequestDTO
import com.example.drugbank.data.model.Drug
import com.example.drugbank.databinding.CustomToolbarBinding
import com.example.drugbank.databinding.FragmentDrugBinding
import com.example.drugbank.repository.Admin_DrugM_Repository
import com.example.drugbank.respone.DrugMListRespone
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@AndroidEntryPoint
class DrugFragment : Fragment() {

    private lateinit var _binding: FragmentDrugBinding
    lateinit var _adapter: DrugAdapter
    lateinit var  tokenManager: TokenManager
    lateinit var _drugViewModel: DrugViewModel


    @Inject
    lateinit var adminDrugmRepository: Admin_DrugM_Repository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _drugViewModel = ViewModelProvider(this).get(DrugViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDrugBinding.inflate(inflater, container, false)
        tokenManager = TokenManager(requireContext())
        setUpRecycleView()
        setUpComboList()
        setUpSearchQueries()
        addNewDrug()
        onDrugItemClick()


        return _binding.root
    }

    private fun CallDrugList() {

        adminDrugmRepository.getDrugMList(
            "Bearer ${tokenManager.getAccessToken()}",
            pageNo = _drugViewModel.currentPage.value!!,
            pageSize = PAGE_SIZE,
            sortField = _drugViewModel.selectedSortField.value.toString(),
            sortOrder = _drugViewModel.selectefSortBy.value.toString(),
            search =  _drugViewModel.currentSearchValue.value.toString()
        ).enqueue(object: Callback<DrugMListRespone> {
            override fun onResponse(
                call: Call<DrugMListRespone>,
                response: Response<DrugMListRespone>
            ) {
                if (response.isSuccessful) {
                    val DrugResponse: DrugMListRespone? = response.body()
                    val drugList: List<Drug> = DrugResponse?.content?.map { drug ->
                        Drug(
                            id = drug.id,
                            approvalStatus = drug.approvalStatus,
                            clinicalDescription = drug.clinicalDescription,
                            description = drug.description,
                            drugbankId = null,
                            name = drug.name,
                            simpleDescription = drug.simpleDescription,
                            state = drug.state,
                            type = drug.type,
                            active = drug.active
                        )
                    } ?: emptyList()
                    Log.d("CurrentSearchValue", _drugViewModel.currentSearchValue.value.toString())
                    _drugViewModel.loadMoreDruglist(drugList)
                    _adapter.differ.submitList(_drugViewModel.currentDrugList.value)
                } else {
                    Log.d("CheckGetList", response.code().toString())
                }
            }
            override fun onFailure(call: Call<DrugMListRespone>, t: Throwable) {
            }
        })

    }


    private fun setUpRecycleView() {
        _adapter = DrugAdapter()
        _binding.rvDrugList.adapter = _adapter
        CallDrugList()
        _binding.rvDrugList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastCompletelyVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                if (lastCompletelyVisibleItem == totalItemCount - 1) {
                    _drugViewModel.incrementCurrentPage()
                    CallDrugList()
                }
            }
        })
    }

    private fun setUpComboList() {
        val sortField = resources.getStringArray(R.array.sortField_drug)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu, sortField)
        _binding.atcSortFieldListCombo.setAdapter(arrayAdapter)

        _binding.atcSortFieldListCombo.setOnItemClickListener { _, _, position, _ ->

            _drugViewModel.selectedSortField.value = when (sortField[position]) {
                "ID" -> "id"
                "Type" -> "type"
                "Name" -> "name"
                "State" -> "state"
                "Description" -> "description"
                "Simple Description" -> "simpleDescription"
                "Clinical Description" -> "clinicalDescription"
                else -> {
                    ""
                }
            }
            RESET_VIEWMODEL_VALUE()
            CallDrugList()
        }


        val sortByList = resources.getStringArray(R.array.sortOrder)
        val sortOderAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu, sortByList)
        _binding.atcSortByListCombo.setAdapter(sortOderAdapter)

        _binding.atcSortByListCombo.setOnItemClickListener { _, _, position, _ ->
            _drugViewModel.selectefSortBy.value = when(sortByList[position]) {
                "ACS" -> "asc"
                "DESC" -> "desc"
                else -> {
                    ""
                }
            }
            RESET_VIEWMODEL_VALUE()
            CallDrugList()
        }
    }

    private fun RESET_VIEWMODEL_VALUE() {
        _drugViewModel.emptyDrugList()
        _drugViewModel.resetCurrentPage()
        _adapter.differ.submitList(emptyList())
    }


    private fun setUpSearchQueries() {
        val searchView = _binding.searchView
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    _drugViewModel.currentSearchValue.value = query
                    RESET_VIEWMODEL_VALUE()
                    CallDrugList()
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    if (newText?.length!! % 2 == 0) {
                        _drugViewModel.currentSearchValue.value = newText
                        RESET_VIEWMODEL_VALUE()
                        CallDrugList()
                    }
                }
                if (newText?.length == 0) {
                    _drugViewModel.resetSearchValue()
                    RESET_VIEWMODEL_VALUE()
                    CallDrugList()
                }
                return true
            }
        })
    }
    private fun onDrugItemClick() {
        _adapter.onItemClick = {
            val dialogBinding = layoutInflater.inflate(R.layout.dialog_drug_info, null)
            val myDialog = Dialog(requireContext())


            dialogBinding.findViewById<TextView>(R.id.drugName).text = it.name
            dialogBinding.findViewById<TextView>(R.id.drubBankId).text ="ID: " + it.id.toString()
            dialogBinding.findViewById<TextView>(R.id.active).text = "Active: " + it.active
            dialogBinding.findViewById<TextView>(R.id.approvalStatus).text = it.approvalStatus.toString()
            dialogBinding.findViewById<TextView>(R.id.type).text = it.type
            dialogBinding.findViewById<TextView>(R.id.simpleDescription).text = it.simpleDescription
            dialogBinding.findViewById<TextView>(R.id.description).text = it.description
            dialogBinding.findViewById<TextView>(R.id.clinicalDescription).text = it.clinicalDescription
            dialogBinding.findViewById<TextView>(R.id.state).text = it.state

            val imageResource = if (it.active) R.drawable.background_drug_active else R.drawable.background_drug_deactive
            dialogBinding.findViewById<ImageView>(R.id.iv_drugactive).setImageResource(imageResource)

            if (it.active == false) {
                dialogBinding.findViewById<AppCompatButton>(R.id.btn_deleteDrug).visibility = View.GONE
            } else dialogBinding.findViewById<AppCompatButton>(R.id.btn_deleteDrug).visibility = View.VISIBLE

            dialogBinding.findViewById<AppCompatButton>(R.id.btn_deleteDrug).setOnClickListener {view ->
                val confirmDialog = ConfirmDialog(
                    requireContext(),
                    object : ConfirmDialog.ConfirmCallback {
                        override fun negativeAction() {}
                        override fun positiveAction() {
                            callDeleteDrug(it.id)
                            myDialog.dismiss()
                        }
                    },
                    title = "Confirm",
                    message = "Delete Drug Info",
                    positiveButtonTitle = "Yes",
                    negativeButtonTitle = "No"
                )
                confirmDialog.show()
            }
            dialogBinding.findViewById<CustomToolbar>(R.id.customToolbar).onStartIconClick = {
                myDialog.dismiss()
            }
            myDialog.setContentView(dialogBinding)
            myDialog.setCancelable(true)
            myDialog.window?.setLayout(Screen.width, 3000)
           // myDialog.window?.setBackgroundDrawable(ColorDrawable(requireContext().getColor(R.color.zxing_transparent)))
            myDialog.show()
        }
    }

    private fun callDeleteDrug(id: Int) {
        adminDrugmRepository.deleteDrug(
            "Bearer ${tokenManager.getAccessToken()}",
            id = id
        ).enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    RESET_VIEWMODEL_VALUE()
                    CallDrugList()
                }
                else  {
                    val errorDialog = ErrorDialog(requireContext(),
                        errorContent =  response.code().toString(),
                        textButton = "Back"
                        )
                    errorDialog.show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun addNewDrug() {
        _binding.ivAddnewDrug.setOnClickListener {
            val dialogBinding = layoutInflater.inflate(R.layout.dialog_addnew_drug, null)
            val myDialog = Dialog(requireContext())

            var currentStatus = 0
            val approvalList = resources.getStringArray(R.array.aproval)
            val arrayApprovalAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu, approvalList)
            dialogBinding.findViewById<AutoCompleteTextView>(R.id.atc_approvalstatus).let {
                it.setAdapter(arrayApprovalAdapter)
                it.setOnItemClickListener {_, _, position, _ ->
                    currentStatus = position
                }
            }

            val drugname = dialogBinding.findViewById<TextInputEditText>(R.id.et_drugname)
            val state = dialogBinding.findViewById<TextInputEditText>(R.id.etState)
            val description = dialogBinding.findViewById<TextInputEditText>(R.id.etDescription)
            val simpleDescription = dialogBinding.findViewById<TextInputEditText>(R.id.etSimpleDescription)
            val clinical = dialogBinding.findViewById<TextInputEditText>(R.id.etClinicalDescription)

            dialogBinding.findViewById<AppCompatButton>(R.id.btn_save).setOnClickListener {
                if ( !drugname.text.isNullOrEmpty() &&
                    !state.text.isNullOrEmpty() &&
                    !description.text.isNullOrEmpty() &&
                    !simpleDescription.text.isNullOrEmpty() &&
                    !clinical.text.isNullOrEmpty()
                ) {
                    val createDrugRequestDTO = CreateDrugRequestDTO(
                        id =  0,
                        name = drugname.text.toString(),
                        description = description.text.toString(),
                        state = state.text.toString(),
                        simpleDescription = simpleDescription.text.toString(),
                        clinicalDescription = clinical.text.toString(),
                        approvalStatus =  currentStatus,
                        type = "Capsule"
                    )
                    val confirmDialog = ConfirmDialog(
                        requireContext(),
                        object : ConfirmDialog.ConfirmCallback {
                            override fun negativeAction() {}
                            override fun positiveAction() {
                                callCreateDrug(createDrugRequestDTO)
                                CallDrugList()
                                myDialog.dismiss()
                            }
                        },
                        title = "Confirm",
                        message = "Save Drug Info",
                        positiveButtonTitle = "Yes",
                        negativeButtonTitle = "No"
                    )
                    confirmDialog.show()
                } else {
                    val errorDialog = ErrorDialog(
                        errorContent = "Must fill all value",
                        textButton = "Back",
                        context = requireContext()
                    )
                    errorDialog.show()
                }
            }

            myDialog.setContentView(dialogBinding)
            myDialog.setCancelable(true)
            myDialog.window?.setLayout(Screen.width, 3000)
            // myDialog.window?.setBackgroundDrawable(ColorDrawable(requireContext().getColor(R.color.zxing_transparent)))
            myDialog.show()
            dialogBinding.findViewById<CustomToolbar>(R.id.customToolbar).onStartIconClick = {
                myDialog.dismiss()
            }
            dialogBinding.findViewById<AppCompatButton>(R.id.btn_back).setOnClickListener {
                myDialog.dismiss()
            }

        }
    }


    private fun callCreateDrug(createDrugRequestDTO: CreateDrugRequestDTO) {
        adminDrugmRepository.createDrug(
            "Bearer ${tokenManager.getAccessToken()}",
            createDrugRequestDTO = createDrugRequestDTO
        ).enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("CheckCode", response.message().toString())
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }

    companion object {
        private const val PAGE_SIZE = 10
    }


}