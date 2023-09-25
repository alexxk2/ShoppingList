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
                created = formatCreatingData(created)
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

    private fun formatCreatingData(created: String): String {
        val year = created.take(4)
        val monthNumber = created.drop(5).take(2).trim().toInt()
        val day = created.drop(8).take(2)
        val time = created.dropWhile { it != ' ' }.dropLast(3).trim()

        val monthName = when (monthNumber) {
            1 -> "января"
            2 -> "февраля"
            3 -> "марта"
            4 -> "апреля"
            5 -> "мая"
            6 -> "июня"
            7 -> "июля"
            8 -> "августа"
            9 -> "сентября"
            10 -> "октября"
            11 -> "ноября"
            else -> "декабря"
        }

        return "$day $monthName $year $time"
    }
}