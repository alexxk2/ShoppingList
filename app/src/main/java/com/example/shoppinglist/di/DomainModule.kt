package com.example.shoppinglist.di

import com.example.shoppinglist.domain.details.AddItemToShoppingListUseCase
import com.example.shoppinglist.domain.details.DeleteItemFromShoppingListUseCase
import com.example.shoppinglist.domain.lists.CreateShoppingListUseCase
import com.example.shoppinglist.domain.lists.DeleteShoppingListUseCase
import com.example.shoppinglist.domain.lists.GetAllShoppingListsUseCase
import com.example.shoppinglist.domain.lists.GetShoppingListUseCase
import com.example.shoppinglist.domain.lists.RestoreShoppingListUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<AddItemToShoppingListUseCase> { AddItemToShoppingListUseCase(shopRepository = get()) }

    factory<DeleteItemFromShoppingListUseCase> { DeleteItemFromShoppingListUseCase(shopRepository = get()) }

    factory<CreateShoppingListUseCase> { CreateShoppingListUseCase(shopRepository = get()) }

    factory<DeleteShoppingListUseCase> { DeleteShoppingListUseCase(shopRepository = get()) }

    factory<GetAllShoppingListsUseCase> { GetAllShoppingListsUseCase(shopRepository = get()) }

    factory<GetShoppingListUseCase> { GetShoppingListUseCase(shopRepository = get()) }

    factory<RestoreShoppingListUseCase> { RestoreShoppingListUseCase(shopRepository = get()) }
}