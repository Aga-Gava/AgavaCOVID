package com.example.agavacovid.ui.dashboard;

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

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private TextView descripcion;
    private TextView tituloPolitica;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        descripcion = (TextView) root.findViewById(R.id.textView4);
        tituloPolitica = (TextView) root.findViewById(R.id.textView5);

        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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