package com.example.translator_kotlin.presentation

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import com.example.translator_kotlin.R
import com.example.translator_kotlin.databinding.ActivityMainBinding
import com.example.translator_kotlin.domain.model.Translate
import com.example.translator_kotlin.presentation.base.BaseActivity
import com.example.translator_kotlin.presentation.dialog.Dialog
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), MainListenerModule.MainListener {

    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding = ActivityMainBinding::inflate
    private lateinit var adapter: MainViewPageAdapter
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        adapter = MainViewPageAdapter(this)

        binding.viewPager.adapter = adapter
        binding.viewPager.offscreenPageLimit = 3

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.setIcon(
                when (position) {
                    0 -> R.drawable.ic_translate
                    1 -> R.drawable.ic_bookmark
                    else -> R.drawable.ic_settings
                }
            )
        }.attach()

        binding.viewPager.isUserInputEnabled = false

        mainViewModel.showInitDialog()
    }

    override fun openTranslate(translate: Translate) {
        binding.viewPager.setCurrentItem(0, true)
        adapter.translateFragment.setTranslate(translate)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (view is EditText) {
                val outRect = Rect()
                view.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    view.clearFocus()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                    adapter.translateFragment.saveTranslate()
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

}
