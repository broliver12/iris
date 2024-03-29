package com.strasz.iris.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.strasz.iris.R
import com.strasz.iris.database.App
import com.strasz.iris.viewmodel.MainViewModel

/**************************************************************
 ***        Originally written by Oliver Straszynski        ***
 ***        https://github.com/broliver12/                  ***
 ***        Subject to MIT License (c) 2021                 ***
 **************************************************************/

class MainActivity : AppCompatActivity() {

    // Initialize shared ViewModel
    private val viewModel = MainViewModel(
        App.colorDao
    )

    // Initialize color picker UI
    private val colorPickerFragment: ColorPickerFragment = ColorPickerFragment(viewModel) {
        showSavedColorsPage()
    }

    // Initialize saved color list UI
    private val colorListFragment: ColorListFragment = ColorListFragment(viewModel) {
        onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set Android status bar color
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.navy)
        }

        // Show main page
        showMainPage()
    }


    override fun onStart() {
        super.onStart()
        // Make sure we have read/write access for external storage
        if (!checkPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_STORAGE_REQ_CODE
            )
        }
        if (!checkPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_STORAGE_REQ_CODE
            )
        }
    }

    // Navigate to main page
    private fun showMainPage() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, colorPickerFragment)
            .commit()
    }

    // Navigate to saved color list page
    private fun showSavedColorsPage() {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack("colorList")
            .replace(R.id.fragmentContainer, colorListFragment)
            .commit()
    }

    // Helper function for checking system permissions
    private fun checkPermission(context: Context, permission: String) =
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

    companion object {
        private const val READ_STORAGE_REQ_CODE = 1
        private const val WRITE_STORAGE_REQ_CODE = 2
    }
}