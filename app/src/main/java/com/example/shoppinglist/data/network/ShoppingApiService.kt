package com.example.shoppinglist.data.network

import com.example.shoppinglist.data.network.dto.AddItemResponse
import com.example.shoppinglist.data.network.dto.CreateListResponse
import com.example.shoppinglist.data.network.dto.DeleteItemResponse
import com.example.shoppinglist.data.network.dto.DeleteListResponse
import com.example.shoppinglist.data.network.dto.GetListResponse
import com.example.shoppinglist.data.network.dto.GetListsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ShoppingApiService {

    @POST("/shopping/v2/CreateShoppingList")
    suspend fun createShoppingList(
        @Query("key") key: String,
        @Query("name") name: String
    ): Response<CreateListResponse>

    @POST("/shopping/v2/RemoveShoppingList")
    suspend fun deleteShoppingList(
        @Query("list_id") listId: Int
    ): Response<DeleteListResponse>

    @GET("/shopping/v2/GetAllMyShopLists")
    suspend fun getAllShoppingLists(
        @Query("key") key: String
    ): Response<GetListsResponse>

    @GET("/shopping/v2/GetShoppingList")
    suspend fun getShoppingList(
        @Query("list_id") listId: Int,
    ): Response<GetListResponse>

    @POST("/shopping/v2/AddToShoppingList")
    suspend fun addItemToShoppingList(
        @Query("id") listId: Int,
        @Query("value") name: String,
        @Query("n") quantity: Int
    ): Response<AddItemResponse>

    @POST("/shopping/v2/RemoveFromList")
    suspend fun deleteItemFromShoppingList(
        @Query("list_id") listId: Int,
        @Query("item_id") itemId: Int,
    ): Response<DeleteItemResponse>



    companion object{
        const val apiKey = "8DKTM7"
    }
}