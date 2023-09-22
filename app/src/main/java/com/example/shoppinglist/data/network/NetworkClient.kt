package com.example.shoppinglist.data.network

import com.example.shoppinglist.data.network.dto.ProductDto
import com.example.shoppinglist.data.network.dto.ShoppingListDto

interface NetworkClient {

    suspend fun createShoppingList(name: String): Boolean

    suspend fun deleteShoppingList(id: Int): Boolean

    suspend fun restoreShoppingList(id: Int): Boolean

    suspend fun getAllShoppingLists(): Pair<Boolean, List<ShoppingListDto>>

    suspend fun getShoppingList(id: Int): Pair<Boolean, List<ProductDto>>


    suspend fun addItemToShoppingList(listId: Int, itemName: String, quantity: Int): Boolean

    suspend fun deleteItemFromShoppingList(listId: Int, itemId: Int): Boolean
}