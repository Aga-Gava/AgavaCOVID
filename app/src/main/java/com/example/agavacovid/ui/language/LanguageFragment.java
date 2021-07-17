package com.example.agavacovid.ui.language;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.agavacovid.R;

import java.util.Locale;
/**
 * @author Juan Velazquez Garcia
 * @author Maria Ruiz Molina
 */
public class LanguageFragment extends Fragment {

    private LanguageViewModel languageViewModel;
    private RadioButton radioES;
    private RadioButton radioEN;
    private RadioButton radioFR;
    private RadioButton radioPT;
    private RadioButton radioDE;
    private RadioButton radioIT;
    private RadioButton radioRU;
    private Button buttonIdioma;
    private Locale myLocale;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        languageViewModel =
                new ViewModelProvider(this).get(LanguageViewModel.class);
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        radioES = (RadioButton) view.findViewById(R.id.radioES);
        radioEN = (RadioButton) view.findViewById(R.id.radioEN);
        radioFR = (RadioButton) view.findViewById(R.id.radioFR);
        radioPT = (RadioButton) view.findViewById(R.id.radioPT);
        radioDE = (RadioButton) view.findViewById(R.id.radioDE);
        radioIT = (RadioButton) view.findViewById(R.id.radioIT);
        radioRU = (RadioButton) view.findViewById(R.id.radioRU);
        buttonIdioma = (Button) view.findViewById(R.id.buttonIdioma);

        buttonIdioma.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (radioES.isChecked()) {
                     setLocale("es");
                 } else if (radioEN.isChecked()) {
                     setLocale("en");
                 } else if (radioFR.isChecked()) {
                     setLocale("fr");
                 } else if (radioPT.isChecked()) {
                    setLocale("pt");
                 } else if (radioDE.isChecked()) {
                     setLocale("de");
                 } else if (radioIT.isChecked()) {
                     setLocale("it");
                 } else if (radioRU.isChecked()) {
                     setLocale("ru");
                 }
             }

        });

        languageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return view;
    }

    public void setLocale(String lang) {

        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        getActivity().recreate();
    }
}