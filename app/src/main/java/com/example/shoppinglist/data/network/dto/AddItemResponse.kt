package com.example.shoppinglist.data.network.dto

import com.google.gson.annotations.SerializedName

data class AddItemResponse(
    @SerializedName("item_id")val itemId: Int,
    val success: Boolean
)