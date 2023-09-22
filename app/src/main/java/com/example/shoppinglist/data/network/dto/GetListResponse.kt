package com.example.shoppinglist.data.network.dto

import com.google.gson.annotations.SerializedName

data class GetListResponse(
    @SerializedName("item_list")val productsList: List<ProductDto>,
    val success: Boolean
)