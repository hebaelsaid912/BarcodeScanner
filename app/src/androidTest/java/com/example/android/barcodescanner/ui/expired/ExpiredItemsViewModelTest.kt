package com.example.android.barcodescanner.ui.expired

import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test

class ExpiredItemsViewModelTest{
    private lateinit var viewModel: ExpiredItemsViewModel

    @Before
    fun setUp() {
        viewModel = ExpiredItemsViewModel()
    }
    @Test
    fun getItemLoadingDataFromDB_returnLoading(){
        viewModel.getItemRoomData(ApplicationProvider.getApplicationContext())
        val result = viewModel.roomDataState.value
        Truth.assertThat(result is ExpiredItemsViewModel.RoomItemsViewState.Loading)
    }
    @Test
    fun getItemSuccessDataFromDB_returnLoading(){
        viewModel.getItemRoomData(ApplicationProvider.getApplicationContext())
        val result = viewModel.roomDataState.value
        Truth.assertThat(result is ExpiredItemsViewModel.RoomItemsViewState.Success)
    }
    @Test
    fun getItemErrorDataFromDB_returnLoading(){
        viewModel.getItemRoomData(ApplicationProvider.getApplicationContext())
        val result = viewModel.roomDataState.value
        Truth.assertThat(result is ExpiredItemsViewModel.RoomItemsViewState.Error)
    }
}