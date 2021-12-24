package com.strasz.colorpicker.view

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.strasz.colorpicker.R
import com.strasz.colorpicker.database.ColorModel
import com.strasz.colorpicker.util.ColorUtil.Companion.generateHexString
import kotlinx.android.synthetic.main.view_fav_color_tile.view.*

class ColorListAdapter : ListAdapter<ColorModel, ColorModelViewHolder>(ColorListItemDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorModelViewHolder {
        return ColorModelViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.view_fav_color_tile, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ColorModelViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}

class ColorModelViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(model: ColorModel){
        view.tileColorView.setBackgroundColor(model.hexVal)
        view.tileTextView.text = generateHexString(model.hexVal)
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