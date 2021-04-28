package com.example.agavacovid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    BluetoothAdapter bluetoothAdapter;
    int REQUEST_ENABLE_BLUETOOTH=1;
    private MulticastSocket socket;
    private InetAddress group;
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

    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private Map<String, Date> mNewDevicesMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setItemBackgroundResource(R.drawable.background_selector);
        this.getSupportActionBar().hide();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();



        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);



        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiManager.MulticastLock multicastLock = wifi.createMulticastLock("multicastLock");
        multicastLock.setReferenceCounted(true);
        multicastLock.acquire();



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

        // Ver junticos de la mano con gente extranya de la Passion y tasar las cartas de PTCG de Gava


        //if(!bluetoothAdapter.isDiscovering()) {
            //IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
          //  this.registerReceiver(mReceiver, filter);

            //bluetoothAdapter.startDiscovery();

        //}

    }
    @Override
    protected void onResume(){
        super.onResume();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            socket = new MulticastSocket(4446);
            group = InetAddress.getByName("224.0.0.251");
            socket.joinGroup(group);

            DatagramPacket packet;
            for (int i = 0; i < 5; i++){
                byte[] buf = new byte[256];
                packet = new DatagramPacket(buf, buf.length);

                socket.receive(packet);

                String received = new String(packet.getData());
                //System.out.println("Quote of the Moment: " + received);

                //tostada
                Toast.makeText(getApplicationContext(),
                        "Has recibido un virus (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        try {
            socket.leaveGroup(group);
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket.close();
    }
}