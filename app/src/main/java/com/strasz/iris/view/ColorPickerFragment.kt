package com.strasz.iris.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ColorInt
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.jakewharton.rxbinding3.view.touches
import com.strasz.iris.R
import com.strasz.iris.databinding.FragmentColorPickerBinding
import com.strasz.iris.viewmodel.IColorPickerViewModel
import io.reactivex.Observable
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.launch

/**************************************************************
 ***        Originally written by Oliver Straszynski        ***
 ***        https://github.com/broliver12/                  ***
 ***        Subject to MIT License (c) 2021                 ***
 **************************************************************/

class ColorPickerFragment() : Fragment(), IColorPickerView {

    private lateinit var viewModel: IColorPickerViewModel
    private lateinit var navCallback: () -> Unit

     constructor(vm: IColorPickerViewModel, navCb: () -> Unit) : this(){
         viewModel = vm
         navCallback = navCb
     }

    private lateinit var binding: FragmentColorPickerBinding

    private var photoFilePath: Uri? = null
    private var activeSavePress: Boolean = false

    @ColorInt
    private var curColor: Int? = null
    private var darkGrey = 0

    private val galleryResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let {
                    viewModel.updateCurrentImage(it)
                    curColor = null
                    binding.saveButton.isEnabled = false
                    binding.saveButton.setTextColor(darkGrey)
                }
            }
        }

    private val cameraResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                photoFilePath?.let {
                    viewModel.updateCurrentImage(it)
                    curColor = null
                    binding.saveButton.isEnabled = false
                    binding.saveButton.setTextColor(darkGrey)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        darkGrey = ResourcesCompat.getColor(resources, R.color.darkGrey, null)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentColorPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility", "CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity?)!!.setSupportActionBar(binding.mainToolbar)
        viewModel.bindView(this)

        binding.mainImageContainer.maxZoom = 60f

        binding.saveButton.apply {
            isEnabled = false
            setTextColor(ResourcesCompat.getColor(resources, R.color.darkGrey, null))
            setOnTouchListener { saveBtn, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        saveBtn.setIsPressed(true)
                        viewModel.saveOnClick().doOnNext {
                            curColor?.apply {
                                if (activeSavePress) {
                                    viewModel.confirmSave(
                                        getSafeColor()
                                    )
                                    lifecycleScope.launch {
                                        showSuccessfulSaveToast()
                                        saveBtn.setIsPressed(false)
                                    }
                                }
                            }
                        }.subscribe()
                    }
                    MotionEvent.ACTION_UP -> {
                        saveBtn.setIsPressed(false)
                    }
                }
                true
            }
        }

        viewModel.selectedImage.subscribe { x ->
            binding.mainImageContainer.setZoom(1f)
            binding.mainImageContainer.setImageURI(x)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.reloadLastImage()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.color_selector_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_go_to_gallery -> {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                galleryResultLauncher.launch(intent)
            }
            R.id.menu_go_to_camera -> {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val photoFile: File? = try {
                    createImageFileInAppDir()
                } finally {
                    // nothing to do
                }
                if (photoFile != null) {
                    photoFilePath = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.android.provider",
                        photoFile
                    )
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoFilePath)
                }
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                cameraResultLauncher.launch(intent)
            }
            R.id.menu_go_to_favorites -> {
                activity as MainActivity
                navCallback.invoke()
            }
        }
        return true
    }

    private fun createImageFileInAppDir(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CANADA).format(Date())
        val imagePath = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(imagePath, "JPEG_\${$timeStamp}_.jpg")
    }

    override fun onStart() {
        super.onStart()
        bind()
    }

    @SuppressLint("CheckResult")
    private fun bind() {
        viewModel.apply {
            binding.apply {
                xValueText().subscribe { x: String ->
                    xValueText.text = x
                }
                yValueText().subscribe { x: String ->
                    yValueText.text = x
                }
                rgbText().subscribe { x: String ->
                    rgbResult.text = x
                }
                pixelColorInt().subscribe { x: Int ->
                    selectedColorView.setBackgroundColor(x)
                    saveButton.isEnabled = true
                    saveButton.setTextColor(Color.WHITE)
                    curColor = x
                }
                hexText().subscribe { x: String ->
                    hexResult.text = x
                }
                cmykText().subscribe { x: String ->
                    cmykResult.text = x
                }
            }
        }
    }

    override val imageTouched: Observable<MotionEvent>
        get() = binding.mainImageContainer.touches()

    override val currentImageBitmap: Bitmap
        get() {
            binding.mainImageContainer.apply {
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                draw(Canvas(bitmap))
                return bitmap
            }
        }

    private fun View.setIsPressed(pressed: Boolean) {
        isPressed = pressed
        activeSavePress = pressed
    }

    private fun Int.getSafeColor() =
        if (Color.alpha(this) == FULL_ALPHA) {
            this
        } else {
            ResourcesCompat.getColor(
                resources,
                R.color.black,
                null
            )
        }

    private fun showSuccessfulSaveToast() {
        Toast.makeText(
            requireContext(),
            "Saved Successfully!",
            Toast.LENGTH_LONG
        ).show()
    }

    companion object {
        private const val FULL_ALPHA = 255
    }
}