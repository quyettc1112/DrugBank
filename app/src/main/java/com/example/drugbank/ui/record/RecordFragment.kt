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
import com.example.drugbank.repository.Admin_Profile_Repository
import com.example.drugbank.respone.ProfileDetailRespone
import com.example.drugbank.respone.ProfileListRespone
import com.example.drugbank.ui.activity.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.security.auth.callback.Callback

@AndroidEntryPoint
class RecordFragment : Fragment() {
    private lateinit var viewModel: RecordViewModel
    private lateinit var tokenManager: TokenManager

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
        CallProfileList()
        CallProfileDetail()

        return inflater.inflate(R.layout.fragment_record, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RecordViewModel::class.java)


    }


    fun CallProfileList() {
        adminProfileRepository.getProfileList(
            authorization = "Bearer ${tokenManager.getAccessToken()}",
            pageSize = 10,
            pageNo = 0,
            search = ""
        ).enqueue(object : retrofit2.Callback<ProfileListRespone> {
            override fun onResponse(
                call: Call<ProfileListRespone>,
                response: Response<ProfileListRespone>
            ) {
                if (response.isSuccessful) {
                    Log.d("CheckValueRespone", response.body().toString())
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
                    Log.d("CheckValueResponeDetail", response.body().toString())
                }
                else  Log.d("CheckValueResponeDetail", response.code().toString())
            }

            override fun onFailure(call: Call<ProfileDetailRespone>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

    }

}