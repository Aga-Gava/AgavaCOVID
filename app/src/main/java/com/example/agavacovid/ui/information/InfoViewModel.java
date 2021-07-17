package com.example.agavacovid.ui.information;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
/**
 * @author Juan Velazquez Garcia
 * @author Maria Ruiz Molina
 */
public class InfoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public InfoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}