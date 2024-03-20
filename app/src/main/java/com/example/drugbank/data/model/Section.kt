package com.example.drugbank.data.model

data class Section(
    val id: Int,
    val name: String,
    val isExpandable: Boolean,
    val items: List<Item>
) {
    data class Item(
        val id: Int,
        val name: String
    )
}
