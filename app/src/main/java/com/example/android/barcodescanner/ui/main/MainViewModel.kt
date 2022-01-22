package com.example.android.barcodescanner.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.barcodescanner.database.ItemsDataBase
import com.example.android.barcodescanner.pojo.Items
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _viewState = MutableStateFlow<ItemsViewState>(ItemsViewState.Idle)
    val state: StateFlow<ItemsViewState> get() = _viewState

    private val _roomDataViewState = MutableStateFlow<RoomItemsViewState>(RoomItemsViewState.Idle)
    val roomDataState: StateFlow<RoomItemsViewState> get() = _roomDataViewState

    private lateinit var items: List<Items>

    sealed class ItemsViewState {
        data class Success(val isInserted: Boolean) : ItemsViewState()
        data class Error(val message: String) : ItemsViewState()
        object Loading : ItemsViewState()
        object Idle : ItemsViewState()
    }

    sealed class RoomItemsViewState {
        data class Success(val items: List<Items>) : RoomItemsViewState()
        data class Error(val message: String) : RoomItemsViewState()
        object Loading : RoomItemsViewState()
        object Idle : RoomItemsViewState()
    }
    //Inserting data from Scan to RoomDB
    fun getItemData(scanData: String , context: Context) = viewModelScope.launch {
        _viewState.value = ItemsViewState.Loading
        _viewState.value = try {
            Log.d("MainActivity", "Insert data from scan to room data base")
            ItemsViewState.Success(insertItemsDataIntoRoomDB(scanData, context))
        } catch (ex: Exception) {
            ItemsViewState.Error(ex.message!!)
        }
    }
    private fun insertItemsDataIntoRoomDB(data: String , context: Context): Boolean{
        viewModelScope.launch(Dispatchers.Default) {
            val items =  async { setItemData(data) }
            Log.d("MainActivity", "Insert data from scan to room data base")
            Log.d("MainActivity", items.await().item_name)
            Log.d("MainActivity", items.await().item_category)
            Log.d("MainActivity", items.await().item_expire_date)
            ItemsDataBase.getDataBase(context)
                .itemsDeo().insertNewItem(items.await())
        }
        return true
    }
    private fun setItemData(scanData: String): Items {
        val listDate = convertData(scanData)
        return convertStringListItemData(listDate)
    }
    private fun convertStringListItemData(listDate: List<String>): Items {
        return Items(
            item_name = listDate[0],
            item_category = listDate[1],
            item_expire_date = listDate[2]
        )
    }
    private fun convertData(scanData: String): List<String> {
        return scanData.split('_')
    }


    // get data from db
    fun getItemRoomData(context: Context) = viewModelScope.launch {
        _roomDataViewState.value = RoomItemsViewState.Loading
        getDataFromDb(context)
        delay(2000L)
        _roomDataViewState.value = try {
           Log.d("MainActivity",items.size.toString())
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