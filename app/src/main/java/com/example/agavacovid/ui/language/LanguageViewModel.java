package com.example.agavacovid.ui.language;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
/**
 * @author Juan Velazquez Garcia
 * @author Maria Ruiz Molina
 */
public class LanguageViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public LanguageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}