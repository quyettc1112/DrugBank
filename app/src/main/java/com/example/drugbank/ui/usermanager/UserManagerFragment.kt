package com.example.drugbank.ui.usermanager

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.res.Resources
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.drugbank.R
import com.example.drugbank.base.dialog.ConfirmDialog
import com.example.drugbank.base.dialog.ErrorDialog
import com.example.drugbank.base.dialog.NotifyDialog
import com.example.drugbank.common.BaseAPI.RetrofitClient
import com.example.drugbank.common.Resource.Screen
import com.example.drugbank.common.Token.TokenManager
import com.example.drugbank.data.dto.AddUserRequestDTO
import com.example.drugbank.data.dto.UpdateUserRequestDTO
import com.example.drugbank.data.model.User
import com.example.drugbank.databinding.FragmentSavedBinding
import com.example.drugbank.databinding.LayoutBottomSheetBinding
import com.example.drugbank.repository.Admin_UserM_Repository
import com.example.drugbank.respone.UserListResponse
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class UserManagerFragment : Fragment() {

   // private lateinit var binding:ActivityMainBinding
    private lateinit var _binding: FragmentSavedBinding
    private lateinit var _userAdapter: UserManagerAdapter

    lateinit var _viewModel: UserManagerViewModel
    lateinit var  tokenManager: TokenManager


    @Inject
    lateinit var userRepository: Admin_UserM_Repository



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this).get(UserManagerViewModel::class.java)

        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.layout_bottom_sheet, null)
        showBottomSheet(bottomSheetDialog, view)
        onAddNewClick()
        setUpUiStatic()
        _viewModel.setLoading(true)
        loadingUI()

        return _binding.root
    }

    private fun setUpUiStatic() {
        var isExpanded = true
        _binding.img.visibility = if (isExpanded) View.GONE else View.VISIBLE
        _binding.userGeneralInfo.setOnClickListener {
            isExpanded = !isExpanded
            val expandedHeight = 120.dpToPx()
            TransitionManager.beginDelayedTransition(_binding.root, AutoTransition())
            val newHeight = if (isExpanded) expandedHeight else 50.dpToPx()
            val layoutParams = _binding.userGeneralInfo.layoutParams
            layoutParams.height = newHeight
            _binding.userGeneralInfo.layoutParams = layoutParams
            _binding.img.visibility = if (isExpanded) View.GONE else View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _viewModel.setLoading(true)
        tokenManager = TokenManager(requireContext())
        _userAdapter = UserManagerAdapter(requireContext(), userRepository, tokenManager.getAccessToken().toString(), this@UserManagerFragment)

        onItemClickAdapter()
        searchViewOnQuery()
        val itemTouchHelper = ItemTouchHelper(_userAdapter.getSimpleCallBack())
        itemTouchHelper.attachToRecyclerView(_binding.rclListUser)
        CallUserList()
        updateStatic()

    }

    private fun updateStatic() {
        DefaultCallUserList()
        DefaultCallUserListGen()
        DefaultCallUserListAcctive()
    }

    private fun loadingUI() {
        _viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                _binding.pgIsLoading.visibility = View.VISIBLE
            } else {
                _binding.pgIsLoading.visibility = View.GONE
            }
        }
    }

    fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    private fun showBottomSheet(bottomSheetDialog:BottomSheetDialog, view: View) {
        _binding.imbFilter.setOnClickListener {

            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()

            val rolelist = resources.getStringArray(R.array.RoleName)
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu, rolelist)

            view.findViewById<AutoCompleteTextView>(R.id.atc_roleListCombo).let {
                it.setAdapter(arrayAdapter)
                it.setOnItemClickListener { parent, view, position, id ->
                    _viewModel.selectedRole.value = rolelist[position]
                    if (rolelist[position] == "SUPER ADMIN") {
                        _viewModel.selectedRole.value = "SUPERADMIN"
                    }
                    if (rolelist[position] == "ALL") {
                        _viewModel.selectedRole.value = ""
                    }
                    CallUserList()
                }
            }
            val genderList = resources.getStringArray(R.array.Gender)
            val arrayApderGender =
                ArrayAdapter(requireContext(), R.layout.dropdown_menu, genderList)
            view.findViewById<AutoCompleteTextView>(R.id.atc_genderList).let {
                it.setAdapter(arrayApderGender)
                it.setOnItemClickListener { parent, view, position, id ->
                    if (genderList[position] == "ALL") {
                        _viewModel.selectedGender.value = null
                    }
                    if (genderList[position] == "Male") {
                        _viewModel.selectedGender.value = 0
                    }
                    if (genderList[position] == "Female") {
                        _viewModel.selectedGender.value = 1
                    }
                    CallUserList()
                }
            }


            val activeList = resources.getStringArray(R.array.Active)
            val arrayApderActive =
                ArrayAdapter(requireContext(), R.layout.dropdown_menu, activeList)
            view.findViewById<AutoCompleteTextView>(R.id.atc_ActiveList).let {
                it.setAdapter(arrayApderActive)
                it.setOnItemClickListener { parent, view, position, id ->
                    _viewModel.selectedActive.value = activeList[position]
                    if (activeList[position] == "ALL") {
                        _viewModel.selectedActive.value = null
                    }
                    CallUserList()
                }
            }


            view.findViewById<AppCompatButton>(R.id.btn_save).setOnClickListener {
                CallUserList()
                bottomSheetDialog.dismiss()
            }

        }
    }
    public fun DefaultCallUserList() {
        RetrofitClient.instance_User.getPageableUser(
            "Bearer ${tokenManager.getAccessToken()}",
            pageNo = 0,
            pageSize = 100,
            sortField = "id",
            sortOrder = "asc",
            roleName = "",
            status = "",
            gender = null,
        ).enqueue(object : Callback<UserListResponse> {
            override fun onResponse(
                call: Call<UserListResponse>,
                response: Response<UserListResponse>
            ) {
                if (response.isSuccessful) {
                    val userResponse: UserListResponse? = response.body()
                    val totalElement = userResponse?.totalElements ?: 0 // Lấy totalElement từ response
                    _viewModel.user_count.value = totalElement
                    _binding.tvNumoofUser.text = "User Count: ${_viewModel.user_count.value}"
                } else {
                    println("Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                println("Failed to make API call: ${t.message}")
            }
        })

    }

    public fun DefaultCallUserListGen() {
        RetrofitClient.instance_User.getPageableUser(
            "Bearer ${tokenManager.getAccessToken()}",
            pageNo = 0,
            pageSize = 100,
            sortField = "id",
            sortOrder = "asc",
            roleName = "",
            status = "",
            gender = 0,
        ).enqueue(object : Callback<UserListResponse> {
            override fun onResponse(
                call: Call<UserListResponse>,
                response: Response<UserListResponse>
            ) {
                if (response.isSuccessful) {
                    val userResponse: UserListResponse? = response.body()
                    val totalElement = userResponse?.totalElements ?: 0 // Lấy totalElement từ response
                    _viewModel.user_gender.value = totalElement
                    _binding.tvNumofGender.text = "M: ${_viewModel.user_gender.value}, FM: ${_viewModel.user_count.value!! - _viewModel.user_gender.value!!}"
                } else {
                    println("Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                println("Failed to make API call: ${t.message}")
            }
        })

    }

    public fun DefaultCallUserListAcctive() {
        RetrofitClient.instance_User.getPageableUser(
            "Bearer ${tokenManager.getAccessToken()}",
            pageNo = 0,
            pageSize = 100,
            sortField = "id",
            sortOrder = "asc",
            roleName = "",
            status = "active",
            gender = null,
        ).enqueue(object : Callback<UserListResponse> {
            override fun onResponse(
                call: Call<UserListResponse>,
                response: Response<UserListResponse>
            ) {
                if (response.isSuccessful) {
                    val userResponse: UserListResponse? = response.body()
                    val totalElement = userResponse?.totalElements ?: 0 // Lấy totalElement từ response
                    _viewModel.user_active.value = totalElement
                    _binding.tvNumofActive.text = "Active:  ${_viewModel.user_active.value} / ${_viewModel.user_count.value}"
                } else {
                    println("Error: ${response.code()}")
                }
                _viewModel.setLoading(false)
            }
            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                println("Failed to make API call: ${t.message}")
            }
        })

    }

    public fun CallUserList() {
        RetrofitClient.instance_User.getPageableUser(
            "Bearer ${tokenManager.getAccessToken()}",
            pageNo = 0,
            pageSize = 30,
            sortField = "id",
            sortOrder = "asc",
            roleName = _viewModel.selectedRole.value,
            status = _viewModel.selectedActive.value,
            gender = _viewModel.selectedGender.value,
        ).enqueue(object : Callback<UserListResponse> {
            override fun onResponse(
                call: Call<UserListResponse>,
                response: Response<UserListResponse>
            ) {
                if (response.isSuccessful) {
                    val userResponse: UserListResponse? = response.body()
                    val userList: List<User> = userResponse?.content?.map { user ->
                        User(
                            id = user.id,
                            username = user.username,
                            email = user.email,
                            fullname = user.fullname,
                            dayOfBirth = user.dayOfBirth,
                            gender = user.gender,
                            roleName = user.roleName,
                            isActive = user.isActive,
                            avatar = user.avatar
                        )
                    } ?: emptyList()
                    updateStatic()
                    _userAdapter.differ.submitList(userList)
                    _binding.rclListUser.adapter = _userAdapter

                } else {
                    println("Error: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                println("Failed to make API call: ${t.message}")
            }
        })

    }

    private fun onItemClickAdapter() {
        _userAdapter.onItemClick = {
            showUserInfoDialog(it)
        }

    }

    private fun showUserInfoDialog(user: User) {
        val dialogBinding = layoutInflater.inflate(R.layout.dialog_user_info, null)
        val myDialog = Dialog(requireContext())

        val username = dialogBinding.findViewById<TextView>(R.id.tv_userName)
        val id   = dialogBinding.findViewById<TextView>(R.id.tv_id)
        val etEmail = dialogBinding.findViewById<TextInputEditText>(R.id.etEmail_userInfo)
        val et_fullname = dialogBinding.findViewById<TextInputEditText>(R.id.et_fullname)
        val et_dateofbirth = dialogBinding.findViewById<AppCompatButton>(R.id.btn_dob_uf)
        val ivUserAvatar = dialogBinding.findViewById<CircleImageView>(R.id.ivUserAvatar)

        val male = dialogBinding.findViewById<RadioButton>(R.id.rdo_btn_male)
        val female = dialogBinding.findViewById<RadioButton>(R.id.rdo_btn_female)
        male.isChecked = user.gender == 0
        female.isChecked = !male.isChecked

        username.text = user.username
        id.text ="ID: "+ user.id.toString()
        etEmail.setText(user.email)
        et_fullname.setText(user.fullname)
        et_dateofbirth.setText(user.dayOfBirth)
//        _viewModel.dob.observe(viewLifecycleOwner, Observer { dobValue ->
//            et_dateofbirth.setText(dobValue)
//        })
        et_dateofbirth.setOnClickListener { input ->
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, dayOfMonth)

                    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val formattedDate = dateFormat.format(calendar.time)
                    et_dateofbirth.setText(formattedDate)
                    _viewModel.updateDOB_UFValue(formattedDate)
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.setOnCancelListener { }
            datePickerDialog.setOnDismissListener {}

            datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
            datePickerDialog.show()
        }

        Picasso.get()
            .load(user.avatar) // Assuming item.img is the URL string
            .placeholder(R.drawable.user_general) // Optional: Placeholder image while loading
            .error(R.drawable.user_general) // Optional: Error image to display on load failure
            .into(ivUserAvatar)

        val rolename = dialogBinding.findViewById<AutoCompleteTextView>(R.id.atc_roleListCombo_info)
        val activeName = dialogBinding.findViewById<AutoCompleteTextView>(R.id.atc_ActiveList)

        val rolelist = resources.getStringArray(R.array.RoleName)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu, rolelist)
        rolename.setAdapter(arrayAdapter)
        rolename.setText(user.roleName)
        if (user.roleName =="SECRETARY") {
            rolename.setText("SECRETARY")
        } else rolename.setText(user.roleName)

        val activeList = resources.getStringArray(R.array.Active)
        val arrayApderActive = ArrayAdapter(requireContext(), R.layout.dropdown_menu, activeList)
        activeName.setAdapter(arrayApderActive)
        activeName.setText(user.isActive)

        myDialog.setContentView(dialogBinding)
        myDialog.setCancelable(true)
        myDialog.window?.setLayout(Screen.width, Screen.height)
       // myDialog.window?.setBackgroundDrawable(ColorDrawable(requireContext().getColor(R.color.zxing_transparent)))
        myDialog.show()

        onButtonClickDialog(dialogBinding, et_fullname, etEmail, et_dateofbirth, male, myDialog)
    }

    private fun onButtonClickDialog(
        dialogBinding: View,
        et_fullname: TextInputEditText,
        etEmail: TextInputEditText,
        et_dateofbirth: AppCompatButton,
        male: RadioButton,
        myDialog: Dialog
    ) {
        val btn_save = dialogBinding.findViewById<AppCompatButton>(R.id.btn_save)
        val btn_back = dialogBinding.findViewById<AppCompatButton>(R.id.btn_back)
        btn_save.setOnClickListener {
            if (!et_fullname.text.toString().isNullOrEmpty()) {
                val confirmDialog = ConfirmDialog(
                    requireContext(),
                    object : ConfirmDialog.ConfirmCallback {
                        override fun negativeAction() {}
                        override fun positiveAction() {
                            CallUpdateUser(
                                email = etEmail.text.toString(),
                                UpdateUserRequestDTO(
                                    et_fullname.text.toString(), et_dateofbirth.text.toString(),
                                    gender = if (male.isChecked) 0 else 1
                                )
                            )
                            myDialog.dismiss()
                        }
                    },
                    title = "Confirm",
                    message = "Save User Info",
                    positiveButtonTitle = "Yes",
                    negativeButtonTitle = "No"
                )
                confirmDialog.show()
            } else {
                val errorDialog = ErrorDialog(
                    context = requireContext(),
                    errorContent = "Error Null Input",
                    textButton = "Back"
                )
                // Show the ConfirmDialog
                errorDialog.show()

            }
        }
        btn_back.setOnClickListener {
            myDialog.dismiss()
        }
    }
    private fun CallUpdateUser(
        email: String,
        updateUserRequestDTO: UpdateUserRequestDTO
    ) {
        userRepository.UpdateUserInfo(
            "Bearer ${tokenManager.getAccessToken()}",
            email = email,
            updateUserRequestDTO
            ).enqueue(object : Callback<UserListResponse.User> {
            override fun onResponse(
                call: Call<UserListResponse.User>,
                response: Response<UserListResponse.User>
            ) {
                CallUserList()
            }

            override fun onFailure(call: Call<UserListResponse.User>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun searchViewOnQuery() {
        val searchView = _binding.searchView
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object :  androidx.appcompat.widget.SearchView.OnQueryTextListener
            {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }
                override fun onQueryTextChange(newText: String?): Boolean {
                    if (!newText.isNullOrBlank()) {
                        val currentList = _userAdapter.differ.currentList
                        val filteredList = currentList.filter { item ->
                            // Replace this with your actual filtering logic
                            item.fullname.contains(newText, ignoreCase = true)
                        }
                        _userAdapter.differ.submitList(filteredList)
                    }
                    if (newText?.length == 0) {
                        CallUserList()
                    }
                    return true
                }
            })
    }




    private fun onAddNewClick() {
//        val navController = requireActivity().findNavController(R.id.nav_host_fragment_activity_main)
//        navController.navigate(R.id.registerFragment)

        _binding.toolblarCustome.onEndIconClick = {
            val dialogBinding = layoutInflater.inflate(R.layout.activity_register, null)
            val myDialog = Dialog(requireContext())

            val etEmail = dialogBinding.findViewById<TextInputEditText>(R.id.etEmail)
            val etUsername = dialogBinding.findViewById<TextInputEditText>(R.id.etUsername)
            val etPassword = dialogBinding.findViewById<TextInputEditText>(R.id.etPassword)
            val etFullname = dialogBinding.findViewById<TextInputEditText>(R.id.etFullname)
            val btn_dob = dialogBinding.findViewById<AppCompatButton>(R.id.btn_dob)
            val btn_register = dialogBinding.findViewById<AppCompatButton>(R.id.btn_register)
            val btn_back = dialogBinding.findViewById<AppCompatButton>(R.id.btn_back)

            val atc_roleListCombo_info = dialogBinding.findViewById<AutoCompleteTextView>(R.id.atc_roleListCombo_info)
            val atc_ActiveList = dialogBinding.findViewById<AutoCompleteTextView>(R.id.atc_ActiveList)

            // Settup Gender
            val currentGender = 0;
            val male = dialogBinding.findViewById<RadioButton>(R.id.rdo_btn_male)
            val female = dialogBinding.findViewById<RadioButton>(R.id.rdo_btn_female)
            male.isChecked = true
            female.isChecked = !male.isChecked

            /// Setp Day og Birth
            _viewModel.dob.observe(viewLifecycleOwner, Observer { dobValue ->
                btn_dob.setText(dobValue)
            })
            btn_dob.setOnClickListener { input ->
                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { _, year, month, dayOfMonth ->
                        val calendar = Calendar.getInstance()
                        calendar.set(year, month, dayOfMonth)

                        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
                        val formattedDate = dateFormat.format(calendar.time)

                        _viewModel.updateDOBValue(formattedDate)
                    },
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                )
                datePickerDialog.setOnCancelListener { }
                datePickerDialog.setOnDismissListener {}

                datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 1000
                datePickerDialog.show()
            }
            // Settup Role
            var currentRole = 0;
            val rolelist = resources.getStringArray(R.array.RoleName_Create)
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu, rolelist)
            atc_roleListCombo_info.setAdapter(arrayAdapter)
            atc_roleListCombo_info.setOnItemClickListener {  _, _, position, _ ->
                currentRole = when (rolelist[position]) {
                    "ADMIN" -> 1
                    "SECRETARY" -> 2
                    else -> {
                        -1 // Default role set to -1
                    }
                }
            }

            etEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Được gọi trước khi văn bản trong etEmail thay đổi
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // Được gọi khi văn bản trong etEmail thay đổi
                    val email = s.toString()
                    if (!isEmailValid(email)) {
                        // Nếu email không hợp lệ, xử lý tương ứng ở đây
                        etEmail.error = "Incorrect Email"
                    } else {
                        // Nếu email hợp lệ, đảm bảo rằng không có thông báo lỗi được hiển thị
                        etEmail.error = null
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    // Được gọi sau khi văn bản trong etEmail thay đổi
                }
            })

            etUsername.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // Được gọi khi văn bản trong etEmail thay đổi
                    val etun = s.toString()
                    if(etun.isNullOrEmpty() || etun.length == 0) {
                        etUsername.error = "Not Null Value"
                    } else {
                        etUsername.error = null
                    }
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


                }

                override fun afterTextChanged(s: Editable?) {
                    // Được gọi sau khi văn bản trong etEmail thay đổi
                }
            })

            val email = etEmail.text.toString()
            if (!isEmailValid(email)) {
                // Nếu email không hợp lệ, hiển thị thông báo lỗi trực tiếp trên TextInputEditText
                etEmail.error = "Email không hợp lệ"
            } else {
                // Nếu email hợp lệ, đảm bảo rằng không có thông báo lỗi được hiển thị
                etEmail.error = null
            }
            // Setup Active
//            var currentActive = ""
//            val activeList = resources.getStringArray(R.array.Active_user_adapter)
//            val arrayApderActive = ArrayAdapter(requireContext(), R.layout.dropdown_menu, activeList)
//            atc_ActiveList.setAdapter(arrayApderActive)
//            atc_ActiveList.setOnItemClickListener { parent, view, position, id ->
//                currentActive = activeList[position]
//            }
            btn_register.setOnClickListener {
                if (
                    !etUsername.text.isNullOrEmpty() &&
                    !etEmail.text.isNullOrEmpty() &&
                    !etPassword.text.isNullOrEmpty()&&
                    !etFullname.text.isNullOrEmpty()&&
                    currentGender != null &&
                    currentRole != null
                ) {
                    val userAddDTO = AddUserRequestDTO(
                        email = etEmail.text.toString(),
                        username = etUsername.text.toString(),
                        fullName = etFullname.text.toString(),
                        dob = btn_dob.text.toString(),
                        gender = if (male.isChecked) 0 else 1,
                        roleID = currentRole
                    )
                    Log.d("CheckUserDTO", userAddDTO.toString())
                    CallRegisterUser(userAddDTO)

                } else {
                    val errorDialog = ErrorDialog(
                        errorContent = "Must fill all value",
                        textButton = "Back",
                        context = requireContext()
                    )
                    errorDialog.show()
                }

            }
            btn_back.setOnClickListener {
                myDialog.dismiss()
            }

            myDialog.setContentView(dialogBinding)
            myDialog.setCancelable(true)
            myDialog.window?.setLayout(Screen.width, Screen.height)
            myDialog.show()

        }
    }
    fun isEmailValid(email: String): Boolean {
        val pattern = Regex("^\\w+@gmail\\.com$")
        return pattern.matches(email)
    }

    private fun CallRegisterUser(userAddDTO : AddUserRequestDTO){
        userRepository.addUser(
            authorization = "Bearer ${tokenManager.getAccessToken()}"
            ,userAddDTO).enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    val notifyDialog = NotifyDialog(
                        requireContext(),
                        textButton = "OK",
                        message = "Add new User Success",
                        title = "Add User"
                    )
                    notifyDialog.show()
                    notifyDialog.setOnDismissListener {
                        CallUserList()
                    }
                }
                else {
                    val error = ErrorDialog(
                        context = requireContext(),
                        errorContent = response.code().toString(),
                        textButton = "Back"
                    )
                    error.show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }




}