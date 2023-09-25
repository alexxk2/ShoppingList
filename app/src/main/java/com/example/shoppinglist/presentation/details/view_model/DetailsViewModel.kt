package com.example.shoppinglist.presentation.details.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.domain.details.AddItemToShoppingListUseCase
import com.example.shoppinglist.domain.details.DeleteItemFromShoppingListUseCase
import com.example.shoppinglist.domain.lists.GetShoppingListUseCase
import com.example.shoppinglist.domain.models.Product
import com.example.shoppinglist.presentation.details.models.AddedState
import com.example.shoppinglist.presentation.lists.models.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailsViewModel(

    private val getShoppingListUseCase: GetShoppingListUseCase,
    private val addItemToShoppingListUseCase: AddItemToShoppingListUseCase,
    private val deleteItemFromShoppingListUseCase: DeleteItemFromShoppingListUseCase
) : ViewModel() {


    private val _screenState = MutableLiveData<ScreenState>()
    val screenState: LiveData<ScreenState> = _screenState

    private val _listOfProducts = MutableLiveData<List<Product>>()
    val listOfProducts: LiveData<List<Product>> = _listOfProducts

    private val _addedState = MutableLiveData<AddedState>()
    val addedState: LiveData<AddedState> = _addedState

    private var listId = 0
    private var itemId = 0

    private var poolingJob: Job? = null
    private var isPooling: Boolean = false


    fun getShoppingList() {
        _screenState.postValue(ScreenState.Loading)

        viewModelScope.launch(Dispatchers.IO) {

            val resultFromData = getShoppingListUseCase.execute(listId)

            if (resultFromData.first) {
                _listOfProducts.postValue(resultFromData.second!!)
                _screenState.postValue(if (resultFromData.second.isEmpty()) ScreenState.Intro else ScreenState.Content )
            } else _screenState.postValue(ScreenState.Error)
        }
    }

    fun addItemToShoppingList(itemName: String, quantity: Int) {

        viewModelScope.launch(Dispatchers.IO) {

            val isAdded = addItemToShoppingListUseCase.execute(listId, itemName, quantity)

            if (isAdded) {
                _addedState.postValue(AddedState.Added(itemName))
                getShoppingList()
            } else {
                _addedState.postValue(AddedState.NotAdded)

            }

            delay(RETURN_ADDED_DEFAULT_DELAY)
            _addedState.postValue(AddedState.Default)
        }
    }

    fun deleteItemFromShoppingList() {
        viewModelScope.launch(Dispatchers.IO) {
            val isDeleted = deleteItemFromShoppingListUseCase.execute(listId, itemId)
            getShoppingList()
        }
    }

    fun getListId(id: Int) {
        listId = id
    }

    fun getItemId(id: Int) {
        itemId = id
    }

    fun startPooling() {
        poolingJob?.cancel()

        if (isPooling) {
            poolingJob = viewModelScope.launch(Dispatchers.IO) {
                delay(POOLING_FREQUENCY)
                poolForUpdates()
                startPooling()
            }
        } else return

    }

    fun allowPooling() {
        isPooling = true
    }

    fun stopPooling() {
        poolingJob?.cancel()
        isPooling = false

    }

    private fun poolForUpdates(){

        viewModelScope.launch(Dispatchers.IO) {

            val resultFromData = getShoppingListUseCase.execute(listId)

            if (resultFromData.first) {
                if (!areListsEqual(resultFromData.second)){
                    _listOfProducts.postValue(resultFromData.second!!)
                    _screenState.postValue(if (resultFromData.second.isEmpty()) ScreenState.Intro else ScreenState.Content)
                }

            } else _screenState.postValue(ScreenState.Error)

        }
    }

    private fun areListsEqual(newList:  List<Product>): Boolean{

        if (newList.size != listOfProducts.value?.size) return false
        else{
            val newListIds = newList.map{
                it.id
            }
            val oldListIds = listOfProducts.value?.map {
                it.id
            }
            newListIds.forEach {
                if (oldListIds?.contains(it) == false) return false
            }
        }
        return true
    }

    companion object {
        const val RETURN_ADDED_DEFAULT_DELAY = 300L
        const val POOLING_FREQUENCY = 1000L
    }
}