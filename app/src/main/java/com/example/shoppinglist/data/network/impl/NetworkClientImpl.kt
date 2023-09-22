package com.example.shoppinglist.data.network.impl

import com.example.shoppinglist.data.network.NetworkClient
import com.example.shoppinglist.data.network.ShoppingApiService
import com.example.shoppinglist.data.network.dto.ProductDto
import com.example.shoppinglist.data.network.dto.ShoppingListDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkClientImpl : NetworkClient {

    private val baseUrl = "https://cyberprot.ru"


    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: ShoppingApiService by lazy {
        retrofit.create(ShoppingApiService::class.java)
    }

    override suspend fun createShoppingList(name: String): Boolean {

        val response = retrofitService.createShoppingList(API_KEY, name)
        return response.code() == 200 && response.body()?.success == true
    }

    override suspend fun deleteShoppingList(id: Int): Boolean {

        val response = retrofitService.deleteShoppingList(id)
        return response.code() == 200 && response.body()?.success == true
    }

    override suspend fun restoreShoppingList(id: Int): Boolean {

        val response = retrofitService.deleteShoppingList(id)
        return response.code() == 200 && response.body()?.success == true

    }

    override suspend fun getAllShoppingLists(): Pair<Boolean, List<ShoppingListDto>> {

        val response = retrofitService.getAllShoppingLists(API_KEY)

        return if (response.code() == 200 && response.body()?.success == true) {
            Pair(true, response.body()?.shoppingLists ?: emptyList())
        } else Pair(false, emptyList())
    }

    override suspend fun getShoppingList(id: Int): Pair<Boolean, List<ProductDto>> {

        val response = retrofitService.getShoppingList(id)

        return if (response.code() == 200 && response.body()?.success == true) {
            Pair(true, response.body()?.productsList ?: emptyList())
        } else Pair(false, emptyList())

    }

    override suspend fun addItemToShoppingList(
        listId: Int,
        itemName: String,
        quantity: Int
    ): Boolean {

        val response = retrofitService.addItemToShoppingList(
            listId,
            itemName,
            quantity
        )
        return response.code() == 200 && response.body()?.success == true
    }

    override suspend fun deleteItemFromShoppingList(listId: Int, itemId: Int): Boolean {

        val response = retrofitService.deleteItemFromShoppingList(
            listId,
            itemId
        )

        return response.code() == 200 && response.body()?.success == true
    }

    companion object {
        const val API_KEY = "8DKTM7"
    }

}