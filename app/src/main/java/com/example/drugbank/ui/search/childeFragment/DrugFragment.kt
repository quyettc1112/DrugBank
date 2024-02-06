package com.example.drugbank.ui.search.childeFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drugbank.common.Token.TokenManager
import com.example.drugbank.data.model.Drug
import com.example.drugbank.databinding.FragmentDrugBinding
import com.example.drugbank.repository.Admin_DrugM_Repository
import com.example.drugbank.respone.DrugMListRespone
import com.example.drugbank.ui.usermanager.UserManagerViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@AndroidEntryPoint
class DrugFragment : Fragment() {

    private lateinit var _binding: FragmentDrugBinding
    private lateinit var _adapter: DrugAdapter
    lateinit var  tokenManager: TokenManager
    lateinit var drugViewModel: DrugViewModel

    private var currentPage: Int = 0

    @Inject
    lateinit var adminDrugmRepository: Admin_DrugM_Repository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDrugBinding.inflate(inflater, container, false)
        drugViewModel = ViewModelProvider(this).get(DrugViewModel::class.java)



        tokenManager = TokenManager(requireContext())
        _adapter = DrugAdapter()
        _binding.rvDrugList.adapter = _adapter
        CallDrugList()



        _binding.rvDrugList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                var lastCompletelyVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
                var totalItemCount = layoutManager.itemCount

                // Kiểm tra xem đã lướt tới phần tử cuối cùng hay chưa
                if (lastCompletelyVisibleItem == totalItemCount - 1) {
                    // Hiển thị Toast khi lướt tới phần tử cuối cùng
                    Toast.makeText(requireContext(), "Add More", Toast.LENGTH_SHORT).show()
                    //CallDrugLisv2()
                    CallDrugList()
                }
            }
        })

        return _binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }
    private fun CallDrugList() {

        adminDrugmRepository.getDrugMList(
            "Bearer ${tokenManager.getAccessToken()}",
            pageNo = currentPage,
            pageSize = PAGE_SIZE,
            sortField = "id",
            sortOrder = "asc",
            search =  ""
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
                    drugViewModel.loadMoreDruglist(drugList)
                    _adapter.differ.submitList(drugViewModel.currentDrugList.value)
                } else {
                    Log.d("CheckGetList", response.code().toString())
                }
            }

            override fun onFailure(call: Call<DrugMListRespone>, t: Throwable) {
            }
        })

        currentPage++
    }


    private fun setUpRecycleView() {
    }


    companion object {
        private const val PAGE_SIZE = 10
    }


}