package com.example.translator_kotlin.presentation

import com.example.translator_kotlin.domain.model.Translate

class MainRouter {

    var listener: Listener? = null

    fun openTranslate(translate: Translate) {
        listener?.openTranslate(translate)
    }

    interface Listener {
        fun openTranslate(translate: Translate)
    }

}
