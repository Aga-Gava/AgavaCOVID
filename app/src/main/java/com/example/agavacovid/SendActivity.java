package com.example.agavacovid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
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
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Juan Velazquez Garcia
 * @author Maria Ruiz Molina
 */
public class SendActivity extends AppCompatActivity  {

    private View vistaEnvio;
    private EditText code;
    private EditText fecha;
    private Button buttonConfirmacion;
    private DatePickerDialog picker;
    private static final long CATORCE_DIAS = 1209600000; //El numero del demonio son 14 dias en milisegundos T-T
    BluetoothAdapter bluetoothAdapter;
    int REQUEST_ENABLE_BLUETOOTH=1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bluetoothAdapter= BluetoothAdapter.getDefaultAdapter();

        if(!bluetoothAdapter.isEnabled()){

            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
        }


        vistaEnvio = (View) findViewById(R.id.vistaEnvio);
        code =  (EditText) vistaEnvio.findViewById(R.id.outlinedTextField);
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
                                fecha.setText(year + "-" + String.format("%02d", monthOfYear + 1) + "-" + String.format("%02d", dayOfMonth));
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
                String fecharec;

                if(code.getText().equals("")){
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.mensaje_vacio), Toast.LENGTH_LONG).show();
                }else {
                    if (code.getText().length() < 12) {
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.mensaje_incompleto), Toast.LENGTH_LONG).show();
                    } else {
                        if(fecha.getText() ==null){
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date fecharecdate = new Date();
                            sdf.format(fecharecdate);
                            fecharec = fecharecdate.toString();
                        }else{
                            fecharec = String.valueOf(fecha.getText()) + " " + "00" + ":" + "00" + ":" + "00";
                        }
                        Intent intent = new Intent(SendActivity.this, PopUp.class);
                        Bundle b = new Bundle();
                        b.putLong("code", Long.parseLong(String.valueOf(code.getText())));
                        b.putString("fecha", fecharec);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                }
            }
        });
    }

}