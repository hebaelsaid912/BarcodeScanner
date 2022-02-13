package com.example.android.barcodescanner.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.barcodescanner.pojo.Items

@Dao
interface ItemsDao {

    @Query("SELECT * FROM items ORDER By id DESC")
    suspend fun getAllItems():List<Items>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewItem(item: Items)

}