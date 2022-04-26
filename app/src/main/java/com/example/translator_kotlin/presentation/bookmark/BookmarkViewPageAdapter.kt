package com.example.translator_kotlin.presentation.bookmark

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.translator_kotlin.presentation.bookmark.bookmarkPage.FavoritesFragment
import com.example.translator_kotlin.presentation.bookmark.bookmarkPage.HistoryFragment


class BookmarkViewPageAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private var historyFragment = HistoryFragment.newInstance()
    private var favoritesFragment = FavoritesFragment.newInstance()

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            historyFragment
        } else {
            favoritesFragment
        }
    }
}
