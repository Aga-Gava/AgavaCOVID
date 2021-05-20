package com.example.agavacovid;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.io.IOException;

import java.io.OutputStream;
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
            OutputStream os = socket.getOutputStream();
            os.write("CACNEA".getBytes());
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