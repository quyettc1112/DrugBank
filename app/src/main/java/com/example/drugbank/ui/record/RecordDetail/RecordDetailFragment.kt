package com.example.drugbank.ui.record.RecordDetail

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drugbank.R
import com.example.drugbank.base.dialog.ErrorDialog
import com.example.drugbank.common.Token.TokenManager
import com.example.drugbank.common.constant.Constant
import com.example.drugbank.databinding.FragmentRecordDetailBinding
import com.example.drugbank.repository.Admin_Profile_Repository
import com.example.drugbank.respone.ProfileDetailRespone
import com.example.drugbank.ui.activity.main.MainActivity
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class RecordDetailFragment : Fragment() {


    private lateinit var viewModel: RecordDetailViewModel
    private lateinit var _binding: FragmentRecordDetailBinding
    private lateinit var tokenManager: TokenManager

    private lateinit var profileDetailApapter: ProfileDetailAdapter

    private var profileID: Int = 0

    @Inject
    lateinit var adminProfileRepository : Admin_Profile_Repository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RecordDetailViewModel::class.java)
        tokenManager = TokenManager(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentRecordDetailBinding.inflate(inflater, container, false)

        onBackClick()
        loadingUI()
        setUpCallprofileDetail()

        _binding.parentRecycleView.let {
            it.setHasFixedSize(true)
            it.layoutManager = LinearLayoutManager(requireContext())
        }






        return  _binding.root
    }



    private fun loadingUI() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                _binding.pgIsLoading.visibility = View.VISIBLE
            } else {
                _binding.pgIsLoading.visibility = View.GONE
            }
        }
    }
    private fun setUpCallprofileDetail() {
        val sharedPreferences =
            requireActivity().getSharedPreferences(Constant.CURRENT_PROFILE, Context.MODE_PRIVATE)
        profileID = sharedPreferences.getInt(Constant.CURRENT_PROFILE_ID, 0)
        if (profileID != 0) {
            CallProfileDetail()
        }
    }

    private fun onBackClick() {
        _binding.toolblarCustome.onStartIconClick = {
            val navController =
                requireActivity().findNavController(R.id.nav_host_fragment_activity_main)
            navController.navigate(Constant.getNavSeleted(Constant.RECORD_NAV_ID))
        }
    }

    fun CallProfileDetail() {
        viewModel.setLoading(true)
        adminProfileRepository.getProfileDetail(
            authorization = "Bearer ${tokenManager.getAccessToken()}",
            id = profileID
        ).enqueue(object : retrofit2.Callback<ProfileDetailRespone> {
            override fun onResponse(
                call: Call<ProfileDetailRespone>,
                response: Response<ProfileDetailRespone>
            ) {
                if (response.isSuccessful) {
                    val productDetailRespone: ProfileDetailRespone? = response.body()
                    val profileInformation: ProfileDetailRespone.ProfileInformation? =
                        productDetailRespone?.profileInformation
                    profileInformation(profileInformation)
//                    parentAdapter = ParentAdapter((productDetailRespone?.profileDetailList))
//                    _binding.parentRecycleView.adapter = parentAdapter
                    profileDetailInfo(productDetailRespone)

                }
                else  {
                    val errorDialog = ErrorDialog(
                        requireContext(),
                        textButton = "Back",
                        errorContent = "${response.code()}"
                    )
                    errorDialog.show()
                }
                viewModel.setLoading(false)
            }
            override fun onFailure(call: Call<ProfileDetailRespone>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun profileDetailInfo(productDetailRespone: ProfileDetailRespone?) {
        profileDetailApapter =
            ProfileDetailAdapter(productDetailRespone?.profileDetailList, requireContext(),
                requireActivity() as MainActivity
            )
        _binding.parentRecycleView.adapter = profileDetailApapter

        profileDetailApapter.onItemClick = {


        }

    }

    fun profileInformation(profileInformation: ProfileDetailRespone.ProfileInformation?) {
        _binding.tvProfileNameDetai.text = profileInformation?.title.toString()
        _binding.tvCreateByPfDetail.text = profileInformation?.createdBy.toString()
        _binding.tvCreateOnPfDetail.text = profileInformation?.createdOn.toString()
        _binding.tvUpdateByPfDetail.text = profileInformation?.updatedBy.toString()
        _binding.tvUpdateOnPfDetail.text = profileInformation?.updatedOn.toString()

        if (profileInformation?.status == "CLOSED") {
            _binding.tvStatusPfDetail.text = profileInformation.status
            _binding.tvStatusPfDetail.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red_light))
        }
        if (profileInformation?.status == "PENDING") {
            _binding.tvStatusPfDetail.text = profileInformation.status
            _binding.tvStatusPfDetail.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.icon_color_bottom_normal))
        }

        if (profileInformation?.imageURL.isNullOrEmpty()) {
            _binding.ivProfileDetail.setImageResource(R.drawable.dafult_product_img)

        } else {
            Picasso.get()
                .load(profileInformation?.imageURL) // Assuming item.img is the URL string
                .placeholder(R.drawable.loadingsim) // Optional: Placeholder image while loading
                .error(R.drawable.dafult_product_img) // Optional: Error image to display on load failure
                .into(_binding.ivProfileDetail)

        }


    }

    fun profileDetailListInfor () {


    }


}