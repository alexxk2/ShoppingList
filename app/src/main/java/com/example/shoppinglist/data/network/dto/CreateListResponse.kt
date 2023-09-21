package com.example.shoppinglist.data.network.dto

import com.google.gson.annotations.SerializedName

data class CreateListResponse(
    @SerializedName("list_id")val listId: Int,
    val success: Boolean
)