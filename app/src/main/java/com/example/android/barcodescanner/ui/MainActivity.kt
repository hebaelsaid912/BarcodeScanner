package com.example.android.barcodescanner.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android.barcodescanner.R
import com.example.android.barcodescanner.database.ItemsDataBase
import com.example.android.barcodescanner.databinding.ActivityMainBinding
import com.example.android.barcodescanner.pojo.Items
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.ArrayList

class MainActivity : AppCompatActivity(), EasyPermissions.RationaleCallbacks,
    EasyPermissions.PermissionCallbacks{

    private lateinit var binding:ActivityMainBinding
    lateinit var hide: Animation
    lateinit var reveal: Animation
    private lateinit var mainItemsList: ArrayList<Items>
    private lateinit var mainItemsAdapter: MainItemsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_main)
        hide = AnimationUtils.loadAnimation(this , android.R.anim.fade_out)
        reveal = AnimationUtils.loadAnimation(this , android.R.anim.fade_in)
        binding.scanNowBTN.setOnClickListener {
            openCamToScan()
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

    private fun readStorageTask() {
        if (hasReadStoragePermission()) {

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


        } else {
            EasyPermissions.requestPermissions(
                this,
                "This app needs access to your storage,",
                123,
                android.Manifest.permission.CAMERA
            )
        }
    }

    fun openCamToScan(){
        val barcodeScanner = IntentIntegrator(this)
        barcodeScanner.setPrompt("scan barcode")
        barcodeScanner.setCameraId(0)
        barcodeScanner.setOrientationLocked(true)
        barcodeScanner.setBeepEnabled(true)
        barcodeScanner.captureActivity = CaptureActivity::class.java
        barcodeScanner.initiateScan()
        Log.d("MainActivity","openCamToScan")
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("MainActivity","onActivityResult")
        Log.d("MainActivity","onActivityResult ${resultCode}")
        Log.d("MainActivity","onActivityResult ${Activity.RESULT_OK}")
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            Log.d("MainActivity","onActivityResult ${result.contents}")
            if (result.contents == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_SHORT).show()
            } else {
                try {
                    Log.d("MainActivity","onActivityResult 1111 ${result.contents}")
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
    private fun setItemInRV(mainItemsAdapter: MainItemsAdapter) {
        lifecycleScope.launch(Dispatchers.Main) {
            binding.itemsScanedRV.layoutManager = LinearLayoutManager(this@MainActivity
                , LinearLayoutManager.HORIZONTAL,false)
            binding.itemsScanedRV.adapter = mainItemsAdapter
            Log.d("MainActivity","Get data from room to main rv")
        }

    }
    private fun getDataFromDb()= lifecycleScope.launch (Dispatchers.Default){
        this.let {
            val categoryItemsList = ItemsDataBase
                .getDataBase(this@MainActivity).itemsDeo().getAllItems()
            mainItemsList = categoryItemsList as ArrayList<Items>
            mainItemsList.reverse()
            mainItemsAdapter = MainItemsAdapter(mainItemsList)
            setItemInRV(mainItemsAdapter)
        }
    }
    private fun insertItemsDataIntoRoomDB(items: Items) {
        lifecycleScope.launch(Dispatchers.Default) {
            this.let {
                ItemsDataBase.getDataBase(this@MainActivity)
                        .itemsDeo().insertNewItem(items)
            }
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
        Log.d("MainActivity","onPermissionsGranted + $requestCode + $perms")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if( EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            AppSettingsDialog.Builder(this).build().show()
            Log.d("MainActivity","onPermissionsDenied 2 + $requestCode + $perms")
        }
    }
}