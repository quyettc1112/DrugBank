package com.example.drugbank.ui.usermanager

import android.app.DatePickerDialog
import android.app.Dialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
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
import com.example.drugbank.repository.Admin_UserM_Repository
import com.example.drugbank.respone.UserListResponse
import com.google.android.material.textfield.TextInputEditText
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

    lateinit var _viewModel: UserManager
    lateinit var  tokenManager: TokenManager


    @Inject
    lateinit var userRepository: Admin_UserM_Repository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        _viewModel = ViewModelProvider(this).get(UserManager::class.java)
        onAddNewClick()
        setUpComboBoxWithViewmodel()

        return _binding.root
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tokenManager = TokenManager(requireContext())
        _userAdapter = UserManagerAdapter(requireContext(), userRepository, tokenManager.getAccessToken().toString(), this@UserManagerFragment)

        onItemClickAdapter()
        searchViewOnQuery()
        val itemTouchHelper = ItemTouchHelper(_userAdapter.getSimpleCallBack())
        itemTouchHelper.attachToRecyclerView(_binding.rclListUser)
        CallUserList()
    }

    public fun CallUserList() {
        RetrofitClient.instance_User.getPageableUser(
            "Bearer ${tokenManager.getAccessToken()}",
            pageNo = 0,
            pageSize = 20,
            sortField = "id",
            sortOrder = "asc",
            roleName = _viewModel.selectedRole.value,
            status = _viewModel.selectedActive.value,
            gender = _viewModel.selectedGender.value
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
                            isActive = user.isActive
                        )
                    } ?: emptyList()
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
        val et_dateofbirth = dialogBinding.findViewById<EditText>(R.id.et_dateofbirth)
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
        val avatarResId = if (user.gender == 0) R.drawable.anh_2 else R.drawable.anh_3
        ivUserAvatar.setImageResource(avatarResId)

        val rolename = dialogBinding.findViewById<AutoCompleteTextView>(R.id.atc_roleListCombo_info)
        val activeName = dialogBinding.findViewById<AutoCompleteTextView>(R.id.atc_ActiveList)

        val rolelist = resources.getStringArray(R.array.RoleName)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu, rolelist)
        rolename.setAdapter(arrayAdapter)
        rolename.setText(user.roleName)
        if (user.roleName =="SUPERADMIN") {
            rolename.setText("SUPER ADMIN")
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
        et_dateofbirth: EditText,
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
    private fun setUpComboBoxWithViewmodel() {
        val rolelist = resources.getStringArray(R.array.RoleName)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu, rolelist)
        _binding.atcRoleListCombo.setAdapter(arrayAdapter)

        _binding.atcRoleListCombo.setOnItemClickListener { _, _, position, _ ->
            _viewModel.selectedRole.value = rolelist[position]
            if (rolelist[position] == "SUPER ADMIN") {
                _viewModel.selectedRole.value = "SUPERADMIN"
            }
            CallUserList()
        }

        val genderList = resources.getStringArray(R.array.Gender)
        val arrayApderGender = ArrayAdapter(requireContext(), R.layout.dropdown_menu, genderList)
        _binding.atcGenderList.setAdapter(arrayApderGender)

        _binding.atcGenderList.setOnItemClickListener { _, _, position, _ ->
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

        val activeList = resources.getStringArray(R.array.Active)
        val arrayApderActive = ArrayAdapter(requireContext(), R.layout.dropdown_menu, activeList)
        _binding.atcActiveList.setAdapter(arrayApderActive)
        _binding.atcActiveList.setOnItemClickListener { _, _, position, _ ->
            _viewModel.selectedActive.value = activeList[position]
            if (activeList[position] == "ALL") {
                _viewModel.selectedActive.value = null
            }
            CallUserList()
        }
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
            val rolelist = resources.getStringArray(R.array.RoleName)
            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu, rolelist)
            atc_roleListCombo_info.setAdapter(arrayAdapter)
            atc_roleListCombo_info.setOnItemClickListener {  _, _, position, _ ->
                if (rolelist[position].equals("USER")) {
                    currentRole = 3;
                }
                if (rolelist[position].equals("ADMIN")) {
                    currentRole = 2;
                }
                if (rolelist[position].equals("SUPER ADMIN")) {
                    currentRole = 1;
                }
            }

            // Setup Active
            var currentActive = ""
            val activeList = resources.getStringArray(R.array.Active_user_adapter)
            val arrayApderActive = ArrayAdapter(requireContext(), R.layout.dropdown_menu, activeList)
            atc_ActiveList.setAdapter(arrayApderActive)
            atc_ActiveList.setOnItemClickListener { parent, view, position, id ->
                currentActive = activeList[position]
            }
            btn_register.setOnClickListener {
                if (
                    !etUsername.text.isNullOrEmpty() &&
                    !etEmail.text.isNullOrEmpty() &&
                    !etPassword.text.isNullOrEmpty()&&
                    !etFullname.text.isNullOrEmpty()&&
                    !currentActive.isNullOrEmpty() &&
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

    private fun CallRegisterUser(userAddDTO : AddUserRequestDTO){
        userRepository.addUser(userAddDTO).enqueue(object : Callback<String>{
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
                        errorContent = response.errorBody()!!.string(),
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