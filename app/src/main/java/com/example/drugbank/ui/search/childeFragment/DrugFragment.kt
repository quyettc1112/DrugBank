package com.example.drugbank.ui.search.childeFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private lateinit var _adapter: DrugAdapter
    lateinit var  tokenManager: TokenManager

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



        return _binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tokenManager = TokenManager(requireContext())
        _adapter = DrugAdapter()
        CallDrugList()

    }
    private fun CallDrugList() {
        adminDrugmRepository.getDrugMList(
            "Bearer ${tokenManager.getAccessToken()}",
            pageNo = 0,
            pageSize = 20,
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
                            type = drug.type
                        )
                    } ?: emptyList()


                    _adapter.differ.submitList(drugList)
                    _binding.rvDrugList.adapter = _adapter

                } else Log.d("CheckGetLIst", response.code().toString())
            }

            override fun onFailure(call: Call<DrugMListRespone>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun setUpRecycleView() {




    }


}