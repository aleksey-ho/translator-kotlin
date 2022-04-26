package com.example.translator_kotlin.presentation.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.translator_kotlin.R
import com.example.translator_kotlin.databinding.FragmentBookmarkBinding
import com.example.translator_kotlin.presentation.base.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkFragment : BaseFragment<FragmentBookmarkBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentBookmarkBinding =
        FragmentBookmarkBinding::inflate

    private val viewModel: BookmarkViewModel by activityViewModels()

    private lateinit var adapter: BookmarkViewPageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        return root
    }

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
        val currentItem = binding.viewPager.currentItem
        viewModel.clear(currentItem == 0)
    }

    companion object {
        fun newInstance(): BookmarkFragment {
            return BookmarkFragment()
        }
    }

}
