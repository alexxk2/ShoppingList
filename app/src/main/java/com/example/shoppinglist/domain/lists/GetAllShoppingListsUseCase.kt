package com.example.shoppinglist.domain.lists

import com.example.shoppinglist.domain.models.ShoppingList
import com.example.shoppinglist.domain.repositories.ShopRepository

class GetAllShoppingListsUseCase(private val shopRepository: ShopRepository) {

    suspend fun execute(): List<ShoppingList> = shopRepository.getAllShoppingLists()
}