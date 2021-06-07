package com.example.agavacovid.ui.home;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.agavacovid.ClientBTClass;
import com.example.agavacovid.InfoActivity;
import com.example.agavacovid.MainActivity;
import com.example.agavacovid.MyBroadcastReceiver;
import com.example.agavacovid.R;
import com.example.agavacovid.SendActivity;
import com.example.agavacovid.ServerBTClass;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


public class HomeFragment extends Fragment{

    private HomeViewModel homeViewModel;
    private ImageButton buttonInfo;
    private ImageButton buttonEnvio;
    private TextView textButtonEnvio;
    private TextView textButtonInfo;
    private TextView textButtonInfoPlus;
    private ImageView agava;


    private BluetoothAdapter bluetoothAdapter;

    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING=2;
    static final int STATE_CONNECTED=3;
    static final int STATE_CONNECTION_FAILED=4;
    static final int STATE_MESSAGE_RECEIVED=5;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        int estado = MainActivity.getEstado(); //0 es verde, 1 es amarillo, 2 es rojo

        agava = (ImageView) view.findViewById(R.id.imageAgava);
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

        agava.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                MainActivity.setEstado(0);
                Handler handler=new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {

                        switch (msg.what)
                        {
                            case STATE_LISTENING:
                                Toast.makeText(getContext(),
                                        "Escuchando", Toast.LENGTH_SHORT).show();
                                break;
                            case STATE_CONNECTING:
                                Toast.makeText(getContext(),
                                        "Conectando", Toast.LENGTH_SHORT).show();
                                break;
                            case STATE_CONNECTED:
                                Toast.makeText(getContext(),
                                        "Conectado", Toast.LENGTH_SHORT).show();
                                break;
                            case STATE_CONNECTION_FAILED:
                                Toast.makeText(getContext(),
                                        "Conexión fallida", Toast.LENGTH_SHORT).show();
                                break;
                            case STATE_MESSAGE_RECEIVED:
                                byte[] readBuff= (byte[]) msg.obj;
                                String tempMsg=new String(readBuff,0,msg.arg1);
                                Toast.makeText(getContext(),
                                        "Mensaje recibido: " + tempMsg, Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                });

                bluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
                Set<BluetoothDevice> bt=bluetoothAdapter.getBondedDevices();
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
               ServerBTClass serverBTClass = new ServerBTClass(getContext(), handler);
                serverBTClass.start();

                int i = 1;
                for(BluetoothDevice b: bt){
                    ClientBTClass clientBTClass = new ClientBTClass(b, getContext(), handler);
                    clientBTClass.start();
                }

            }
        }
        );



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