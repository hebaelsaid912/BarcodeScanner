package com.example.android.barcodescanner.dao

import androidx.room.*
import com.example.android.barcodescanner.pojo.Items

@Dao
interface ItemsDao {
    @Query("SELECT * FROM items ORDER By id DESC")
    suspend fun getAllItems():List<Items>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewItem(item: Items)
    @Query("DELETE FROM items WHERE id =:id")
    suspend fun deleteSpecificItem(id:Int)
    @Update
    suspend fun updateItem(item: Items)
    @Delete
    suspend fun deleteItems(item: Items)
}