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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class InfoActivity extends AppCompatActivity {
    BluetoothAdapter bluetoothAdapter;
    int REQUEST_ENABLE_BLUETOOTH=1;

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

    private Map<String, Date> mNewDevicesMap;

    private ImageView imagen;
    private TextView consejo1;
    private TextView consejo2;
    private TextView consejo3;
    private TextView mensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
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



        Bundle b = getIntent().getExtras();
        int estado = 0; // or other values
        if(b != null)
            estado = b.getInt("estado");

        imagen = (ImageView) findViewById(R.id.imagen);
        consejo1 = (TextView) findViewById(R.id.consejo1);
        consejo2 = (TextView) findViewById(R.id.consejo2);
        consejo3 = (TextView) findViewById(R.id.consejo3);
        mensaje = (TextView) findViewById(R.id.mensaje);

        switch (estado){
            case 0:
                imagen.setImageResource(R.drawable.agava_bien);
                consejo1.setText(R.string.consejo1verde);
                consejo2.setText(R.string.consejo2verde);
                consejo3.setText(R.string.consejo3verde);
                mensaje.setText(R.string.mensajeverde);
                break;
            case 1:
                imagen.setImageResource(R.drawable.agava_concontactos);
                consejo1.setText(R.string.consejo1amarillo);
                consejo2.setText(R.string.consejo2amarillo);
                consejo3.setText(R.string.consejo3amarillo);
                mensaje.setText(R.string.mensajeamarillo);
                break;
            case 2:
                imagen.setImageResource(R.drawable.rojo);
                consejo1.setText(R.string.consejo1rojo);
                consejo2.setText(R.string.consejo2rojo);
                consejo3.setText(R.string.consejo3rojo);
                mensaje.setText(R.string.mensajerojo);
                break;

        }
    }
}
