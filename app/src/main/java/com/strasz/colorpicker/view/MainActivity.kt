package com.strasz.colorpicker.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.strasz.colorpicker.R
import com.strasz.colorpicker.database.App
import com.strasz.colorpicker.database.ColorDb
import com.strasz.colorpicker.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {


    // Initialize shared ViewModel
    private val viewModel = MainViewModel(
            App.colorDb
    )

    // Initialize color picker UI
    private val colorPickerFragment: ColorPickerFragment = ColorPickerFragment(viewModel){
        showColorListFragment()
    }

    // Initialize saved color list UI
    private val colorListFragment: ColorListFragment = ColorListFragment(viewModel){
        onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set android systems status bar color
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.spaceGrey)

        // Show main page
        showColorSelectorFragment()
    }

    override fun onStart() {
        super.onStart()
        // Make sure we have read/write access for external storage
        if (!checkPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }
        if (!checkPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 2)
        }
    }

    // Navigate to main page
    private fun showColorSelectorFragment() {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, colorPickerFragment)
                .commit()
    }

    // Navigate to saved color list page
    private fun showColorListFragment() {
        supportFragmentManager
                .beginTransaction()
                .addToBackStack("colorList")
                .replace(R.id.fragmentContainer, colorListFragment)
                .commit()
    }

    // Helper function for checking system permissions
    private fun checkPermission(context: Context, permission: String) =
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}