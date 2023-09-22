package com.example.shoppinglist.presentation.lists.models

sealed interface ScreenState {
    object Content : ScreenState
    object Error : ScreenState
    object Loading : ScreenState
    object Intro: ScreenState
}