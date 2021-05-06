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


public class HomeFragment extends Fragment{

    private HomeViewModel homeViewModel;
    private Button info;
    private ImageButton buttonInfo;
    private ImageButton buttonEnvio;
    private TextView textButtonEnvio;
    private TextView textButtonInfo;
    private TextView textButtonInfoPlus;
    private ImageView agava;
    private final MyBroadcastReceiver br = new MyBroadcastReceiver(getContext());

    /*private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            Toast.makeText(getContext(),
                    "Has recibido un cacnea (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧", Toast.LENGTH_SHORT).show();
            if(bluetoothAdapter.isDiscovering()){
                Toast.makeText(getContext(),
                        "Has recibido una virusaaa27 (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧", Toast.LENGTH_SHORT).show();
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    //bluetooth device found
                    BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    Toast.makeText(context.getApplicationContext(), "Found device " + device.getName(), Toast.LENGTH_SHORT).show();
                    String direccionDevice = device.getAddress();
                    mNewDevicesMap.put(direccionDevice, new Date());
                    bluetoothAdapter.cancelDiscovery();
                    Calendar c = Calendar.getInstance();
                    c.setTime(mNewDevicesMap.get(direccionDevice));
                    c.add(Calendar.SECOND, 30);
                    Date fecha = c.getTime();
                    while(!new Date().after(fecha)){ //Habria que comprobar también la intensidad de señal

                    }
                    Toast.makeText(context.getApplicationContext(),"Espera finalizada", Toast.LENGTH_SHORT).show();

                    //!!!!!COMPROBAR QUE EL DEVICE SE MANTIENE CUNADO CANCELAMOS DISCOVERY

                    ServerBTClass serverClass=new ServerBTClass(getContext());
                    serverClass.start();

                    ClientBTClass clientClass=new ClientBTClass(device, getContext());
                    clientClass.start();

                    //insercion de base de datos
                }
            }

           /* if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismis progress dialog
            } else comentarion de cerrar

        }
    };*/

    private BluetoothAdapter bluetoothAdapter;
    private Map<String, Date> mNewDevicesMap;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        int estado = 1; //0 es verde, 1 es amarillo, 2 es rojo

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
                mNewDevicesMap = new HashMap<>();
                bluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
                bluetoothAdapter.startDiscovery();

                Toast.makeText(getContext(),
                        "Holaaaaa"+ bluetoothAdapter.isDiscovering(), Toast.LENGTH_SHORT).show();

                IntentFilter filter = new IntentFilter();

                filter.addAction(BluetoothDevice.ACTION_FOUND);
                //filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
                //filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

                getActivity().registerReceiver(br, filter);
                Toast.makeText(getContext(),
                        "Has recibido un virus (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧", Toast.LENGTH_SHORT).show();
                //envia cuando este discoverable

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


    /*private void pairDevice(BluetoothDevice device) {
        try {
            if (D)
                Log.d(TAG, "Start Pairing...");

            waitingForBonding = true;

            Method m = device.getClass()
                    .getMethod("createBond", (Class[]) null);
            m.invoke(device, (Object[]) null);

            if (D)
                Log.d(TAG, "Pairing finished.");
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }*/

  /*  private void unpairDevice(BluetoothDevice device) {
        try {
            Method m = device.getClass()
                    .getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }*/

    public boolean createBond(BluetoothDevice btDevice)
            throws Exception
    {
        Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
        Method createBondMethod = class1.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }

}