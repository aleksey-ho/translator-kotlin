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
import com.example.translator_kotlin.presentation.MainListenerModule
import com.example.translator_kotlin.presentation.base.BaseFragment
import com.example.translator_kotlin.presentation.bookmark.BookmarkViewModel
import kotlinx.coroutines.launch

abstract class BaseHistoryFragment : BaseFragment<FragmentHistoryPageBinding>(), BookmarkListener {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHistoryPageBinding =
        FragmentHistoryPageBinding::inflate

    val viewModel: BookmarkViewModel by activityViewModels()

    lateinit var bookmarkRecyclerAdapter: BookmarkRecyclerAdapter

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
        initAdapter()
    }

    private fun initAdapter() {
        bookmarkRecyclerAdapter = BookmarkRecyclerAdapter(requireContext(), this)
        binding.listViewHistory.adapter = bookmarkRecyclerAdapter
        val linearLayoutManager = LinearLayoutManager(context)
        binding.listViewHistory.layoutManager = linearLayoutManager
    }

    override fun saveAsFavorite(translate: Translate) {
        lifecycleScope.launch {
            try {
                viewModel.saveAsFavorite(translate)
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    override fun removeFromFavorites(translate: Translate) {
        lifecycleScope.launch {
            try {
                viewModel.removeFromFavorites(translate)
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }
        }
    }

    override fun openTranslate(translate: Translate) {
        MainListenerModule.getInstance().openTranslate(translate)
    }

}
