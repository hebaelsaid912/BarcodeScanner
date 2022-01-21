package com.example.android.barcodescanner.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.barcodescanner.R
import com.example.android.barcodescanner.pojo.Items
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.exp

class MainItemsAdapter(private val items: List<Items>)
    : RecyclerView.Adapter<MainItemsAdapter.MainItemsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainItemsViewHolder {
        return MainItemsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainItemsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class MainItemsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val itemName = view.findViewById<TextView>(R.id.itemNameTV)
        val itemCategory = view.findViewById<TextView>(R.id.itemCategoryTV)
        val itemDate = view.findViewById<TextView>(R.id.itemDateTV)

        fun bind(item:Items){
            itemName.text = item.item_name
            itemCategory.text = item.item_category
            val expireDate = item.item_expire_date as Date
            val millionSeconds = expireDate.time - Calendar.getInstance().timeInMillis
            val daysNum = TimeUnit.MILLISECONDS.toDays(millionSeconds).toString()
            itemDate.text = daysNum
        }

    }

}