package com.example.agavacovid;

import android.app.DatePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InfoActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_send);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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





            vistaEnvio = (View) findViewById(R.id.vistaEnvio);
            fecha = (EditText) vistaEnvio.findViewById(R.id.etPlannedDate);
            buttonConfirmacion = (Button) vistaEnvio.findViewById(R.id.buttonConfirmacion);



        }

    }


}
