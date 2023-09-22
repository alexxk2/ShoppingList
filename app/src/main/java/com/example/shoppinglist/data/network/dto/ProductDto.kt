package com.example.shoppinglist.data.network.dto

import com.google.gson.annotations.SerializedName

data class ProductDto(
    val name: String,
    val id: Int = 0,
    @SerializedName("created") val quantity: String
)
