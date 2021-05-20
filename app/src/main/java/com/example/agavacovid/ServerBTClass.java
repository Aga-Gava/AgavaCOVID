package com.example.agavacovid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class ServerBTClass extends Thread{
    private BluetoothServerSocket serverSocket;
    private SendReceive sendReceive;
    private static final String APP_NAME = "AgavaCOVID";
    private static final UUID MY_UUID=UUID.fromString("b485f10d-9b3d-4682-b779-9d69ec2a2db5");
    private Context context;
    private Handler handler;
    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING=2;
    static final int STATE_CONNECTED=3;
    static final int STATE_CONNECTION_FAILED=4;
    static final int STATE_MESSAGE_RECEIVED=5;

    public ServerBTClass(Context context, Handler handler){
        try {
            serverSocket= BluetoothAdapter.getDefaultAdapter().listenUsingRfcommWithServiceRecord(APP_NAME, MY_UUID);
            this.context = context;
            this.handler = handler;
        } catch (IOException e) {
            e.printStackTrace();
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
                Message message=Message.obtain();
                message.what=STATE_CONNECTING;
                handler.sendMessage(message);

                socket=serverSocket.accept();

            } catch (IOException e) {
                e.printStackTrace();

                Message message=Message.obtain();
                message.what=STATE_CONNECTION_FAILED;
                handler.sendMessage(message);

            }

            if(socket!=null)
            {
                Message message=Message.obtain();
                message.what=STATE_CONNECTED;
                handler.sendMessage(message);

                sendReceive=new SendReceive(socket,context,handler);
                sendReceive.start();
                break;
            }
        }
    }
}
