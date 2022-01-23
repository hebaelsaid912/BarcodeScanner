package com.example.android.barcodescanner.ui.expired

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.android.barcodescanner.R
import com.example.android.barcodescanner.pojo.Items
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.abs

class ExpiredItemsAdapter (private val items: List<Items>)
    : RecyclerView.Adapter<ExpiredItemsAdapter.ExpiredItemsViewHolder>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpiredItemsViewHolder {
        context = parent.context
        return ExpiredItemsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ExpiredItemsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ExpiredItemsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val itemName = view.findViewById<TextView>(R.id.itemNameTV)
        val itemCategory = view.findViewById<TextView>(R.id.itemCategoryTV)
        val itemDate = view.findViewById<TextView>(R.id.itemDateTV)
        val itemState = view.findViewById<CardView>(R.id.itemsStatusView)

        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun bind(item: Items) {

            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val d = sdf.format(Date())
            val currentDate = LocalDate.parse(d, DateTimeFormatter.ISO_DATE)
            val expireDate = LocalDate.parse(item.item_expire_date, DateTimeFormatter.ISO_DATE)
            Log.d("ExpiredItemsActivity", "ExpiredItemsAdapter current $currentDate expire $expireDate")
            val diffDays = expireDate.dayOfMonth - currentDate.dayOfMonth
            val diffMonth = abs(expireDate.monthValue - currentDate.monthValue)
            val diffYear = abs(expireDate.year - currentDate.year)
            Log.d(
                "ExpiredItemsActivity",
                "ExpiredItemsAdapter day $diffDays month $diffMonth year $diffYear"
            )
             val numOfDays :Int
            if (diffMonth > 0) {
                numOfDays = diffDays + (diffMonth * 30)
            } else {
                numOfDays = diffDays
            }
            val red = ContextCompat.getColor(context, R.color.red)
            if (expireDate.year < currentDate.year ||
                expireDate.monthValue < currentDate.monthValue ||
                numOfDays < 0
            ) {
                itemState.setCardBackgroundColor(red)
                itemDate.text = "Expired"
            } else {
                itemView.layoutParams.height = 0
                itemView.layoutParams.width = 0
            }
            itemName.text = item.item_name
            itemCategory.text = item.item_category
        }

    }
}

