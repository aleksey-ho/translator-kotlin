package com.example.translator_kotlin.presentation.bookmark.bookmarkPage

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.translator_kotlin.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HistoryFragment : BaseHistoryFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView.setBackgroundResource(R.drawable.ic_access_time_black_24dp)
        binding.textView.setText(R.string.history_empty)
        viewModel.history
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { items ->
                bookmarkRecyclerAdapter.items = items
                binding.emptyView.visibility = if (items.isNotEmpty()) View.GONE else View.VISIBLE
            }
            .launchIn(lifecycleScope)
    }

    companion object {
        fun newInstance(): HistoryFragment {
            return HistoryFragment()
        }
    }

}
