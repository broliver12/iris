package com.strasz.iris.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.strasz.iris.databinding.FragmentColorListBinding
import com.strasz.iris.viewmodel.IColorListViewModel
import kotlin.math.round
import kotlinx.coroutines.launch

/**************************************************************
 ***        Originally written by Oliver Straszynski        ***
 ***        https://github.com/broliver12/                  ***
 ***        Subject to MIT License (c) 2021                 ***
 **************************************************************/

class ColorListFragment(
    private val viewModel: IColorListViewModel,
    private val navCallback: () -> Unit
) : Fragment() {

    private lateinit var binding: FragmentColorListBinding

    private val listAdapter = ColorListAdapter {
        viewModel.removeColor(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentColorListBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.colorListRecyclerView.apply {

            val width = resources.displayMetrics.widthPixels
            val wDpi = resources.displayMetrics.xdpi
            val wInches = round(width / wDpi).toInt()

            layoutManager = GridLayoutManager(requireContext(), wInches)
            adapter = listAdapter
        }
        binding.backButton.setOnClickListener {
            navCallback.invoke()
        }
        viewModel.getSavedColorList().subscribe { x ->
            lifecycleScope.launch {
                listAdapter.submitList(x)
            }
        }
    }
}