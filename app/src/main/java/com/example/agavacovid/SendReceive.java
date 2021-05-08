package com.example.agavacovid;

import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.widget.Toast;

import com.example.agavacovid.persistence.AgavaContract;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;


public class SendReceive extends Thread
{
    private final BluetoothSocket bluetoothSocket;
    private final InputStream inputStream;
    private final OutputStream outputStream;
    private Context context;

    public SendReceive (BluetoothSocket socket, Context context)
    {
        bluetoothSocket=socket;
        InputStream tempIn=null;
        OutputStream tempOut=null;
        this.context = context;

        try {
            tempIn=bluetoothSocket.getInputStream();
            tempOut=bluetoothSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        inputStream=tempIn;
        outputStream=tempOut;
    }

    public void run()
    {
        byte[] buffer=new byte[1024]; //poner del tamaño de ids
        int bytes;

        while (true)
        {
            try {
                bytes = inputStream.read(buffer);
                Date fecha_rec = new Date();
                String tempMsg=new String(buffer,0, bytes);
                Toast.makeText(context,tempMsg, Toast.LENGTH_LONG).show();
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.clear();
                values.put(AgavaContract.IdsAjenos.ID_EF, tempMsg);
                values.put(AgavaContract.IdsAjenos.FECHA_REC, fecha_rec.toString());

                long newRowId = db.insert(AgavaContract.IDS_AJENOS_TABLA, null, values);

                Toast.makeText(context, "Has recibido un id. Tabla id = " + newRowId + "DirBlue = " + bluetoothSocket.getRemoteDevice().getAddress(), Toast.LENGTH_LONG).show();


                //CREAR LA QUERY CON LOS DATOS RECIBIDOS E INSERTAR
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(byte[] bytes)
    {
        try {
            // ACCESO A BASE DE DATOS Y PASAMOS UNICAMNETE LOS DATOS. LA CONSULTA SE CREA AL RECIBIR
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String[] projection = {
                    BaseColumns._ID,
                    AgavaContract.IdsPropios.ID_EF,
                    AgavaContract.IdsPropios.CLAVE,
                    AgavaContract.IdsPropios.FECHA_GEN,
            };

            // Filter results WHERE "title" = 'My Title'
            String selection = FeedEntry.COLUMN_NAME_TITLE + " = ?";
            String[] selectionArgs = { "My Title" };

        // Divides el dia en 96 cachos, los 96 cuartos de hora, Cada cacho lo asignas a su rango, dependiendo de en que rango caiga coges el que cae en rango y coja la cota inferior para seleccionar la fecha.

            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}








