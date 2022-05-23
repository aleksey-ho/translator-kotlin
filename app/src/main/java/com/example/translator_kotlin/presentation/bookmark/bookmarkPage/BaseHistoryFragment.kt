package com.example.translator_kotlin.presentation.bookmark.bookmarkPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.translator_kotlin.databinding.FragmentHistoryPageBinding
import com.example.translator_kotlin.domain.model.Translate
import com.example.translator_kotlin.presentation.MainRouter
import com.example.translator_kotlin.presentation.base.BaseFragment
import com.example.translator_kotlin.presentation.bookmark.BookmarkViewModel
import kotlinx.coroutines.launch

abstract class BaseHistoryFragment(
) : BaseFragment<FragmentHistoryPageBinding>(), BookmarkListener {

    abstract var mainRouter: MainRouter

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHistoryPageBinding =
        FragmentHistoryPageBinding::inflate

    val viewModel: BookmarkViewModel by activityViewModels()

    lateinit var bookmarkRecyclerAdapter: BookmarkRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    private fun initAdapter() {
        bookmarkRecyclerAdapter = BookmarkRecyclerAdapter(requireContext(), this)
        binding.listViewHistory.adapter = bookmarkRecyclerAdapter
        val linearLayoutManager = LinearLayoutManager(context)
        binding.listViewHistory.layoutManager = linearLayoutManager
    }

    override fun saveAsFavorite(translate: Translate) {
        viewModel.saveAsFavorite(translate)
    }

    override fun removeFromFavorites(translate: Translate) {
        viewModel.removeFromFavorites(translate)
    }

    override fun openTranslate(translate: Translate) {
        lifecycleScope.launch {
            try {
                mainRouter.openTranslate(translate)
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

}
