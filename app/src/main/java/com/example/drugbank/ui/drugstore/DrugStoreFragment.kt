package com.example.drugbank.ui.drugstore

import android.app.Dialog
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.drugbank.R
import com.example.drugbank.base.customView.CustomToolbar
import com.example.drugbank.common.Resource.Screen
import com.example.drugbank.databinding.FragmentDrugStoreBinding
import com.example.drugbank.ui.activity.map.MapsActivity



import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DrugStoreFragment : Fragment() {


    private lateinit var viewModel: DrugStoreViewModel
    private lateinit var _binding: FragmentDrugStoreBinding

   // private lateinit var map: GoogleMap
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       _binding = FragmentDrugStoreBinding.inflate(inflater, container, false)

       _binding.changeToMap.setOnClickListener {
           val intent = Intent(requireContext(), MapsActivity::class.java)
           startActivity(intent)
       }


       _binding.s1.setOnClickListener {
           dialogStores(R.drawable.gn, "Công Ty Cổ Phần Dược Hậu Giang")
       }

       _binding.s2.setOnClickListener {
           dialogStores(R.drawable.r, "Công Ty Cổ Phần Dược Phẩm Gia Nguyễn")
       }

       _binding.s3.setOnClickListener {
           dialogStores(R.drawable.p, "Công Ty Cổ Phần Dược Phẩm Trung Ương I - Pharbaco")
       }


       _binding.s4.setOnClickListener {
           dialogStores(R.drawable.dmc, "Công Ty Cổ Phần Xuất Nhập Khẩu y tế Domesco")
       }



        return _binding.root

    }

    private fun dialogStores(imageResource: Int, companyName: String) {
        val dialogBinding = layoutInflater.inflate(R.layout.dialog_drug_s1, null)
        val myDialog = Dialog(requireContext())
        myDialog.setContentView(dialogBinding)
        myDialog.setCancelable(true)
        dialogBinding.findViewById<ImageView>(R.id.im_company).setImageResource(imageResource)
        dialogBinding.findViewById<TextView>(R.id.companyName).text = companyName
        myDialog.window?.setLayout(Screen.width, Screen.height)
        myDialog.show()

        dialogBinding.findViewById<CustomToolbar>(R.id.customToolbar).onStartIconClick = {
            myDialog.dismiss()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DrugStoreViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }
}