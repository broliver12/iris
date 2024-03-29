package com.strasz.iris.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.strasz.iris.R
import com.strasz.iris.database.ColorModel
import com.strasz.iris.databinding.SavedColorTileBinding
import com.strasz.iris.util.ColorUtil

/**************************************************************
 ***        Originally written by Oliver Straszynski        ***
 ***        https://github.com/broliver12/                  ***
 ***        Subject to MIT License (c) 2021                 ***
 **************************************************************/

class ColorTileViewHolder(private val binding: SavedColorTileBinding, private val cb: (Int) -> Unit) : RecyclerView.ViewHolder(binding.root) {

    private enum class TileState {
        COLOR, INFO, REMOVE
    }

    private var lastNonRemoveState: TileState = TileState.COLOR
    private var curState: TileState = TileState.COLOR

    fun bind(model: ColorModel) {
        binding.apply {
            root.setOnClickListener {
                if (curState == TileState.INFO) {
                    changeState(TileState.COLOR)
                } else if (curState == TileState.COLOR) {
                    changeState(TileState.INFO)
                }
            }
            root.setOnLongClickListener {
                changeState(TileState.REMOVE)
                true
            }
            tileCancelDeleteTextView.setOnClickListener {
                changeState(lastNonRemoveState)
            }
            tileDeleteTextView.setOnClickListener {
                cb.invoke(absoluteAdapterPosition)
            }
            tileColorView.setBackgroundColor(model.hexVal)
            tileHexTextView.text = ColorUtil.formatHex(model.hexVal)
            tileRGBTextView.text = root.context.getString(R.string.rgb_field_format, ColorUtil.formatRgb(model.hexVal))
            tileCMYKTextView.text = root.context.getString(R.string.cmyk_field_format, ColorUtil.formatCmyk(model.hexVal))
        }

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
        binding.apply {
            tileInfoContainer.visibility = if (infoVis) View.VISIBLE else View.GONE
            tileRemoveContainer.visibility = if (removeVis) View.VISIBLE else View.GONE
            tileColorView.visibility = if (colorVis) View.VISIBLE else View.INVISIBLE
        }
    }
}