package com.example.shoppinglist.domain.details

import com.example.shoppinglist.domain.repositories.ShopRepository

class AddItemToShoppingListUseCase(private val shopRepository: ShopRepository) {

    suspend fun execute(listId: Int, itemName: String, quantity: Int): Boolean =
        shopRepository.addItemToShoppingList(
            listId, itemName, quantity
        )
}