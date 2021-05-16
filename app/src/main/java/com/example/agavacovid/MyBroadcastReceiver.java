package com.example.agavacovid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadcastReceiver";

    private Context context;

    public MyBroadcastReceiver(){

    }

    public MyBroadcastReceiver(Context context){
        super();
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Map<String, Date> mNewDevicesMap = new HashMap<>();
        String action = intent.getAction();
        Toast.makeText(context,
                "Has recibido un cacnea (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧", Toast.LENGTH_SHORT).show();
        if(BluetoothAdapter.getDefaultAdapter().isDiscovering()){
            Toast.makeText(context,
                    "Has recibido una virusaaa27 (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧", Toast.LENGTH_SHORT).show();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                Toast.makeText(context.getApplicationContext(), "Found device " + device.getName(), Toast.LENGTH_SHORT).show();
                String direccionDevice = device.getAddress();
                mNewDevicesMap.put(direccionDevice, new Date());
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                Calendar c = Calendar.getInstance();
                c.setTime(mNewDevicesMap.get(direccionDevice));
                c.add(Calendar.SECOND, 30);
                Date fecha = c.getTime();
                while(!new Date().after(fecha)){ //Habria que comprobar también la intensidad de señal

                }
                Toast.makeText(context.getApplicationContext(),"Espera finalizada", Toast.LENGTH_SHORT).show();

                //!!!!!COMPROBAR QUE EL DEVICE SE MANTIENE CUNADO CANCELAMOS DISCOVERY

                /*ServerBTClass serverClass=new ServerBTClass(context, handler);
                serverClass.start();

                ClientBTClass clientClass=new ClientBTClass(device, context, handler);
                clientClass.start();
                    */
                //insercion de base de datos
            }
        }

           /* if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismis progress dialog
            } else*/

    }
}
