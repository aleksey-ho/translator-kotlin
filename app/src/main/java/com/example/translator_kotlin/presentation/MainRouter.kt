package com.example.translator_kotlin.presentation

import com.example.translator_kotlin.domain.model.Translate
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class MainRouter @Inject constructor() {

    var listener: Listener? = null

    fun openTranslate(translate: Translate) {
        listener?.openTranslate(translate)
    }

    interface Listener {
        fun openTranslate(translate: Translate)
    }

}
