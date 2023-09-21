package com.example.shoppinglist.domain.details

import com.example.shoppinglist.domain.repositories.ShopRepository

class DeleteItemFromShoppingListUseCase(private val shopRepository: ShopRepository) {

    suspend fun execute(listId: Int, itemId: Int): Boolean = shopRepository.deleteItemFromShoppingList(
        listId, itemId
    )
}