package com.strasz.colorpicker.view

import android.service.quicksettings.Tile
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.strasz.colorpicker.database.ColorModel
import com.strasz.colorpicker.util.ColorUtil
import kotlinx.android.synthetic.main.view_fav_color_tile.view.*

class ColorTileViewHolder(private val view: View, private val cb: (Int) -> Unit) : RecyclerView.ViewHolder(view) {

    private enum class TileState {
        COLOR, INFO, REMOVE
    }

    private var lastNonRemoveState: TileState = TileState.COLOR
    private var curState: TileState = TileState.COLOR

    fun bind(model: ColorModel) {
        view.rootView.setOnClickListener {
            if (curState == TileState.INFO) {
                changeState(TileState.COLOR)
            } else if (curState == TileState.COLOR) {
                changeState(TileState.INFO)
            }
        }
        view.rootView.setOnLongClickListener {
            changeState(TileState.REMOVE)
            true
        }
        view.tileCancelDeleteTextView.setOnClickListener {
            changeState(lastNonRemoveState)
        }
        view.tileDeleteTextView.setOnClickListener {
            cb.invoke(absoluteAdapterPosition)
        }
        view.tileColorView.setBackgroundColor(model.hexVal)
        view.tileHexTextView.text = ColorUtil.generateHexString(model.hexVal)
        view.tileRGBTextView.text = "RGB: \n" + ColorUtil.generateRGBString(model.hexVal)
        view.tileCMYKTextView.text = "CMYK: \n" + ColorUtil.generateCMYKString(model.hexVal)
        updateUiState(
                infoVis = false,
                removeVis = false,
                colorVis = true
        )
    }

    private fun changeState(state: TileState) {
        when (state) {
            TileState.COLOR -> {
                lastNonRemoveState = TileState.COLOR
                updateUiState(
                        infoVis = false,
                        removeVis = false,
                        colorVis = true
                )
            }
            TileState.INFO -> {
                lastNonRemoveState = TileState.INFO
                updateUiState(
                        infoVis = true,
                        removeVis = false,
                        colorVis = false
                )
            }
            TileState.REMOVE -> {
                updateUiState(
                        infoVis = false,
                        removeVis = true,
                        colorVis = false
                )
            }
        }
        curState = state
    }

    private fun updateUiState(infoVis: Boolean, removeVis: Boolean, colorVis: Boolean) {
        view.tileInfoContainer.visibility = if (infoVis) View.VISIBLE else View.GONE
        view.tileRemoveContainer.visibility = if (removeVis) View.VISIBLE else View.GONE
        view.tileColorView.visibility = if (colorVis) View.VISIBLE else View.INVISIBLE
    }
}