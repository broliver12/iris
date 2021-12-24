package com.strasz.colorpicker.view

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding2.view.RxView
import com.strasz.colorpicker.R
import com.strasz.colorpicker.viewmodel.MainViewModel
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_color_picker.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ColorPickerFragment(
        private val viewModel: MainViewModel,
        private val navCallback: () -> Unit
) : Fragment(), IColorPickerView {

    private var photoFilePath: Uri? = null
    @ColorInt
    private var curColor: Int? = null
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity?)!!.setSupportActionBar(main_toolbar)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)
        val view = inflater.inflate(R.layout.fragment_color_picker, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.bindView(this)
        saveButton.setOnClickListener {
            setSaveButtonVisibility(false)
        }
        cancelButton.setOnClickListener {
            setSaveButtonVisibility(true)
        }
        confirmButton.setOnClickListener {
            setSaveButtonVisibility(true)

            Toast.makeText(requireContext(), "Saved Successfully!", Toast.LENGTH_LONG).show();

            curColor?.let{
                viewModel.confirmSave(it)
            }
        }
    }

    private fun setSaveButtonVisibility(setVisible: Boolean) {
        confirmSaveLinearLayout.visibility = if (setVisible) View.GONE else View.VISIBLE
        saveButton.visibility = if (setVisible) View.VISIBLE else View.GONE
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
        } else if (item.itemId == R.id.menu_go_to_favorites) {
            activity as MainActivity
            // present color list fragment
            navCallback.invoke()
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
                curColor = null
            } else if (requestCode == CAMERA) {
                val i = 7
                if (photoFilePath != null) {
                    main_image_container.setImageURI(photoFilePath)
                    curColor = null
                } else {
                    // error
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
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
                .subscribe { x: Int ->
                    selected_color_view.setBackgroundColor(x)
                    curColor = x
                }
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