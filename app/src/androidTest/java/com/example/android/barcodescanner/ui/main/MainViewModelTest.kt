package com.example.android.barcodescanner.ui.main

import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test

class MainViewModelTest{
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel()
    }
    @Test
    fun insertScannedItemLoadingData_returnLoading(){
        viewModel.setItemData("Beef_Food_2022-09-11", ApplicationProvider.getApplicationContext())
        val result = viewModel.state.value
        Truth.assertThat(result is MainViewModel.ItemsViewState.Loading)
    }
    @Test
     fun insertScannedItemWithValidData_returnSuccess() {
        viewModel.setItemData("Beef_Food_2022-09-11", ApplicationProvider.getApplicationContext())
        val result = viewModel.state.value
        Truth.assertThat(result is MainViewModel.ItemsViewState.Success)

    }
    @Test
     fun insertScannedItemWithEmptyData_returnError(){
        viewModel.setItemData("", ApplicationProvider.getApplicationContext())
        val result = viewModel.state.value
        Truth.assertThat(result is MainViewModel.ItemsViewState.Error)
    }
    @Test
    fun insertScannedItemWithInvalidData_returnError(){
        viewModel.setItemData("Beef,ff,9-12-2022", ApplicationProvider.getApplicationContext())
        val result = viewModel.state.value
        Truth.assertThat(result is MainViewModel.ItemsViewState.Error)
    }

    @Test
    fun getItemLoadingDataFromDB_returnLoading(){
        viewModel.getItemRoomData(ApplicationProvider.getApplicationContext())
        val result = viewModel.roomDataState.value
        Truth.assertThat(result is MainViewModel.RoomItemsViewState.Loading)
    }
    @Test
    fun getItemSuccessDataFromDB_returnLoading(){
        viewModel.getItemRoomData(ApplicationProvider.getApplicationContext())
        val result = viewModel.roomDataState.value
        Truth.assertThat(result is MainViewModel.RoomItemsViewState.Success)
    }
    @Test
    fun getItemErrorDataFromDB_returnLoading(){
        viewModel.getItemRoomData(ApplicationProvider.getApplicationContext())
        val result = viewModel.roomDataState.value
        Truth.assertThat(result is MainViewModel.RoomItemsViewState.Error)
    }
}