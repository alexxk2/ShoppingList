package com.example.shoppinglist.presentation.lists.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.domain.lists.CreateShoppingListUseCase
import com.example.shoppinglist.domain.lists.DeleteShoppingListUseCase
import com.example.shoppinglist.domain.lists.GetAllShoppingListsUseCase
import com.example.shoppinglist.domain.lists.GetShoppingListUseCase
import com.example.shoppinglist.domain.lists.RestoreShoppingListUseCase
import com.example.shoppinglist.domain.models.ShoppingList
import com.example.shoppinglist.presentation.lists.models.CreatedState
import com.example.shoppinglist.presentation.lists.models.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ListsViewModel(
    private val getAllShoppingListsUseCase: GetAllShoppingListsUseCase,
    private val createShoppingListUseCase: CreateShoppingListUseCase,
    private val deleteShoppingListUseCase: DeleteShoppingListUseCase,
    private val restoreShoppingListUseCase: RestoreShoppingListUseCase,
    private val getShoppingListUseCase: GetShoppingListUseCase
) : ViewModel() {

    private val _screenState = MutableLiveData<ScreenState>()
    val screenState: LiveData<ScreenState> = _screenState

    private val _shoppingLists = MutableLiveData<List<ShoppingList>>()
    val shoppingLists: LiveData<List<ShoppingList>> = _shoppingLists

    private val _createdState = MutableLiveData<CreatedState>()
    val createdState: LiveData<CreatedState> = _createdState

    private val _numberOfProducts = MutableLiveData<String>()
    val numberOfProducts: LiveData<String> = _numberOfProducts

    private var listInFocus = 0

    private var poolingJob: Job? = null
    private var isPooling: Boolean = false



    fun getAllLists() {
        _screenState.postValue(ScreenState.Loading)

        viewModelScope.launch(Dispatchers.IO) {

            val resultFromData = getAllShoppingListsUseCase.execute()

            if (resultFromData.first) {
                _shoppingLists.postValue(resultFromData.second!!)
                _screenState.postValue(if (resultFromData.second.isEmpty()) ScreenState.Intro else ScreenState.Content)
            } else _screenState.postValue(ScreenState.Error)
        }
    }

    fun createList(name: String) {

        viewModelScope.launch(Dispatchers.IO) {

            val isCreated = createShoppingListUseCase.execute(name)

            if (isCreated) {
                _createdState.postValue(CreatedState.Created(name))
                getAllLists()
            } else {
                _createdState.postValue(CreatedState.NotCreated)
            }

            delay(RETURN_CREATED_DEFAULT_DELAY)
            _createdState.postValue(CreatedState.Default)


        }
    }

    fun getShoppingListItemsQuantity(id: Int) {
        listInFocus = id
        viewModelScope.launch(Dispatchers.IO) {
            _numberOfProducts.postValue(getRightEndingGoods(getShoppingListUseCase.execute(id).second.size))
        }
    }


    fun deleteShoppingList() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteShoppingListUseCase.execute(listInFocus)
            getAllLists()
        }
    }

    fun restoreShoppingList() {
        viewModelScope.launch(Dispatchers.IO) {
            restoreShoppingListUseCase.execute(listInFocus)
            getAllLists()
        }
    }

    private fun getRightEndingGoods(numberOfTracks: Int): String {
        val preLastDigit = numberOfTracks % 100 / 10

        if (preLastDigit == 1) {
            return "$numberOfTracks товаров"
        }

        return when (numberOfTracks % 10) {
            1 -> "$numberOfTracks товар"
            2 -> "$numberOfTracks товара"
            3 -> "$numberOfTracks товара"
            4 -> "$numberOfTracks товара"
            else -> "$numberOfTracks товаров"
        }
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

            val resultFromData = getAllShoppingListsUseCase.execute()

            if (resultFromData.first) {
                if (!areListsEqual(resultFromData.second)){
                    _shoppingLists.postValue(resultFromData.second!!)
                    _screenState.postValue(if (resultFromData.second.isEmpty()) ScreenState.Intro else ScreenState.Content)
                }

            } else _screenState.postValue(ScreenState.Error)

        }
    }

    private fun areListsEqual(newList:  List<ShoppingList>): Boolean{

        if (newList.size != shoppingLists.value?.size) return false
        else{
            val newListIds = newList.map{
                it.id
            }
            val oldListIds = shoppingLists.value?.map {
                it.id
            }
            newListIds.forEach {
                if (oldListIds?.contains(it) == false) return false
            }
        }
        return true
    }

    companion object {
        const val RETURN_CREATED_DEFAULT_DELAY = 300L
        const val POOLING_FREQUENCY = 1000L
    }

}