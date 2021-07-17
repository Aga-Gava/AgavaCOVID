package com.example.agavacovid;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.agavacovid.client.AgavaClient;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * @author Juan Velazquez Garcia
 * @author Maria Ruiz Molina
 */
public class PopUp extends Activity {

    private Button buttonAceptar;
    private Button buttonCancelar;
    private static TextView textTituloPopUp;
    BluetoothAdapter bluetoothAdapter;
    int REQUEST_ENABLE_BLUETOOTH=1;
    private long code;
    private String fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_up);
        getWindow().getDecorView().setBackground(new ColorDrawable(Color.WHITE));

        Bundle b = getIntent().getExtras();
        if(b != null) {
            code = b.getLong("code");
            fecha = b.getString("fecha");
        }

        bluetoothAdapter= BluetoothAdapter.getDefaultAdapter();

        if(!bluetoothAdapter.isEnabled()){

            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width*.85), (int) (height*.65));


        buttonAceptar = (Button) findViewById(R.id.buttonAceptar);
        buttonCancelar = (Button) findViewById(R.id.buttonCancelar);
        textTituloPopUp = (TextView) findViewById(R.id.textTituloPopUp);

        buttonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);



                try {
                    AgavaClient cli = new AgavaClient(code, fecha, getApplicationContext()); //Se crea el cliente

                    cli.startClient(); //Se inicia el cliente

                }catch (IOException e){
                    System.out.println(e);
                }


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
