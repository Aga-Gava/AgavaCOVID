package com.example.agavacovid;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class PopUp extends Activity {

    private Button buttonAceptar;
    private Button buttonCancelar;
    private static TextView textTituloPopUp;
    BluetoothAdapter bluetoothAdapter;
    int REQUEST_ENABLE_BLUETOOTH=1;
    private Map<String, Date> mNewDevicesMap;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismis progress dialog
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                Toast.makeText(getApplicationContext(), "Found device "+ device.getName(), Toast.LENGTH_SHORT).show();
                mNewDevicesMap.put(device.getAddress(), new Date());
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_up);
        getWindow().getDecorView().setBackground(new ColorDrawable(Color.WHITE));

        bluetoothAdapter= BluetoothAdapter.getDefaultAdapter();
        mNewDevicesMap = new HashMap<>();


        if(!bluetoothAdapter.isEnabled()){

            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,130);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
        }


        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }


        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);

        bluetoothAdapter.startDiscovery();



        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.8), (int) (height*.55));


        buttonAceptar = (Button) findViewById(R.id.buttonAceptar);
        buttonCancelar = (Button) findViewById(R.id.buttonCancelar);
        textTituloPopUp = (TextView) findViewById(R.id.textTituloPopUp);



        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);



                try {
                    AgavaClient cli = new AgavaClient(); //Se crea el cliente

                    //System.out.println("Iniciando cliente\n");
                    cli.startClient(); //Se inicia el cliente

                    System.out.println("Salgo :3");
                }catch (IOException e){
                    System.out.println(e);
                }


                System.out.println("Buenas noches tengan todos");
                //startActivity(new Intent(PopUp.this, MainActivity.class));

            }
        });


        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });



    }
}
