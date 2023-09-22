package com.example.shoppinglist.domain.lists

import com.example.shoppinglist.domain.models.Product
import com.example.shoppinglist.domain.repositories.ShopRepository

class GetShoppingListUseCase(private val shopRepository: ShopRepository) {

    suspend fun execute(id: Int): Pair<Boolean, List<Product>> = shopRepository.getShoppingList(id)
}