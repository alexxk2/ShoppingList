package com.example.shoppinglist.data.network.dto

import com.google.gson.annotations.SerializedName

data class GetListsResponse(
    @SerializedName("shop_list") val shoppingLists: List<ShoppingListDto>,
    val success: Boolean
)
