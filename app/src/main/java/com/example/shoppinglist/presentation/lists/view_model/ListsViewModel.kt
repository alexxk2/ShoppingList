package com.example.shoppinglist.presentation.lists.view_model

import androidx.lifecycle.ViewModel
import com.example.shoppinglist.domain.lists.CreateShoppingListUseCase
import com.example.shoppinglist.domain.lists.DeleteShoppingListUseCase
import com.example.shoppinglist.domain.lists.GetAllShoppingListsUseCase
import com.example.shoppinglist.domain.lists.RestoreShoppingListUseCase

class ListsViewModel(
    private val getAllShoppingListsUseCase: GetAllShoppingListsUseCase,
    private val createShoppingListUseCase: CreateShoppingListUseCase,
    private val deleteShoppingListUseCase: DeleteShoppingListUseCase,
    private val restoreShoppingListUseCase: RestoreShoppingListUseCase

): ViewModel() {


}