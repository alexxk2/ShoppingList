package com.example.shoppinglist.domain.lists

import com.example.shoppinglist.domain.repositories.ShopRepository

class DeleteShoppingListUseCase(private val shopRepository: ShopRepository) {

    suspend fun execute(id: Int): Boolean = shopRepository.deleteShoppingList(id)
}