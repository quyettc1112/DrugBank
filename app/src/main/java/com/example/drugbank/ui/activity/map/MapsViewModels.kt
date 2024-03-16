package com.example.drugbank.ui.activity.map

import androidx.lifecycle.MutableLiveData
import com.example.healthcarecomp.base.BaseViewModel

class MapsViewModels : BaseViewModel()  {

    val nearBy = MutableLiveData<String>()

    init {
        nearBy.value = "drugstore"
    }

}