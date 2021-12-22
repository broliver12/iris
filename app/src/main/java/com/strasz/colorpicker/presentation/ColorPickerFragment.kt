package com.strasz.colorpicker.presentation

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import com.jakewharton.rxbinding2.view.RxView
import com.strasz.colorpicker.R
import com.strasz.colorpicker.activity.MainActivity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.color_selector_fragment.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ColorPickerFragment : BaseFragment(), IColorSelectorView {

    var accessGalleryButton: MenuItem? = null
    private val viewModel = ColorPickerViewModel()
    private var photoFilePath: Uri? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity?)!!.setSupportActionBar(main_toolbar)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.color_selector_fragment, container, false)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.color_selector_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        accessGalleryButton = menu.getItem(0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_go_to_gallery) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY)
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
            startActivityForResult(intent, CAMERA)
            return true
        }
        return false
    }

    private fun createImageFileInAppDir(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imagePath = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File(imagePath, "JPEG_\${timeStamp}_" + ".jpg")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                val uri = data!!.data
                main_image_container.setImageURI(uri)
            } else if (requestCode == CAMERA) {
                val i = 7
                if (photoFilePath != null) {
                    main_image_container.setImageURI(photoFilePath)
                } else {
                    // error
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.bindView(this)
        bind()
    }

    private fun bind() {
        viewModel.xValueText()
                .subscribe { x: String -> x_value_text.text = x }
        viewModel.yValueText()
                .subscribe { x: String -> y_value_text.text = x }
        viewModel.rgbText()
                .subscribe { x: String -> rgb_result.text = x }
        viewModel.pixelColorInt()
                .subscribe { x: Int -> selected_color_view.setBackgroundColor(x) }
        viewModel.hexText()
                .subscribe { x: String -> hex_result.text = x }
        viewModel.cmykText()
                .subscribe { x: String -> cmyk_result.text = x }
    }

    override val imageTouched: Observable<MotionEvent>
        get() = RxView.touches(main_image_container)
    override val currentImageBitmap: Bitmap
        get() {
            main_image_container.isDrawingCacheEnabled = true
            val currentScreenshot = Bitmap.createBitmap(main_image_container.drawingCache)
            main_image_container.isDrawingCacheEnabled = false
            return currentScreenshot
        }

    companion object {
        private const val GALLERY = 1000
        private const val CAMERA = 1001
    }
}