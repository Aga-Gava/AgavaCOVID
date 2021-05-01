package com.example.agavacovid;


import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Message;

import java.io.IOException;

import java.util.UUID;


public class ClientBTClass extends Thread {
    private BluetoothDevice device;
    private BluetoothSocket socket;
    private static final UUID MY_UUID=UUID.fromString("8ce255c0-223a-11e0-ac64-0803450c9a66"); //CAMBIARLO MUY FUERTEMENTE



    public ClientBTClass(BluetoothDevice device1) {
        device = device1;

        try {
            socket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            socket.connect();
            sendReceive = new SendReceive(socket);
            sendReceive.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}