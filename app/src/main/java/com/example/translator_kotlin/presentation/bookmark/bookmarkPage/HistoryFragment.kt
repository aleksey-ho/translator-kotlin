package com.example.translator_kotlin.presentation.bookmark.bookmarkPage

import android.os.Bundle
import android.view.View
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : BaseHistoryFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.viewModelScope.launch(Dispatchers.Main) {
            viewModel.history.collect { items ->
                bookmarkRecyclerAdapter.items = items
                binding.emptyView.visibility = if (items.isNotEmpty()) View.GONE else View.VISIBLE
            }
        }
    }

    companion object {
        fun newInstance(): HistoryFragment {
            return HistoryFragment()
        }
    }

}
