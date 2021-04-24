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

import com.example.agavacovid.InfoActivity;
import com.example.agavacovid.R;
import com.example.agavacovid.SendActivity;


public class HomeFragment extends Fragment{

    private HomeViewModel homeViewModel;
    private Button info;
    private ImageButton buttonInfo;
    private ImageButton buttonEnvio;
    private TextView textButtonEnvio;
    private TextView textButtonInfo;
    private TextView textButtonInfoPlus;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        int estado = 2; //0 es verde, 1 es amarillo, 2 es rojo

        buttonInfo = (ImageButton) view.findViewById(R.id.buttonInfo);
        textButtonInfo = (TextView) view.findViewById(R.id.textButtonInfo);
        textButtonInfoPlus = (TextView) view.findViewById(R.id.textButtonInfoPlus);
        buttonEnvio = (ImageButton) view.findViewById(R.id.buttonEnvio);
        textButtonEnvio = (TextView) view.findViewById(R.id.textButtonEnvio);

        switch (estado){
            case 0:
                buttonInfo.setImageResource(R.drawable.custom_button_info);
                textButtonInfo.setText(R.string.sincontactos);
                textButtonInfoPlus.setText(R.string.sincontactosplus);
                break;
            case 1:
                buttonInfo.setImageResource(R.drawable.custom_button_info2);
                textButtonInfo.setText(R.string.concontactos);
                textButtonInfoPlus.setText(R.string.concontactosplus);
                break;
            case 2:
                buttonInfo.setImageResource(R.drawable.custom_button_info3);
                textButtonInfo.setText(R.string.contagiado);
                textButtonInfoPlus.setText(R.string.contagiadoplus);
                break;
        }

        // Listeners
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InfoActivity.class);
                Bundle b = new Bundle();

                switch (estado){
                    case 0:
                        b.putInt("estado", 0);
                        intent.putExtras(b);
                        break;
                    case 1:
                        b.putInt("estado", 1);
                        intent.putExtras(b);
                        break;
                    case 2:
                        b.putInt("estado", 2);
                        intent.putExtras(b);
                        break;
                }
                startActivity(intent);
            }
        });
        buttonEnvio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SendActivity.class));

            }
        });
        textButtonEnvio.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SendActivity.class));

            }
        });

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return view;
    }

}