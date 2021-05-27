package com.example.agavacovid;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.provider.BaseColumns;
import android.widget.Toast;

import com.example.agavacovid.persistence.AgavaContract;
import com.example.agavacovid.persistence.DbHelper;

import java.io.IOException;

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
import java.util.UUID;


public class ClientBTClass extends Thread {
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private static final String APP_NAME = "AgavaCOVID";
    private static final UUID MY_UUID=UUID.fromString("b485f10d-9b3d-4682-b779-9d69ec2a2db5");
    private SendReceive sendReceive;
    private Context context;
    private Handler handler;
    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING=2;
    static final int STATE_CONNECTED=3;
    static final int STATE_CONNECTION_FAILED=4;
    static final int STATE_MESSAGE_RECEIVED=5;

    public ClientBTClass(BluetoothDevice device1, Context context, Handler handler) {
        device = device1;
        this.context = context;
        this.handler = handler;
        try {
            socket = device.createRfcommSocketToServiceRecord(MY_UUID);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        super.run();

        try {

            socket.connect();
            Message message=Message.obtain();
            message.what=STATE_CONNECTED;
            handler.sendMessage(message);
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
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //2021-05-26 17:00:00

                try {
                    c.setTime(sdf.parse(fechagen));
                } catch (ParseException e) {
                    e.printStackTrace();

                }

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
            OutputStream os = socket.getOutputStream();
            os.write((idsfecha.get(listafechas.get(listafechas.size()-1)).getBytes()));
            cursor.close();
            dbHelper.close();
            message = Message.obtain();
            message.what=STATE_MESSAGE_RECEIVED;
            handler.sendMessage(message);
            //sendReceive = new SendReceive(socket, context, handler);
            //sendReceive.start();
            //sendReceive.write("CACNEA".getBytes());
            //sendReceive.write("NOIVERN".getBytes());


        } catch (IOException e) {
            e.printStackTrace();
            Message message=Message.obtain();
            message.what=STATE_CONNECTION_FAILED;
            handler.sendMessage(message);

        }
    }
}