package com.example.drugbank.ui.record

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.drugbank.R
import com.example.drugbank.common.Token.TokenManager
import com.example.drugbank.databinding.FragmentRecordBinding
import com.example.drugbank.repository.Admin_Profile_Repository
import com.example.drugbank.respone.ProductListRespone
import com.example.drugbank.respone.ProfileDetailRespone
import com.example.drugbank.respone.ProfileListRespone
import com.example.drugbank.ui.search.childeFragment.ProductFragment.ProductAdapter
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class RecordFragment : Fragment() {
    private lateinit var viewModel: RecordViewModel
    private lateinit var tokenManager: TokenManager
    private lateinit var _recordAdapter: RecordAdapter
    private lateinit var _binding: FragmentRecordBinding

    @Inject
    lateinit var adminProfileRepository : Admin_Profile_Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenManager = TokenManager(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecordBinding.inflate(inflater, container, false)
        _recordAdapter = RecordAdapter(requireContext())
        CallProfileList()
        CallProfileDetail()

        _binding.rclListProfile.adapter = _recordAdapter
        return _binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RecordViewModel::class.java)


    }
    fun CallProfileList() {
        adminProfileRepository.getProfileList(
            authorization = "Bearer ${tokenManager.getAccessToken()}",
            pageSize = PAGE_SIZE,
            pageNo = 0,
            search = ""
        ).enqueue(object : retrofit2.Callback<ProfileListRespone> {
            override fun onResponse(
                call: Call<ProfileListRespone>,
                response: Response<ProfileListRespone>
            ) {
                if (response.isSuccessful) {
                    val profileListRespone: ProfileListRespone? = response.body()
                    val profileList: List<ProfileListRespone.Content> =
                        profileListRespone?.content?.map { profile ->
                            ProfileListRespone.Content(
                                profileId = profile?.profileId,
                                title = profile?.title.toString(),
                                createdOn = profile?.createdOn.toString(),
                                createdBy = profile?.createdBy.toString(),
                                updatedBy = profile?.updatedBy.toString(),
                                updatedOn = profile?.updatedOn.toString(),
                                status = profile?.status.toString(),
                                imageURL = profile?.createdOn.toString()
                            )
                        } ?: emptyList()
                    viewModel.loadMoreRecord(profileList)
                    viewModel.totalElement.value = profileListRespone?.totalElements
                    _recordAdapter.differ.submitList(viewModel.currentProfileList.value)
                }
                else  Log.d("CheckValueRespone", response.code().toString())
            }

            override fun onFailure(call: Call<ProfileListRespone>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    fun CallProfileDetail() {
        adminProfileRepository.getProfileDetail(
            authorization = "Bearer ${tokenManager.getAccessToken()}",
            id = 1
        ).enqueue(object : retrofit2.Callback<ProfileDetailRespone> {
            override fun onResponse(
                call: Call<ProfileDetailRespone>,
                response: Response<ProfileDetailRespone>
            ) {
                if (response.isSuccessful) {
                    Log.d("CheckValueResponeDetai", response.body().toString())
                }
                else  Log.d("CheckValueResponeDetai", response.code().toString())
            }
            override fun onFailure(call: Call<ProfileDetailRespone>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }
    companion object {
        private const val PAGE_SIZE = 20
    }

}