package com.example.agavacovid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.io.IOException;


public class PopUp extends Activity {

    private Button buttonAceptar;
    private Button buttonCancelar;
    private static TextView textTituloPopUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_up);
        getWindow().getDecorView().setBackground(new ColorDrawable(Color.WHITE));

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
