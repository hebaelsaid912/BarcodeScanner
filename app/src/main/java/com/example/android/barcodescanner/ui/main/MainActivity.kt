package com.example.android.barcodescanner.ui.main

import android.content.Context
import android.content.Intent
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
import com.example.android.barcodescanner.databinding.ActivityMainBinding
import com.example.android.barcodescanner.pojo.Items
import com.example.android.barcodescanner.ui.expired.ExpiredItemsActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import org.json.JSONException
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.ArrayList

class MainActivity : AppCompatActivity(), EasyPermissions.RationaleCallbacks,
    EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainItemsList: ArrayList<Items>
    private lateinit var mainItemsAdapter: MainItemsAdapter

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        readStorageTask()

        binding.scanNowBTN.setOnClickListener {
            accessCamera()
            openCamToScan()
        }
        binding.expiredCV.setOnClickListener {
            val intent = Intent(this, ExpiredItemsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun processRender(data: String) {
        lifecycleScope.launchWhenStarted {
            Log.d("MainActivity", data)
            viewModel.setItemData(data, this@MainActivity)
            viewModel.state.collect {
                when (it) {
                    is MainViewModel.ItemsViewState.Success -> {
                        if (it.isInserted) {
                            Log.d("MainActivity", "Insert data from scan to room data base")
                            processRoomDataBaseRender(this@MainActivity)
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Error please scan again",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    is MainViewModel.ItemsViewState.Loading -> {
                        Log.d("MainActivity", "Getting Data From Database")
                    }
                    is MainViewModel.ItemsViewState.Error -> {
                        Toast.makeText(this@MainActivity, "Error convert Data", Toast.LENGTH_SHORT)
                            .show()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun processRoomDataBaseRender(context: Context) {
        lifecycleScope.launchWhenStarted {
            viewModel.getItemRoomData(context)
            viewModel.roomDataState.collect {
                when (it) {
                    is MainViewModel.RoomItemsViewState.Success -> {
                        binding.shimmerEffectRV.stopShimmer()
                        binding.shimmerEffectRV.visibility = View.GONE
                        binding.mainEmptyView.visibility = View.GONE
                        mainItemsList = it.items as ArrayList<Items>
                        mainItemsAdapter = MainItemsAdapter(mainItemsList)
                        setItemInRV(mainItemsAdapter)
                        if(mainItemsList.isEmpty()){
                            binding.mainEmptyView.visibility = View.VISIBLE
                        }
                        Log.d("MainActivity", "get data from db to view")
                        Log.d("MainActivity", mainItemsList.size.toString())
                    }
                    is MainViewModel.RoomItemsViewState.Loading -> {
                        Log.d("MainActivity", "Getting Data From Database")
                    }
                    is MainViewModel.RoomItemsViewState.Error -> {
                        Toast.makeText(this@MainActivity, "Error Getting Data", Toast.LENGTH_SHORT)
                            .show()
                        Log.d("MainActivity", it.message)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setItemInRV(mainItemsAdapter: MainItemsAdapter) {
        binding.itemsScanedRV.hasFixedSize()
        binding.itemsScanedRV.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.itemsScanedRV.adapter = mainItemsAdapter
        Log.d("MainActivity", "Get data from room to main rv")

    }

    //check Permissions
    private fun readStorageTask() {
        if (hasReadStoragePermission()) {
            processRoomDataBaseRender(context = this)
        } else {
            EasyPermissions.requestPermissions(
                this,
                "This app needs access to your storage,",
                123,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun accessCamera() {
        if (hasAccessCameraPermission()) {

            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            EasyPermissions.requestPermissions(
                this,
                "This app needs access to your storage,",
                123,
                android.Manifest.permission.CAMERA
            )
        }
    }

    private fun hasReadStoragePermission(): Boolean {
        return EasyPermissions.hasPermissions(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    private fun hasAccessCameraPermission(): Boolean {
        return EasyPermissions.hasPermissions(
            this,
            android.Manifest.permission.CAMERA
        )
    }

    // start scan barcode
    private fun openCamToScan() {
        val barcodeScanner = IntentIntegrator(this)
        barcodeScanner.setPrompt("scan barcode")
        barcodeScanner.setCameraId(0)
        barcodeScanner.setOrientationLocked(true)
        barcodeScanner.setBeepEnabled(true)
        barcodeScanner.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        barcodeScanner.captureActivity = CaptureActivity::class.java
        barcodeScanner.initiateScan()
        Log.d("MainActivity", "openCamToScan")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("MainActivity", "onActivityResult")
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    Log.d("MainActivity", "onActivityResult 1111 ${result.contents}")
                    processRender(result.contents)
                } catch (exception: JSONException) {
                    Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)


    }

    override fun onRationaleAccepted(requestCode: Int) {
        TODO("Not yet implemented")
    }

    override fun onRationaleDenied(requestCode: Int) {
        TODO("Not yet implemented")
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.d("MainActivity", "onPermissionsGranted + $requestCode + $perms")
        processRoomDataBaseRender(this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
            Log.d("MainActivity", "onPermissionsDenied 2 + $requestCode + $perms")
        }
    }
}