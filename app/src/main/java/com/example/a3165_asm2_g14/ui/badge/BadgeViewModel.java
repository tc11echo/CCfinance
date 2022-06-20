package com.example.a3165_asm2_g14.ui.badge;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BadgeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public BadgeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is badge fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}