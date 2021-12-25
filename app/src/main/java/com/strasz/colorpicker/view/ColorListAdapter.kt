package com.strasz.colorpicker.view

import ColorTileViewHolder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.strasz.colorpicker.R
import com.strasz.colorpicker.database.ColorModel

class ColorListAdapter : ListAdapter<ColorModel, ColorTileViewHolder>(ColorListItemDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorTileViewHolder {
        return ColorTileViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.view_fav_color_tile, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ColorTileViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}

class ColorListItemDiffUtil() : DiffUtil.ItemCallback<ColorModel>() {
    override fun areItemsTheSame(p0: ColorModel, p1: ColorModel): Boolean {
        return p0 == p1
    }

    override fun areContentsTheSame(p0: ColorModel, p1: ColorModel): Boolean {
        return p0.id == p1.id
    }
}