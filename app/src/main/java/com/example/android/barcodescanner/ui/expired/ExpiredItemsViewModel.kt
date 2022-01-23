package com.example.android.barcodescanner.ui.expired

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.barcodescanner.data.database.ItemsDataBase
import com.example.android.barcodescanner.pojo.Items
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExpiredItemsViewModel :ViewModel() {
    private val _roomDataViewState = MutableStateFlow<RoomItemsViewState>(
        RoomItemsViewState.Idle)
    val roomDataState: StateFlow<RoomItemsViewState> get() = _roomDataViewState
    private lateinit var items: List<Items>
    sealed class RoomItemsViewState {
        data class Success(val items: List<Items>) : RoomItemsViewState()
        data class Error(val message: String) : RoomItemsViewState()
        object Loading : RoomItemsViewState()
        object Idle : RoomItemsViewState()
    }

    // get data from db
    fun getItemRoomData(context: Context) = viewModelScope.launch {
        _roomDataViewState.value = RoomItemsViewState.Loading
        getDataFromDb(context)
        delay(2000L)
        _roomDataViewState.value = try {
            Log.d("ExpiredItemsActivity",items.size.toString())
            RoomItemsViewState.Success(items)
        } catch (ex: Exception) {
            RoomItemsViewState.Error(ex.message!!)
        }
    }
    private fun getDataFromDb(context: Context){
        viewModelScope.launch(Dispatchers.Default) {
            setItemList( ItemsDataBase
                .getDataBase(context).itemsDeo().getAllItems())
        }
    }
    private fun setItemList(itemsData: List<Items>){
        items = itemsData
    }
}