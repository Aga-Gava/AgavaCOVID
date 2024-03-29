package com.example.agavacovid;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.widget.Toast;

import com.example.agavacovid.persistence.AgavaContract;
import com.example.agavacovid.persistence.DbHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Juan Velazquez Garcia
 * @author Maria Ruiz Molina
 */
public class MainActivity extends AppCompatActivity {
    BluetoothAdapter bluetoothAdapter;
    int REQUEST_ENABLE_BLUETOOTH=1;
    private MulticastListenerThread multicastListenerThread;
    private boolean isListening = false;
    private WifiManager.MulticastLock wifiLock;
    private List<String> listaHashes;
    private static int estado = 0;
    private DbHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setItemBackgroundResource(R.drawable.background_selector);
        this.getSupportActionBar().hide();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();



        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        dbHelper= new DbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();



        values.clear();
        values.put(AgavaContract.IdsPropios.ID_EF, "910a5497fa536069da1e00aecb93e45af7943b93789933d9b61cf814b35533eb");
        values.put(AgavaContract.IdsPropios.CLAVE_GEN, "MC4CAQAwBQYDK2VwBCIEIPtCxE+YheK+57B6xfjrGhjeYzc2MUkOGlaynnVEO/L8");
        values.put(AgavaContract.IdsPropios.FECHA_GEN, "2021-05-26 17:00:00"); //yyyy-MM-dd HH:mm:ss
        db.insert(AgavaContract.IDS_PROPIOS_TABLA, null, values);

        values.clear();
        values.put(AgavaContract.IdsPropios.ID_EF, "0ab330eb9fae7fcca4ed7f0f65f018235693969d5cdd4720f437ea43004df0fe");
        values.put(AgavaContract.IdsPropios.CLAVE_GEN, "910a5497fa536069da1e00aecb93e45af7943b93789933d9b61cf814b35533eb");
        values.put(AgavaContract.IdsPropios.FECHA_GEN, "2021-05-26 17:15:00");
        db.insert(AgavaContract.IDS_PROPIOS_TABLA, null, values);

        values.clear();
        values.put(AgavaContract.IdsPropios.ID_EF, "f64757888e6330d08dbfb0d2f48eca29df715adc8fa9d86fd97d48003a1cfe12");
        values.put(AgavaContract.IdsPropios.CLAVE_GEN, "0ab330eb9fae7fcca4ed7f0f65f018235693969d5cdd4720f437ea43004df0fe");
        values.put(AgavaContract.IdsPropios.FECHA_GEN, "2021-05-26 17:30:00");
        db.insert(AgavaContract.IDS_PROPIOS_TABLA, null, values);

        values.clear();
        values.put(AgavaContract.IdsAjenos.ID_EF, "8a19a2c9bd2408ab43862183bea9e3b1601b4f87a1d81162d2bd6618a4104db7");
        values.put(AgavaContract.IdsAjenos.FECHA_REC, "2021-06-16 17:30:00");
        db.insert(AgavaContract.IDS_AJENOS_TABLA, null, values);

        dbHelper.close();

        listaHashes = new ArrayList<>();


        if (this.isListening) {
            stopListening();
        } else {
            startListening();
        }

        bluetoothAdapter= BluetoothAdapter.getDefaultAdapter();


        if(!bluetoothAdapter.isEnabled()){

            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        stopListening();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
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
                outputErrorToConsole(getString(R.string.error_wifi));
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
        Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("NewApi")
    public void comprobarHash(String message) {
        String[] arrSplit = message.split(",");
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        StringBuffer result = new StringBuffer();
        byte[] hash = digest.digest(arrSplit[0].getBytes());
        for (byte byt : hash) result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        String hashString = result.toString();

        listaHashes.add(hashString);
        if(comprobarIDsContagiados()){
            if(estado == 0){
                estado = 1;
                recreate();
            }
        }
    }

    public boolean comprobarIDsContagiados(){

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

        return !Collections.disjoint(listaHashes, listaIdsAjenosBD); //true si hay interseccion, false sino

    }

    public static int getEstado(){
        return estado;
    }

    public static void setEstado(int estado){
        MainActivity.estado = estado;
    }

}