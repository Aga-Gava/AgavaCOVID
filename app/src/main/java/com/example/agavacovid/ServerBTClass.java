package com.example.agavacovid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class ServerBTClass extends Thread{
    private BluetoothServerSocket serverSocket;
    private SendReceive sendReceive;
    private static final String APP_NAME = "AgavaCOVID";
    private static final UUID MY_UUID=UUID.fromString("b485f10d-9b3d-4682-b779-9d69ec2a2db5");
    private Context context;

    public ServerBTClass(Context context){
        try {
            serverSocket= BluetoothAdapter.getDefaultAdapter().listenUsingRfcommWithServiceRecord(APP_NAME, MY_UUID);
            this.context = context;
            Toast.makeText(context,
                    "Has recibido un virus (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧ " + serverSocket, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context,
                    "Has recibido un virus (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧ ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void run()
    {
        super.run();

        BluetoothSocket socket=null;
        //SendReceive
        while (socket==null)
        {
            try {
                socket=serverSocket.accept();

                Toast.makeText(context,
                        "Has recibido un virus (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧ " + socket, Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();


            }

            if(socket!=null)
            {
                sendReceive=new SendReceive(socket,context);
                sendReceive.start();
                break;
            }
        }
    }
}
