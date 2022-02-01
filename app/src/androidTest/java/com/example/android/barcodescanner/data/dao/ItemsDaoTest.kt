package com.example.android.barcodescanner.data.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.barcodescanner.data.database.ItemsDataBase
import com.example.android.barcodescanner.pojo.Items
import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * AndroidJUnit4 is a library used to test kotlin or java code or any code run in jvm
 * here, we are in android environment not at jvm environment
 * so we need to annotation that we need to use AndroidJUnit4 to test
 * and we use android test because we need to use real mobile or emulator to access room db
 * when scan qrCode
 */
@RunWith(AndroidJUnit4::class)
class ItemsDaoTest {

    private lateinit var database:ItemsDataBase
    private lateinit var dao:ItemsDao

    @Before
    fun setUp(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ItemsDataBase::class.java
        ).allowMainThreadQueries().build()
        dao = database.itemsDeo()
    }

    @After
    fun shutDown(){
        database.close()
    }

    @Test
    fun insertIntoDB() = runBlocking {
        val itemScanned = Items("Beef","Food","2021-10-9")
        dao.insertNewItem(itemScanned)
        val allItems =  dao.getAllItems()
        Truth.assertThat(allItems).contains(itemScanned)
    }
}