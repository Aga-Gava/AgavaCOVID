package com.example.agavacovid;


import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Message;
import android.widget.Toast;

import java.io.IOException;

import java.util.UUID;


public class ClientBTClass extends Thread {
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private static final String APP_NAME = "AgavaCOVID";
    private static final UUID MY_UUID=UUID.fromString("b485f10d-9b3d-4682-b779-9d69ec2a2db5");
    private SendReceive sendReceive;
    private Context context;


    public ClientBTClass(BluetoothDevice device1, Context context) {
        device = device1;
        this.context = context;

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
        Toast.makeText(context,
                "Has recibido un virus (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧ ", Toast.LENGTH_SHORT).show();
        try {
            socket.connect();
            Toast.makeText(context,
                    "Has recibido un virus (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧ " + socket, Toast.LENGTH_SHORT).show();
            sendReceive = new SendReceive(socket,context);
            sendReceive.start();
            sendReceive.write("CACNEA".getBytes());
            //sendReceive.write("NOIVERN".getBytes());

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}