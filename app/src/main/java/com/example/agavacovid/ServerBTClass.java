package com.example.agavacovid;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;

public class ServerBTClass extends Thread{
    private BluetoothServerSocket serverSocket;

    public ServerClass(){
        try {
            serverSocket=bluetoothAdapter.listenUsingRfcommWithServiceRecord(APP_NAME, MY_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run()
    {
        BluetoothSocket socket=null;
        //SendReceive
        while (socket==null)
        {
            try {
                socket=serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();

            }

            if(socket!=null)
            {
                sendReceive=new SendReceive(socket);
                sendReceive.start();
                break;
            }
        }
    }
}
