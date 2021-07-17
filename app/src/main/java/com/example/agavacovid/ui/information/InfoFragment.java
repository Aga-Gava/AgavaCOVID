package com.example.agavacovid.ui.information;

import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.agavacovid.R;
/**
 * @author Juan Velazquez Garcia
 * @author Maria Ruiz Molina
 */
public class InfoFragment extends Fragment {

    private InfoViewModel infoViewModel;
    private TextView descripcion;
    private TextView tituloPolitica;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        infoViewModel =
                new ViewModelProvider(this).get(InfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        descripcion = (TextView) root.findViewById(R.id.textView4);
        tituloPolitica = (TextView) root.findViewById(R.id.textView5);

        infoViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                descripcion.getText().toString().replace('\n', System.lineSeparator().toCharArray()[0]);
                tituloPolitica.getText().toString().replace('\n', System.lineSeparator().toCharArray()[0]);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    descripcion.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
                }
            }
        });
        return root;
    }
}