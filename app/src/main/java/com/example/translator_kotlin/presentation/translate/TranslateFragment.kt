package com.example.translator_kotlin.presentation.translate

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.translator_kotlin.R
import com.example.translator_kotlin.data.LangDirection
import com.example.translator_kotlin.databinding.FragmentTranslateBinding
import com.example.translator_kotlin.databinding.TextInputPanelBinding
import com.example.translator_kotlin.domain.model.Language
import com.example.translator_kotlin.domain.model.Translate
import com.example.translator_kotlin.presentation.base.BaseFragment
import com.example.translator_kotlin.presentation.dialog.Dialog
import com.example.translator_kotlin.presentation.langSelector.LangSelectionActivity
import com.example.translator_kotlin.presentation.langSelector.LangSelectionListenerModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TranslateFragment : BaseFragment<FragmentTranslateBinding>(),
    LangSelectionListenerModel.LangSelectionListener {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentTranslateBinding =
        FragmentTranslateBinding::inflate
    private lateinit var textInputPanelBinding: TextInputPanelBinding

    private val viewModel: TranslateViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        textInputPanelBinding = binding.textInputPanel
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadLanguages()
        savedInstanceState?.getString(TEXT_SOURCE)?.let { viewModel.setSourceText(it) }
        savedInstanceState?.getString(TEXT_TRANSLATE)?.let { viewModel.setTranslate(it) }
        binding.textViewLangSource.setOnClickListener {
            requireContext().startActivity(
                LangSelectionActivity.newIntent(requireContext(), LangDirection.SOURCE)
            )
        }
        binding.textViewLangTarget.setOnClickListener {
            requireContext().startActivity(
                LangSelectionActivity.newIntent(requireContext(), LangDirection.TARGET)
            )
        }
        binding.buttonTryAgain.setOnClickListener {
            viewModel.translateText(textInputPanelBinding.editTextToTranslate.text.toString())
        }
        textInputPanelBinding.buttonClear.setOnClickListener { viewModel.clearSourceText() }
        textInputPanelBinding.buttonVoiceInput.setOnClickListener {
            Toast.makeText(
                context,
                R.string.not_implemented,
                Toast.LENGTH_SHORT
            ).show()
        }
        textInputPanelBinding.buttonPlayback.setOnClickListener {
            Toast.makeText(
                context,
                R.string.not_implemented,
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.buttonSwap.setOnClickListener { viewModel.swapLanguages() }

        textInputPanelBinding.editTextToTranslate.setRawInputType(InputType.TYPE_CLASS_TEXT)
        textInputPanelBinding.editTextToTranslate.setOnEditorActionListener { v, actionId, _ ->
            var handled = false
            //when user stops typing and presses Enter
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (v.text.isNotEmpty())
                    viewModel.translateText(textInputPanelBinding.editTextToTranslate.text.toString())
                else {
                    viewModel.clearTranslate()
                    viewModel.clearSourceText()
                }
                handled = true
                v.clearFocus()
                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
            }
            handled
        }

        //when user is typing - translate in real-time
        textInputPanelBinding.editTextToTranslate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                viewModel.translateText(
                    textInputPanelBinding.editTextToTranslate.text.toString(),
                    false
                )
            }
        })

        binding.textViewTranslate.movementMethod = ScrollingMovementMethod()

        viewModel.viewModelScope.launch(Dispatchers.Main) {
            viewModel.showErrorDialog.collect { showErrorDialog ->
                showErrorDialog?.let {
                    showErrorDialog(it)
                }
            }
        }
        viewModel.textSource.observe(viewLifecycleOwner) {
            textInputPanelBinding.editTextToTranslate.setText(it)
        }
        viewModel.translate.observe(viewLifecycleOwner) {
            binding.textViewTranslate.text = it
        }
        viewModel.internetConnectionError.observe(viewLifecycleOwner) {
            binding.internetConnectionError.visibility = if (it) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        viewModel.languageSource.observe(viewLifecycleOwner) {
            binding.textViewLangSource.text = it.name
        }
        viewModel.languageTarget.observe(viewLifecycleOwner) {
            binding.textViewLangTarget.text = it.name
        }
    }

    override fun langSelected(language: Language, direction: LangDirection) {
        if (direction == LangDirection.SOURCE)
            viewModel.setLanguageSource(language)
        else
            viewModel.setLanguageTarget(language)
        viewModel.downloadLanguageModel(language)
        viewModel.translateText(textInputPanelBinding.editTextToTranslate.text.toString())
    }

    fun saveTranslate() {
        viewModel.saveTranslate()
    }

    fun setTranslate(translate: Translate) {
        viewModel.setLanguageSource(translate.languageSource)
        viewModel.setLanguageTarget(translate.languageTarget)
        viewModel.setSourceText(translate.textSource)
        viewModel.setTranslate(translate.textTarget)
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putString(
            TEXT_SOURCE,
            textInputPanelBinding.editTextToTranslate.text.toString()
        )
        savedInstanceState.putString(TEXT_TRANSLATE, binding.textViewTranslate.text.toString())
        super.onSaveInstanceState(savedInstanceState)
    }

    private fun showErrorDialog(error: Throwable) {
        Dialog().showErrorDialog(requireActivity())
    }

    companion object {
        const val TEXT_SOURCE = "TEXT_SOURCE"
        const val TEXT_TRANSLATE = "TEXT_TRANSLATE"
        fun newInstance(): TranslateFragment {
            return TranslateFragment()
        }
    }

}
