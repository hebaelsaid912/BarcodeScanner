package com.example.android.barcodescanner.ui.expired

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.android.barcodescanner.R
import com.example.android.barcodescanner.databinding.ActivityExpiredItemsBinding
import com.example.android.barcodescanner.pojo.Items
import java.util.ArrayList

class ExpiredItemsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpiredItemsBinding

    private lateinit var expiredItemsList: ArrayList<Items>
    private lateinit var expiredItemsAdapter: ExpiredItemsAdapter
    private val viewModel: ExpiredItemsViewModel by lazy {
        ViewModelProvider(this)[ExpiredItemsViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_expired_items)
        processRoomDataBaseRender(this)
    }

    private fun processRoomDataBaseRender(context: Context) {
        lifecycleScope.launchWhenStarted {
            viewModel.getItemRoomData(context)
            viewModel.roomDataState.collect {
                when (it) {
                    is ExpiredItemsViewModel.RoomItemsViewState.Success -> {
                        binding.expiredShimmerEffectRV.stopShimmer()
                        binding.expiredShimmerEffectRV.visibility = View.GONE
                        binding.expiredEmptyView.visibility = View.GONE
                        expiredItemsList = it.items as ArrayList<Items>
                        expiredItemsAdapter = ExpiredItemsAdapter(expiredItemsList)
                        setItemInRV(expiredItemsAdapter)
                        if(expiredItemsList.isEmpty()){
                            binding.expiredEmptyView.visibility = View.VISIBLE
                        }
                        Log.d("ExpiredItemsActivity", "get data from db to view")
                        Log.d("ExpiredItemsActivity", expiredItemsList.size.toString())
                    }
                    is ExpiredItemsViewModel.RoomItemsViewState.Loading -> {
                        Log.d("ExpiredItemsActivity", "Getting Data From Database")
                    }
                    is ExpiredItemsViewModel.RoomItemsViewState.Error -> {
                        Toast.makeText(this@ExpiredItemsActivity, "Error Getting Data", Toast.LENGTH_SHORT)
                            .show()
                        Log.d("ExpiredItemsActivity", it.message)
                    }
                    else -> Unit

                }
            }
        }
    }
    private fun setItemInRV(expiredItemsAdapter: ExpiredItemsAdapter) {
        binding.expiredItemsScanedRV.hasFixedSize()
        binding.expiredItemsScanedRV.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.expiredItemsScanedRV.adapter = expiredItemsAdapter
        Log.d("ExpiredItemsActivity", "Get data from room to expire rv")

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}