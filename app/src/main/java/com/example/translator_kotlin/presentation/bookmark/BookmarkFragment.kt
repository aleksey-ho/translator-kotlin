package com.example.translator_kotlin.presentation.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.translator_kotlin.R
import com.example.translator_kotlin.databinding.FragmentBookmarkBinding
import com.example.translator_kotlin.presentation.base.BaseFragment
import com.example.translator_kotlin.presentation.dialog.Dialog
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment : BaseFragment<FragmentBookmarkBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBookmarkBinding =
        FragmentBookmarkBinding::inflate

    private val viewModel: BookmarkViewModel by activityViewModels()

    private lateinit var adapter: BookmarkViewPageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BookmarkViewPageAdapter(requireActivity())
        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 2

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setText(
                if (position == 0)
                    R.string.view_pager_history
                else
                    R.string.view_pager_favorites
            )
        }.attach()

        binding.buttonClearHistory.setOnClickListener { clearBookmarks() }
    }

    private fun clearBookmarks() {
        val clearHistory = binding.viewPager.currentItem == 0
        Dialog().showClearHistoryDialog(
            requireActivity(),
            clearHistory,
            onButtonClick = {
                if (clearHistory) {
                    viewModel.clearHistory()
                } else {
                    viewModel.clearFavorites()
                }
            }
        )
    }

    companion object {
        fun newInstance(): BookmarkFragment {
            return BookmarkFragment()
        }
    }

}
