package com.example.shoppinglist.domain.repositories

import com.example.shoppinglist.domain.models.Product
import com.example.shoppinglist.domain.models.ShoppingList

interface ShopRepository {

    suspend fun createShoppingList(name: String): Boolean

    suspend fun deleteShoppingList(id: Int): Boolean

    suspend fun restoreShoppingList(id: Int): Boolean

    suspend fun getAllShoppingLists(): List<ShoppingList>

    suspend fun getShoppingList(id: Int): List<Product>




    suspend fun addItemToShoppingList(listId: Int, itemName: String, quantity: Int): Boolean

    suspend fun deleteItemFromShoppingList(listId: Int, itemId: Int): Boolean

}