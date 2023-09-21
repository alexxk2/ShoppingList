package com.example.shoppinglist.data.converters

import com.example.shoppinglist.data.network.dto.ProductDto
import com.example.shoppinglist.data.network.dto.ShoppingListDto
import com.example.shoppinglist.domain.models.Product
import com.example.shoppinglist.domain.models.ShoppingList

class NetworkConverter {

    fun convertShoppingListToDomain(shoppingListDto: ShoppingListDto): ShoppingList {
        with(shoppingListDto) {

            return ShoppingList(
                id = id,
                name = name,
                created = created
            )
        }
    }

    fun convertProductToDomain(productDto: ProductDto): Product {

        with(productDto) {
            return Product(
                name = name,
                id = id,
                quantity = quantity
            )
        }
    }
}