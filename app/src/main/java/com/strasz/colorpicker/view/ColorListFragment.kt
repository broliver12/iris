package com.strasz.colorpicker.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.strasz.colorpicker.R
import com.strasz.colorpicker.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_saved_colors.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ColorListFragment(
        private val mainViewModel: MainViewModel,
        private val navCallback: () -> Unit
) : Fragment() {

    private val listAdapter = ColorListAdapter {
        GlobalScope.launch {
            mainViewModel.delete(it)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_saved_colors, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        colorListRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = listAdapter
        }

        backButton.setOnClickListener {
            navCallback.invoke()
        }
        mainViewModel.getList().subscribe { x ->
            MainScope().launch {
                listAdapter.submitList(x)
            }
        }
    }
}