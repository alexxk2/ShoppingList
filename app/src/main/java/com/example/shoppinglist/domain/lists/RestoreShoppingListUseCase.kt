package com.example.shoppinglist.domain.lists

import com.example.shoppinglist.domain.repositories.ShopRepository

class RestoreShoppingListUseCase(private val shopRepository: ShopRepository) {

    suspend fun execute(id: Int): Boolean = shopRepository.restoreShoppingList(id)
}