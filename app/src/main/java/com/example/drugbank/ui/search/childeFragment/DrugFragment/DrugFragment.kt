package com.example.drugbank.ui.search.childeFragment.DrugFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drugbank.R
import com.example.drugbank.common.Token.TokenManager
import com.example.drugbank.data.model.Drug
import com.example.drugbank.databinding.FragmentDrugBinding
import com.example.drugbank.repository.Admin_DrugM_Repository
import com.example.drugbank.respone.DrugMListRespone
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
       // _drugViewModel.resetSearchValue()
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
           Toast.makeText(requireContext(), it.name, Toast.LENGTH_SHORT).show()
        }





    }

    companion object {
        private const val PAGE_SIZE = 10
    }


}