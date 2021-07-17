package com.example.agavacovid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agavacovid.persistence.AgavaContract;
import com.example.agavacovid.persistence.DbHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author Juan Velazquez Garcia
 * @author Maria Ruiz Molina
 */
public class InfoActivity extends AppCompatActivity {
    BluetoothAdapter bluetoothAdapter;
    int REQUEST_ENABLE_BLUETOOTH=1;

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

        if(!bluetoothAdapter.isEnabled()){

            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH);
        }

        Bundle b = getIntent().getExtras();
        int estado = 0;
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
                imagen.setImageResource(R.drawable.agava_contagio);
                consejo1.setText(R.string.consejo1rojo);
                consejo2.setText(R.string.consejo2rojo);
                consejo3.setText(R.string.consejo3rojo);
                mensaje.setText(R.string.mensajerojo);
                break;

        }
        DbHelper dbHelper = new DbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                AgavaContract.IdsAjenos.ID_EF,
                AgavaContract.IdsAjenos.FECHA_REC,
        };


        Cursor cursor = db.query(
                AgavaContract.IDS_AJENOS_TABLA,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        Map<Date, String> idsfecha = new HashMap<>();

        while(cursor.moveToNext()) {
            String idEf = cursor.getString(
                    cursor.getColumnIndexOrThrow(AgavaContract.IdsAjenos.ID_EF));
            String fecharec = cursor.getString(
                    cursor.getColumnIndexOrThrow(AgavaContract.IdsAjenos.FECHA_REC));

           Calendar c= Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //2021-05-26 17:00:00

            try {
                c.setTime(sdf.parse(fecharec));
            } catch (ParseException e) {
                e.printStackTrace();

            }

            idsfecha.put(c.getTime(), idEf);
        }
        List<Date> listafechas = new ArrayList<>();
        for(Date d: idsfecha.keySet()){
            listafechas.add(d);
        }

        Collections.sort(listafechas, new Comparator<Date>() {
            public int compare(Date o1, Date o2) {
                return o1.compareTo(o2);
            }
        });

        cursor.close();
        dbHelper.close();
    }


}
