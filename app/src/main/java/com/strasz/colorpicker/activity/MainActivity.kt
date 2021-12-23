package com.strasz.colorpicker.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.strasz.colorpicker.R
import com.strasz.colorpicker.presentation.ColorListFragment
import com.strasz.colorpicker.presentation.ColorPickerFragment


class MainActivity : AppCompatActivity() {
    private val colorPickerFragment: ColorPickerFragment = ColorPickerFragment{
        showColorListFragment()
    }
    private val colorListFragment: ColorListFragment = ColorListFragment{
        showColorSelectorFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_picker)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.spaceGrey)

        showColorSelectorFragment()
    }

    override fun onStart() {
        super.onStart()
        if (!checkPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }
        if (!checkPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 2)
        }
    }

    private fun showColorSelectorFragment() {
        supportFragmentManager
                .beginTransaction()
                .addToBackStack("colorSelector")
                .replace(R.id.fragmentContainer, colorPickerFragment)
                .commit()
    }

    private fun showColorListFragment() {
        supportFragmentManager
                .beginTransaction()
                .addToBackStack("colorList")
                .replace(R.id.fragmentContainer, colorListFragment)
                .commit()
    }

    private fun checkPermission(context: Context, permission: String) =
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}