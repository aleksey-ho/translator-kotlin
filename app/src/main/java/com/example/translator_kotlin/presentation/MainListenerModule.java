package com.example.translator_kotlin.presentation;

import com.example.translator_kotlin.domain.model.Translate;

public class MainListenerModule {
    public interface MainListener {
        void openTranslate(Translate translate);
    }

    private static MainListenerModule mInstance;
    private MainListener mListener;

    private MainListenerModule() {}

    public static MainListenerModule getInstance() {
        if(mInstance == null) {
            mInstance = new MainListenerModule();
        }
        return mInstance;
    }

    public void setListener(MainListener listener) {
        mListener = listener;
    }

    public void openTranslate(Translate translate) {
        if(mListener != null) {
            mListener.openTranslate(translate);
        }
    }
}
