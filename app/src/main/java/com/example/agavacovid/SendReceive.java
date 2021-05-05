package com.example.agavacovid;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


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
                String tempMsg=new String(buffer,0, bytes);
                Toast.makeText(context,tempMsg, Toast.LENGTH_LONG).show();
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
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}








