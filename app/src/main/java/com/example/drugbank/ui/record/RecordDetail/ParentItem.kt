package com.example.drugbank.ui.record.RecordDetail

import com.example.drugbank.respone.ProfileDetailRespone

data class ParentItem(
    val profileRespone: List<ProfileDetailRespone.ProfileDetail?>,
) {
}