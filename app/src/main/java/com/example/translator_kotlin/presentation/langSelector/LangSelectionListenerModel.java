package com.example.translator_kotlin.presentation.langSelector;

import com.example.translator_kotlin.data.LangDirection;
import com.example.translator_kotlin.domain.model.Language;

public class LangSelectionListenerModel {

    public interface LangSelectionListener {
        void langSelected(Language language, LangDirection direction);
    }

    private static LangSelectionListenerModel mInstance;
    private LangSelectionListener mListener;

    private LangSelectionListenerModel() {}

    public static LangSelectionListenerModel getInstance() {
        if(mInstance == null) {
            mInstance = new LangSelectionListenerModel();
        }
        return mInstance;
    }

    public void setListener(LangSelectionListener listener) {
        mListener = listener;
    }

    public void langSelected(Language language, LangDirection direction) {
        if(mListener != null) {
            mListener.langSelected(language, direction);
        }
    }

}
