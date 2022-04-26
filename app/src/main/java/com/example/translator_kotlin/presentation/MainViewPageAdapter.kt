package com.example.translator_kotlin.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.translator_kotlin.presentation.bookmark.BookmarkFragment
import com.example.translator_kotlin.presentation.settings.SettingsFragment
import com.example.translator_kotlin.presentation.translate.TranslateFragment

class MainViewPageAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    internal var translateFragment = TranslateFragment.newInstance()
    internal var bookmarkFragment = BookmarkFragment.newInstance()
    internal var settingsFragment = SettingsFragment.newInstance()

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> translateFragment
            1 -> bookmarkFragment
            else -> settingsFragment
        }
    }

}