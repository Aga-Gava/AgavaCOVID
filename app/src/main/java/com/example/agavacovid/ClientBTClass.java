package com.example.agavacovid;


import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Message;

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

    public void run() {
        try {
            socket.connect();
            sendReceive = new SendReceive(socket,context);
            sendReceive.start();
            sendReceive.write("CACNEA".getBytes());
            //sendReceive.write("NOIVERN".getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}