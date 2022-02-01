package com.example.android.barcodescanner.pojo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "items")
data class Items(
    @ColumnInfo(name = "item_name")
    var item_name: String,
    @ColumnInfo(name = "item_category")
    var item_category: String,
    @ColumnInfo(name = "item_expire_date")
    var item_expire_date: String
   // @ColumnInfo(name = "item_status")
    //var item_status: String

){
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}
