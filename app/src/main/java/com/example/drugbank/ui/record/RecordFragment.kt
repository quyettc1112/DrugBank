package com.example.drugbank.ui.record

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drugbank.R
import com.example.drugbank.base.dialog.ErrorDialog
import com.example.drugbank.base.dialog.NotifyDialog
import com.example.drugbank.common.Token.TokenManager
import com.example.drugbank.common.constant.Constant
import com.example.drugbank.databinding.FragmentRecordBinding
import com.example.drugbank.repository.Admin_Profile_Repository
import com.example.drugbank.respone.ProductListRespone
import com.example.drugbank.respone.ProfileDetailRespone
import com.example.drugbank.respone.ProfileListRespone
import com.example.drugbank.respone.UserListResponse
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
    private lateinit var currentUser: UserListResponse.User

    @Inject
    lateinit var adminProfileRepository : Admin_Profile_Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokenManager = TokenManager(requireContext())
        viewModel = ViewModelProvider(this).get(RecordViewModel::class.java)
        currentUser = Constant.getCurrentUser(requireContext())!!

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecordBinding.inflate(inflater, container, false)
        _recordAdapter = RecordAdapter(requireContext())
        loadingUI()
        //CallProfileList()
       // CallProfileDetail()
        setUpRecycleViewList()


        setUpSearchQueries()


        return _binding.root
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

    private fun setUpRecycleViewList() {
        _binding.rclListProfile.adapter = _recordAdapter
        viewModel.resetAllValue()
        viewModel.setLoading(true)
        CallProfileList()
        _binding.rclListProfile.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastCompletelyVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount
                if (lastCompletelyVisibleItem == totalItemCount - 2) {
                    if (totalItemCount < viewModel.totalElement.value!!) {
                        viewModel.incrementCurrentPage()
                        CallProfileList()
                    }
                }
            }
        })
        _recordAdapter.onItemClick = {
            val navController = requireActivity().findNavController(R.id.nav_host_fragment_activity_main)
            viewModel.current_ID_Item.value = it.profileId
            val sharedPreferences = requireActivity().getSharedPreferences(Constant.CURRENT_PROFILE, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putInt(Constant.CURRENT_PROFILE_ID, it.profileId!!)
            editor.apply()
           // Toast.makeText(requireContext(), "${it.profileId}", Toast.LENGTH_SHORT).show()

            navController.navigate(Constant.getNavSeleted(Constant.RECORD_DETAIL))
        }
    }

    private fun setUpSearchQueries() {
        val searchView = _binding.searchView
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    viewModel.currentSearchValue.value = query
                    viewModel.resetAllValue()
                    CallProfileList()
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    if (newText?.length!! % 2 == 0) {
                        viewModel.currentSearchValue.value= newText
                        viewModel.resetAllValue()
                        CallProfileList()
                    }
                }
                if (newText?.length == 0) {
                    viewModel.resetSearchValue()
                    viewModel.resetAllValue()
                    CallProfileList()
                }
                return true
            }
        })
    }
    fun CallProfileList() {

        adminProfileRepository.getProfileList(
            authorization = "Bearer ${tokenManager.getAccessToken()}",
            pageSize = PAGE_SIZE,
            pageNo = viewModel.currentPage.value!!,
            search = viewModel.currentSearchValue.value.toString()
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
                                imageURL = profile?.imageURL.toString()
                            )
                        } ?: emptyList()

                    if (profileList.isEmpty() && currentUser.roleName == "SECRETARY") {
                        val notifyDialog = NotifyDialog(
                            requireContext(),
                            title = "List Is Empty",
                            message = "You have no Profile Product yet",
                            textButton = "Back")
                        notifyDialog.show()
                    } else {
                        viewModel.loadMoreRecord(profileList)
                        viewModel.totalElement.value = profileListRespone?.totalElements
                        _recordAdapter.differ.submitList(viewModel.currentProfileList.value)
                    }

                    viewModel.setLoading(false)
                }
                else  {
                    val errorDialog = ErrorDialog(
                        requireContext(),
                        textButton = "Back",
                        errorContent = "Error: ${response.code()}"
                    )
                    errorDialog.show()
                }
            }
            override fun onFailure(call: Call<ProfileListRespone>, t: Throwable) {
                val errorDialog = ErrorDialog(
                    requireContext(),
                    textButton = "Back",
                    errorContent = "Error: ${t.message}"
                )
                errorDialog.show()
            }
        })
    }


    companion object {
        private const val PAGE_SIZE = 20
    }

}