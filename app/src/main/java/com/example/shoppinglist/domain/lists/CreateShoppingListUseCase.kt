package com.example.shoppinglist.domain.lists

import com.example.shoppinglist.domain.repositories.ShopRepository

class CreateShoppingListUseCase(private val shopRepository: ShopRepository) {

    suspend fun execute(name: String): Boolean = shopRepository.createShoppingList(name)
}