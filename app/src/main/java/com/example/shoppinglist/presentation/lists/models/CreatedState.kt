package com.example.shoppinglist.presentation.lists.models

sealed interface CreatedState {

    object Default : CreatedState
    data class Created(val listName: String) : CreatedState
    object NotCreated : CreatedState
}