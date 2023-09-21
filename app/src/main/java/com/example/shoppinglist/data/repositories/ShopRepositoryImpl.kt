package com.example.shoppinglist.data.repositories

import com.example.shoppinglist.data.converters.NetworkConverter
import com.example.shoppinglist.data.network.NetworkClient
import com.example.shoppinglist.domain.models.Product
import com.example.shoppinglist.domain.models.ShoppingList
import com.example.shoppinglist.domain.repositories.ShopRepository

class ShopRepositoryImpl(
    private val networkClient: NetworkClient,
    private val networkConverter: NetworkConverter
) : ShopRepository {

    override suspend fun createShoppingList(name: String): Boolean =
        networkClient.createShoppingList(name)

    override suspend fun deleteShoppingList(id: Int): Boolean = networkClient.deleteShoppingList(id)

    override suspend fun restoreShoppingList(id: Int): Boolean =
        networkClient.restoreShoppingList(id)

    override suspend fun getAllShoppingLists(): List<ShoppingList> {

        val networkResponse = networkClient.getAllShoppingLists()
        return networkResponse.map { shoppingListDto ->
            networkConverter.convertShoppingListToDomain(shoppingListDto)
        }

    }

    override suspend fun getShoppingList(id: Int): List<Product> {

        val networkResponse = networkClient.getShoppingList(id)
        return networkResponse.map { productDto ->
            networkConverter.convertProductToDomain(productDto)
        }

    }

    override suspend fun addItemToShoppingList(
        listId: Int,
        itemName: String,
        quantity: Int
    ): Boolean = networkClient.addItemToShoppingList(listId, itemName, quantity)

    override suspend fun deleteItemFromShoppingList(listId: Int, itemId: Int): Boolean =
        networkClient.deleteItemFromShoppingList(listId, itemId)
}