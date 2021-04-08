package com.example.agavacovid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Calendar;

public class SendActivity extends AppCompatActivity  {

    private View vistaEnvio;
    private EditText fecha;
    private Button buttonConfirmacion;
    private DatePickerDialog picker;
    private static final long CATORCE_DIAS = 1209600000; //El numero del demonio son 14 dias en milisegundos T-T

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        vistaEnvio = (View) findViewById(R.id.vistaEnvio);
        fecha = (EditText) vistaEnvio.findViewById(R.id.etPlannedDate);
        buttonConfirmacion = (Button) vistaEnvio.findViewById(R.id.buttonConfirmacion);

        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(SendActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                fecha.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.getDatePicker().setMaxDate(System.currentTimeMillis());
                picker.getDatePicker().setMinDate(System.currentTimeMillis() - CATORCE_DIAS);
                picker.show();
            }
        });

        buttonConfirmacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SendActivity.this, PopUp.class));
            }
        });
    }

}