package com.example.agavacovid;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.widget.Toast;

import com.example.agavacovid.persistence.AgavaContract;
import com.example.agavacovid.persistence.DbHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    BluetoothAdapter bluetoothAdapter;
    int REQUEST_ENABLE_BLUETOOTH=1;
    private MulticastSocket socket;
    private InetAddress group;
    private MulticastListenerThread multicastListenerThread;
    private boolean isListening = false;
    private WifiManager.MulticastLock wifiLock;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

           /* if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismis progress dialog
            } else*/

        }
    };

    //private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private DbHelper dbHelper;
    private SQLiteDatabase db;

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


/*
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiManager.MulticastLock multicastLock = wifi.createMulticastLock("multicastLock");
        multicastLock.setReferenceCounted(true);
        multicastLock.acquire();
*/
        dbHelper= new DbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();



        values.clear();
        values.put(AgavaContract.IdsPropios.ID_EF, "910a5497fa536069da1e00aecb93e45af7943b93789933d9b61cf814b35533eb");
        values.put(AgavaContract.IdsPropios.CLAVE_GEN, "MC4CAQAwBQYDK2VwBCIEIPtCxE+YheK+57B6xfjrGhjeYzc2MUkOGlaynnVEO/L8");
        values.put(AgavaContract.IdsPropios.FECHA_GEN, "2021-05-26 17:00:00"); //yyyy-MM-dd HH:mm:ss
        db.insert(AgavaContract.IDS_PROPIOS_TABLA, null, values);

        values.clear();
        values.put(AgavaContract.IdsPropios.ID_EF, "6b1bf72b210dabcd460870abc1d6c438be23666dba14d970b2032e2f12ff2597");
        values.put(AgavaContract.IdsPropios.CLAVE_GEN, "910a5497fa536069da1e00aecb93e45af7943b93789933d9b61cf814b35533eb");
        values.put(AgavaContract.IdsPropios.FECHA_GEN, "2021-05-26 17:15:00");
        db.insert(AgavaContract.IDS_PROPIOS_TABLA, null, values);

        values.clear();
        values.put(AgavaContract.IdsPropios.ID_EF, "0b544e851e236f6f70b33dfd93fab8df2cfd95975981bf627834a391851a1578");
        values.put(AgavaContract.IdsPropios.CLAVE_GEN, "6b1bf72b210dabcd460870abc1d6c438be23666dba14d970b2032e2f12ff2597");
        values.put(AgavaContract.IdsPropios.FECHA_GEN, "2021-05-26 17:30:00");
        db.insert(AgavaContract.IDS_PROPIOS_TABLA, null, values);

        dbHelper.close();

        /*
        values.clear();
        values.put(AgavaContract.IdsPropios.ID_EF, "1234561112411");
        values.put(AgavaContract.IdsAjenos.FECHA_REC, new Date().toString());
        db.insert(AgavaContract.IDS_AJENOS_TABLA, null, values);
*/

        if (this.isListening) {
            stopListening();
        } else {
            startListening();
        }

        bluetoothAdapter= BluetoothAdapter.getDefaultAdapter();


        if(!bluetoothAdapter.isEnabled()){

            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //enableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,60);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
        }

// es pa que cuando entres en otra actividad no se ponga a hacer discoveries ahi chungos a la vez y pete
       // if (bluetoothAdapter.isDiscovering()) {
        ///    bluetoothAdapter.cancelDiscovery();
        //}

        //bluetoothAdapter.startDiscovery();



        //IntentFilter filter = new IntentFilter();

        //filter.addAction(BluetoothDevice.ACTION_FOUND);
        //filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        //filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        //registerReceiver(mReceiver, filter);

        // Ver junticos de la mano con gente extranya de la Passion y tasar las cartas de PTCG de Gava

    }


    @Override
    protected void onStop() {
        super.onStop();
        stopListening();
    }

    private void startListening() {
        if (!isListening) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {

                    setWifiLockAcquired(true);

                    this.multicastListenerThread = new MulticastListenerThread(this, "224.0.0.251"/*La IP del multicast*/, 4446 /*Puerto de recepcion*/);
                    multicastListenerThread.start();

                    isListening = true;

            } else {
                outputErrorToConsole("Error: ¡No estás conectado a una red WiFi!");
            }
        }
    }

    void stopListening() {
        if (isListening) {
            isListening = false;

            stopThreads();
            setWifiLockAcquired(false);
        }
    }

    private void setWifiLockAcquired(boolean acquired) {
        if (acquired) {
            if (wifiLock != null && wifiLock.isHeld())
                wifiLock.release();

            WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (wifi != null) {
                this.wifiLock = wifi.createMulticastLock("MulticastTester");
                wifiLock.acquire();
            }
        } else {
            if (wifiLock != null && wifiLock.isHeld())
                wifiLock.release();
        }
    }

    private void stopThreads() {
        if (this.multicastListenerThread != null)
            this.multicastListenerThread.stopRunning();
    }

    public void outputErrorToConsole(String errorMessage) {
        outputTextToConsole(errorMessage);
    }

    public void outputTextToConsole(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void comprobarIDsContagiados(String listaIDs){
        // crear la lista de IDs separando usando unas comas o algo bonico. Tambien puede ser "aga"
        db= dbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                AgavaContract.IdsAjenos.ID_EF,
                AgavaContract.IdsAjenos.FECHA_REC,
        };

        Cursor cursor = db.query(AgavaContract.IDS_AJENOS_TABLA, projection,
                null, null, null, null, null);
        List<String> listaIdsAjenosBD = new ArrayList<>();
        while(cursor.moveToNext()) {
            String idEf = cursor.getString(
                    cursor.getColumnIndexOrThrow(AgavaContract.IdsAjenos.ID_EF));
            listaIdsAjenosBD.add(idEf);
        }

        cursor.close();
    }

}