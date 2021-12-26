package com.strasz.colorpicker.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
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
import androidx.annotation.ColorInt
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding2.view.RxView
import com.strasz.colorpicker.R
import com.strasz.colorpicker.databinding.FragmentColorPickerBinding
import com.strasz.colorpicker.viewmodel.IColorPickerViewModel
import io.reactivex.Observable
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ColorPickerFragment(
        private val viewModel: IColorPickerViewModel,
        private val navCallback: () -> Unit
) : Fragment(), IColorPickerView {

    private var photoFilePath: Uri? = null
    private lateinit var binding: FragmentColorPickerBinding

    @ColorInt
    private var curColor: Int? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity?)!!.setSupportActionBar(binding.mainToolbar)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentColorPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.bindView(this)
        binding.saveButton.setOnClickListener {
            curColor?.let {
                setSaveButtonVisibility(false)
            }
        }
        binding.cancelButton.setOnClickListener {
            setSaveButtonVisibility(true)
        }

        binding.confirmButton.setOnClickListener {
            setSaveButtonVisibility(true)
            curColor?.let {
                Toast.makeText(requireContext(), "Saved Successfully!", Toast.LENGTH_LONG).show();
                viewModel.confirmSave(
                        if (Color.alpha(it) == FULL_ALPHA) it else Color.BLACK
                )
            }
        }
        viewModel.selectedImage.subscribe { x ->
            binding.mainImageContainer.setImageURI(x)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.reloadLastImage()
    }

    private fun setSaveButtonVisibility(setVisible: Boolean) {
        binding.confirmSaveLinearLayout.visibility = if (setVisible) View.GONE else View.VISIBLE
        binding.saveButton.visibility = if (setVisible) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.color_selector_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_go_to_gallery) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQ_CODE)
            return true
        } else if (item.itemId == R.id.menu_go_to_camera) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            var photoFile: File? = null
            photoFile = try {
                createImageFileInAppDir()
            } finally {
                // nothing to do
            }
            if (photoFile != null) {
                photoFilePath = FileProvider.getUriForFile(requireContext(), "com.example.android.provider", photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoFilePath)
            }
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, CAMERA_REQ_CODE)
            return true
        } else if (item.itemId == R.id.menu_go_to_favorites) {
            activity as MainActivity
            // present color list fragment
            navCallback.invoke()
        }
        return true
    }


    private fun createImageFileInAppDir(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imagePath = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(imagePath, "JPEG_\${$timeStamp}_.jpg")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQ_CODE) {
                data?.data?.let {
                    viewModel.updateCurrentImage(it)
                    curColor = null
                }
            } else if (requestCode == CAMERA_REQ_CODE) {
                photoFilePath?.let {
                    viewModel.updateCurrentImage(it)
                    curColor = null
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        bind()
    }

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
                    curColor = x
                    setSaveButtonVisibility(true)
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
        get() = RxView.touches(binding.mainImageContainer)

    override val currentImageBitmap: Bitmap
        get() {
            binding.mainImageContainer.apply {
                isDrawingCacheEnabled = true
                val currentScreenshot = Bitmap.createBitmap(drawingCache)
                isDrawingCacheEnabled = false
                return currentScreenshot
            }
        }

    companion object {
        private const val GALLERY_REQ_CODE = 1000
        private const val CAMERA_REQ_CODE = 1001
        private const val FULL_ALPHA = 255
    }
}