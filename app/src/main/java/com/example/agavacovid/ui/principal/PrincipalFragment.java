package com.example.agavacovid.ui.principal;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.agavacovid.bluetooth.ClientBTClass;
import com.example.agavacovid.InfoActivity;
import com.example.agavacovid.MainActivity;
import com.example.agavacovid.R;
import com.example.agavacovid.SendActivity;
import com.example.agavacovid.bluetooth.ServerBTClass;

import java.util.Set;

/**
 * @author Juan Velazquez Garcia
 * @author Maria Ruiz Molina
 */
public class PrincipalFragment extends Fragment{

    private PrincipalViewModel principalViewModel;
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
        principalViewModel =
                new ViewModelProvider(this).get(PrincipalViewModel.class);

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
                                        getString(R.string.escuchando), Toast.LENGTH_SHORT).show();
                                break;
                            case STATE_CONNECTING:
                                Toast.makeText(getContext(),
                                        getString(R.string.conectando), Toast.LENGTH_SHORT).show();
                                break;
                            case STATE_CONNECTED:
                                Toast.makeText(getContext(),
                                        getString(R.string.conectado), Toast.LENGTH_SHORT).show();
                                break;
                            case STATE_CONNECTION_FAILED:
                                Toast.makeText(getContext(),
                                        getString(R.string.conexion_fallida), Toast.LENGTH_SHORT).show();
                                break;
                            case STATE_MESSAGE_RECEIVED:
                                byte[] readBuff= (byte[]) msg.obj;
                                String tempMsg=new String(readBuff,0,msg.arg1);
                                Toast.makeText(getContext(),
                                        getString(R.string.mensaje_recibido) + tempMsg, Toast.LENGTH_SHORT).show();
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

        principalViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return view;
    }

}