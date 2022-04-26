package com.example.translator_kotlin.utils

import android.view.View
import androidx.databinding.BindingAdapter


@BindingAdapter("onClick")
fun View.onClick(listener: (() -> Unit)?) {
    setOnClickListener {
        listener?.invoke()
    }
}

@BindingAdapter("visibleOrGone")
fun View.visibleOrGone(isVisible: Boolean?) {
    if (isVisible != null) {
        visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}