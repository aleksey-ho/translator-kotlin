package com.example.translator_kotlin.presentation.bookmark.bookmarkPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.translator_kotlin.databinding.FragmentHistoryPageBinding
import com.example.translator_kotlin.domain.model.Translate
import com.example.translator_kotlin.presentation.MainRouter
import com.example.translator_kotlin.presentation.base.BaseFragment
import com.example.translator_kotlin.presentation.bookmark.BookmarkViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.android.getKoin
import org.koin.androidx.scope.scopeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.getScopeId

abstract class BaseHistoryFragment(
) : BaseFragment<FragmentHistoryPageBinding>(), BookmarkListener {

    private lateinit var mainRouter: MainRouter

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHistoryPageBinding =
        FragmentHistoryPageBinding::inflate

    val viewModel: BookmarkViewModel by viewModel()

    lateinit var bookmarkRecyclerAdapter: BookmarkRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val scopeId = scopeActivity!!.getScopeId()
        scope.linkTo(getKoin().getScope(scopeId))
        mainRouter = get()
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
