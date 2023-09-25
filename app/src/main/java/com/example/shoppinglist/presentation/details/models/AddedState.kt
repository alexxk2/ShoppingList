package com.example.shoppinglist.presentation.details.models

sealed interface AddedState {

    object Default : AddedState
    data class Added(val productName: String) : AddedState
    object NotAdded: AddedState
}