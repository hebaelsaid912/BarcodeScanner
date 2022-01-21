package com.example.android.barcodescanner.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android.barcodescanner.dao.ItemsDao
import com.example.android.barcodescanner.pojo.Items

@Database(entities = [Items::class], version = 1, exportSchema = false)
abstract class ItemsDataBase : RoomDatabase(){
    companion object {
        var itemsDataBase: ItemsDataBase? = null

        @Synchronized
        fun getDataBase(context: Context): ItemsDataBase {
            if (itemsDataBase == null) {
                itemsDataBase = Room.databaseBuilder(
                    context,
                    ItemsDataBase::class.java,
                    "items.db"
                ).build()
            }
            return itemsDataBase!!
        }
    }
    abstract fun itemsDeo(): ItemsDao
}