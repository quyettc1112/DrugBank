package com.example.drugbank.ui.setting

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
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
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import com.example.drugbank.R
import com.example.drugbank.base.dialog.ConfirmDialog
import com.example.drugbank.base.dialog.ErrorDialog
import com.example.drugbank.common.Resource.Screen
import com.example.drugbank.common.Token.TokenManager
import com.example.drugbank.common.constant.Constant
import com.example.drugbank.data.dto.UpdateUserRequestDTO
import com.example.drugbank.databinding.FragmentSettingBinding
import com.example.drugbank.repository.API_User_Repository
import com.example.drugbank.repository.Admin_UserM_Repository
import com.example.drugbank.respone.UserListResponse
import com.example.drugbank.ui.activity.auth.login.LoginActivity
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment() {


    private lateinit var viewModel: SettingViewModel
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private lateinit var tokenManager: TokenManager
    private lateinit var currentUser: UserListResponse.User

    @Inject
    lateinit var userRepository: Admin_UserM_Repository

    @Inject
    lateinit var apiUserService: API_User_Repository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUser = Constant.getCurrentUser(requireContext())!!
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        logout()
        tokenManager = TokenManager(requireContext())
        bindUserData()
        _binding!!.layoutSetting.setOnClickListener {
            showUserInfoDialog(currentUser)
        }

        binding.ivUserAvatar.setOnClickListener {
            contract.launch("image/*")
        }

        val rootView = binding.root
        return rootView
    }

    private val contract = registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
        if (result != null) {
            upLoadImage(result)
        } else {
            Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun upLoadImage(imageUri: Uri) {
        val filesDir = requireContext().applicationContext.filesDir
        val file = File(filesDir, "image.png")
        val inputStream = requireContext().contentResolver.openInputStream(imageUri)
        val outputStream = FileOutputStream(file)
        inputStream!!.copyTo(outputStream)

        val image = file.asRequestBody("image/*".toMediaTypeOrNull())
        val partImage = MultipartBody.Part.createFormData("file", file.name, image)

        apiUserService.uploadImage(
            authorization = "Bearer ${tokenManager.getAccessToken()}",
            email = currentUser.email.toString(),
            image = partImage
        ).enqueue(object : Callback<UserListResponse.User?> {
            override fun onResponse(
                call: Call<UserListResponse.User?>,
                response: Response<UserListResponse.User?>
            ) {
                if (response.isSuccessful) {
                    CallGetUserByEmail()
                } else {
                    Toast.makeText(requireContext(), "Upload failed", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<UserListResponse.User?>, t: Throwable) {
                Toast.makeText(requireContext(), "Upload failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }




    private fun bindUserData() {
        Picasso.get()
            .load(currentUser!!.avatar) // Assuming item.img is the URL string
            .placeholder(R.drawable.user_general) // Optional: Placeholder image while loading
            .error(R.drawable.user_general) // Optional: Error image to display on load failure
            .into(_binding!!.ivUserAvatar)

        _binding!!.userName.text = currentUser.fullname
    }

    private fun logout() {
        _binding!!.logout.setOnClickListener {
            Constant.removeAllSavedValues(requireContext())
            Constant.removeAllCurrentUser(requireContext())
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)


    }

    private fun CallUpdateUser(
        email: String,
        updateUserRequestDTO: UpdateUserRequestDTO
    ) {
        userRepository.UpdateUserInfo(
            "Bearer ${tokenManager.getAccessToken()}",
            email = email,
            updateUserRequestDTO
        ).enqueue(object : retrofit2.Callback<UserListResponse.User> {
            override fun onResponse(
                call: Call<UserListResponse.User>,
                response: Response<UserListResponse.User>
            ) {
                if (response.isSuccessful) {
                    Constant.removeAllCurrentUser(requireContext())
                    CallGetUserByEmail()
                } else {
                    val errorDialog = ErrorDialog(
                        context = requireContext(),
                        errorContent = "${response.code()}, ${response.message()}",
                        textButton = "Back"
                    )
                    // Show the ConfirmDialog
                    errorDialog.show()

                }
            }
            override fun onFailure(call: Call<UserListResponse.User>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun CallGetUserByEmail() {
        userRepository.getUserByEmail(
            authorization = "Bearer ${tokenManager.getAccessToken()}",
            email = currentUser.email.toString()
        ).enqueue(object : retrofit2.Callback<UserListResponse.User> {
            override fun onResponse(
                call: Call<UserListResponse.User>,
                response: Response<UserListResponse.User>
            ) {
                if (response.isSuccessful) {
                    val userRespone: UserListResponse.User? = response.body()
                    Constant.saveCurrentUser(requireContext(), userRespone!!)
                    currentUser = Constant.getCurrentUser(requireContext())!!
                    bindUserData()
                }
                else {
                    Log.d("CheckUser", response.code().toString())
                }
            }

            override fun onFailure(call: Call<UserListResponse.User>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun showUserInfoDialog(user: UserListResponse.User) {
        val dialogBinding = layoutInflater.inflate(R.layout.dialog_user_info, null)
        val myDialog = Dialog(requireContext())


        val username = dialogBinding.findViewById<TextView>(R.id.tv_userName)
        val id  = dialogBinding.findViewById<TextView>(R.id.tv_id)
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

    override fun onResume() {
        super.onResume()
        CallGetUserByEmail()
    }



}