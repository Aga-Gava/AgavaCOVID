package com.example.agavacovid.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.agavacovid.R;
import com.example.agavacovid.SendActivity;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private Button info;
    private ImageButton buttonInfo;
    private ImageButton buttonEnvio;
    private TextView textButtonEnvio;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        buttonInfo = (ImageButton) view.findViewById(R.id.buttonInfo);
        buttonEnvio = (ImageButton) view.findViewById(R.id.buttonEnvio);
        textButtonEnvio = (TextView) view.findViewById(R.id.textButtonEnvio);

        // Listeners
        //buttonInfo.setOnClickListener(this);
        buttonEnvio.setOnClickListener(this);
        textButtonEnvio.setOnClickListener(this);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(getActivity(), SendActivity.class));

    }


}