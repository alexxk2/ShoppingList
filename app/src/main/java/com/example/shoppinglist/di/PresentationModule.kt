package com.example.shoppinglist.di

import com.example.shoppinglist.presentation.lists.view_model.ListsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    viewModel<ListsViewModel> {
        ListsViewModel(
            getAllShoppingListsUseCase = get(),
            createShoppingListUseCase = get(),
            deleteShoppingListUseCase = get(),
            restoreShoppingListUseCase = get(),
            getShoppingListUseCase = get()
        )
    }
}