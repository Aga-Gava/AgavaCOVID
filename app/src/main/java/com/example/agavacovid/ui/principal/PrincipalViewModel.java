package com.example.agavacovid.ui.principal;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
/**
 * @author Juan Velazquez Garcia
 * @author Maria Ruiz Molina
 */
public class PrincipalViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PrincipalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}