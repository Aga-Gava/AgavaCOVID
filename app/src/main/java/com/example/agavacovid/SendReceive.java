package com.example.agavacovid;

import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.provider.BaseColumns;

import com.example.agavacovid.persistence.AgavaContract;
import com.example.agavacovid.persistence.DbHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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


public class SendReceive extends Thread
{
    private final BluetoothSocket bluetoothSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private Context context;
    private Handler handler;
    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING=2;
    static final int STATE_CONNECTED=3;
    static final int STATE_CONNECTION_FAILED=4;
    static final int STATE_MESSAGE_RECEIVED=5;

    public SendReceive (BluetoothSocket socket, Context context, Handler handler)
    {
        bluetoothSocket=socket;
        InputStream tempIn=null;
        OutputStream tempOut=null;
        this.context = context;
        this.handler = handler;

        try {

            tempIn=bluetoothSocket.getInputStream();
            tempOut=bluetoothSocket.getOutputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }

        inputStream=tempIn;
        outputStream=tempOut;
    }

    @Override
    public void run()
    {
        super.run();
        byte[] buffer=new byte[1024]; //poner del tamaño de ids
        int bytes;

        while (true)
        {
            try {
                bytes = inputStream.read(buffer);
                handler.obtainMessage(STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget();
                Date fecha_rec = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String fecha_rec_format = sdf.format(fecha_rec);
                String tempMsg=new String(buffer,0, bytes);

                //Toast.makeText(context,tempMsg, Toast.LENGTH_LONG).show();

                DbHelper dbHelper = new DbHelper(context);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.clear();
                values.put(AgavaContract.IdsAjenos.ID_EF, tempMsg);
                values.put(AgavaContract.IdsAjenos.FECHA_REC, fecha_rec_format);

                db.insert(AgavaContract.IDS_AJENOS_TABLA, null, values);

                //Toast.makeText(context, "Has recibido un id. Tabla id = " + newRowId + "DirBlue = " + bluetoothSocket.getRemoteDevice().getAddress(), Toast.LENGTH_LONG).show();


                //CREAR LA QUERY CON LOS DATOS RECIBIDOS E INSERTAR
            } catch (IOException e) {
                e.printStackTrace();
               // Toast.makeText(context, "Error en el sendreceive bien feo", Toast.LENGTH_LONG).show();

            }
        }
    }

    public void write(byte[] bytes)
    {
        try {
            // ACCESO A BASE DE DATOS Y PASAMOS UNICAMNETE LOS DATOS. LA CONSULTA SE CREA AL RECIBIR
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String[] projection = {
                    BaseColumns._ID,
                    AgavaContract.IdsPropios.ID_EF,
                    AgavaContract.IdsPropios.CLAVE_GEN,
                    AgavaContract.IdsPropios.FECHA_GEN,
            };

            // Filter results WHERE "title" = 'My Title'

            Cursor cursor = db.query(
                    AgavaContract.IDS_PROPIOS_TABLA,   // The table to query
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
                        cursor.getColumnIndexOrThrow(AgavaContract.IdsPropios.ID_EF));
                String fechagen = cursor.getString(
                        cursor.getColumnIndexOrThrow(AgavaContract.IdsPropios.FECHA_GEN));
                Calendar c= Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //Wed May 26 17:00:00 GMT+02:00 2021
                c.setTime(sdf.parse(fechagen));
                idsfecha.put(c.getTime(), idEf);
            }
            List<Date> listafechas = new ArrayList<>();
            for(Date d: idsfecha.keySet()){
                listafechas.add(d);
            }
            //listafechas.sort((d1,d2) -> d1.compareTo(d2));
            Collections.sort(listafechas, new Comparator<Date>() {
                public int compare(Date o1, Date o2) {
                    return o1.compareTo(o2);
                } //esta ordenada parriba o pabajo? :D
            });

            cursor.close();
        // Divides el dia en 96 cachos, los 96 cuartos de hora, Cada cacho lo asignas a su rango, dependiendo de en que rango caiga coges el que cae en rango y coja la cota inferior para seleccionar la fecha.
        //Esto de arriba iwal no hace falta porque la fecha gen ya es la mayor y se van sacando los chunks cada 15 min (en el final) aunque se generen todos a las 00:00 en el array gordo
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}








