package com.example.translator_kotlin.presentation.bookmark.bookmarkPage

import com.example.translator_kotlin.domain.model.Translate

interface BookmarkListener {

    fun saveAsFavorite(translate: Translate)

    fun removeFromFavorites(translate: Translate)

    fun openTranslate(translate: Translate)

}
