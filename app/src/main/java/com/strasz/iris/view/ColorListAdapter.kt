package com.strasz.iris.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.strasz.iris.database.ColorModel
import com.strasz.iris.databinding.ViewFavColorTileBinding

class ColorListAdapter(
        private val removeFromDbCallback: (ColorModel) -> Unit
) : ListAdapter<ColorModel, ColorTileViewHolder>(ColorListItemDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorTileViewHolder {
        return ColorTileViewHolder(
                ViewFavColorTileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ) {
            val list: MutableList<ColorModel> = arrayListOf<ColorModel>().apply {
                addAll(currentList)
            }
            removeFromDbCallback.invoke(
                    list[it]
            )
            submitList(
                    list.apply {
                        removeAt(it)
                    }
            )
        }
    }

    override fun onBindViewHolder(holder: ColorTileViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}

class ColorListItemDiffUtil : DiffUtil.ItemCallback<ColorModel>() {
    override fun areItemsTheSame(p0: ColorModel, p1: ColorModel): Boolean {
        return p0 == p1
    }

    override fun areContentsTheSame(p0: ColorModel, p1: ColorModel): Boolean {
        return p0.id == p1.id
    }
}