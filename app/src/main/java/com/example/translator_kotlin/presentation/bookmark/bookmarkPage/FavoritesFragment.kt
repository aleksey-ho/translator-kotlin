package com.example.translator_kotlin.presentation.bookmark.bookmarkPage

import android.os.Bundle
import android.view.View
import androidx.lifecycle.viewModelScope
import com.example.translator_kotlin.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoritesFragment : BaseHistoryFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.favorites.observe(viewLifecycleOwner) { items ->
            bookmarkRecyclerAdapter.items = items
            binding.emptyView.visibility = if (items.isNotEmpty()) View.GONE else View.VISIBLE
        }

        viewModel.viewModelScope.launch(Dispatchers.Main) {
            viewModel.favorites.observe(viewLifecycleOwner) { items ->
                bookmarkRecyclerAdapter.items = items
                binding.emptyView.visibility = if (items.isNotEmpty()) View.GONE else View.VISIBLE
            }
        }

        binding.imageView.setBackgroundResource(R.drawable.ic_favorite_dark)
        binding.textView.setText(R.string.favorites_empty)
    }

    companion object {
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }

}
