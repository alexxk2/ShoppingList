package com.example.shoppinglist.data.network.dto

import com.google.gson.annotations.SerializedName

data class DeleteListResponse(
    @SerializedName("is_crossed")val isCrossed: Boolean,
    val success: Boolean
)