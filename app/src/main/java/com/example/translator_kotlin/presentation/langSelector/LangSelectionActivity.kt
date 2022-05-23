package com.example.translator_kotlin.presentation.langSelector

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.translator_kotlin.R
import com.example.translator_kotlin.data.LangDirection
import com.example.translator_kotlin.databinding.ActivityLangSelectionBinding
import com.example.translator_kotlin.domain.model.Language
import com.example.translator_kotlin.presentation.base.BaseActivity
import com.example.translator_kotlin.presentation.translate.TranslateFragment.Companion.EXTRA_DIRECTION
import com.example.translator_kotlin.presentation.translate.TranslateFragment.Companion.EXTRA_ITEM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LangSelectionActivity : BaseActivity<ActivityLangSelectionBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityLangSelectionBinding =
        ActivityLangSelectionBinding::inflate

    private val viewModel: LangSelectionViewModel by viewModels()

    private lateinit var direction: LangDirection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        direction = LangDirection.values()[intent.getIntExtra(DIRECTION, 0)]
        viewModel.setDirection(direction)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (direction == LangDirection.SOURCE)
            getString(R.string.language_source)
        else
            getString(R.string.language_target)

        // создаем адаптер
        val adapter = LangSelectionAdapter(this)

        lifecycleScope.launch {
            try {
                //добавляем в список недавно использованные языки (если есть)
                val recentlyUsedLangs = viewModel.getRecentlyUsedSourceLangs()
                if (recentlyUsedLangs.isNotEmpty()) {
                    adapter.addSectionHeaderItem(getString(R.string.recently_used))
                    adapter.addItems(recentlyUsedLangs)
                }
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }

            try {
                //добавляем все языки
                val languages = viewModel.getLanguages()
                if (languages.isNotEmpty()) {
                    adapter.addSectionHeaderItem(getString(R.string.all_languages))
                    adapter.addItems(languages)
                }
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
            }

            // присваиваем адаптер списку
            binding.listViewLanguages.adapter = adapter
            binding.listViewLanguages.setOnItemClickListener { parent, _, position, _ ->
                val item = parent.adapter.getItem(position)
                if (item is Language) {
                    try {
                        viewModel.updateLanguageUsage(item, direction)
                        val data = Intent()
                        data.putExtra(EXTRA_ITEM, item)
                        data.putExtra(EXTRA_DIRECTION, direction)
                        setResult(RESULT_OK, data)
                        finish()
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                this.finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {

        const val DIRECTION = "DIRECTION"

        fun newIntent(context: Context, direction: LangDirection): Intent {
            val intent = Intent(context, LangSelectionActivity::class.java)
            intent.putExtra(DIRECTION, direction.value)
            return intent
        }
    }

}
