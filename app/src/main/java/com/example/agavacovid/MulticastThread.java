package com.example.agavacovid;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

public class MulticastThread extends Thread {

    private Boolean running = new Boolean(true);
    private MainActivity activity;
    private String multicastIP;
    private int multicastPort;
    private Handler handler;

    MulticastSocket multicastSocket;
    private InetAddress inetAddress;

    MulticastThread(String threadName, MainActivity activity, String multicastIP, int multicastPort, Handler handler) {
        super(threadName);
        this.activity = activity;
        this.multicastIP = multicastIP;
        this.multicastPort = multicastPort;
        this.handler = handler;
    }

    @Override
    public void run() {
        try {
            WifiManager wifiManager = (WifiManager) activity.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int wifiIPInt = wifiInfo.getIpAddress();
            byte[] wifiIPByte = new byte[]{
                    (byte) (wifiIPInt & 0xff),
                    (byte) (wifiIPInt >> 8 & 0xff),
                    (byte) (wifiIPInt >> 16 & 0xff),
                    (byte) (wifiIPInt >> 24 & 0xff)};
            this.inetAddress = InetAddress.getByAddress(wifiIPByte);
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(inetAddress);

            this.multicastSocket = new MulticastSocket(multicastPort);
            multicastSocket.setNetworkInterface(networkInterface);
            multicastSocket.joinGroup(InetAddress.getByName(multicastIP));

        } catch (BindException e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    activity.stopListening();
                }
            });
            String error = "Error: Cannot bind Address or Port.";
            if (multicastPort < 1024)
                error += "\nTry binding to a port larger than 1024.";
            outputErrorToConsole(error);
        } catch (IOException e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    activity.stopListening();
                }
            });
            String error = "Error: Cannot bind Address or Port.\n"
                    + "An error occurred: " + e.getMessage();
            outputErrorToConsole(error);
            e.printStackTrace();
        }
    }

    String getLocalIP() {
        return this.inetAddress.getHostAddress();
    }

    private void outputErrorToConsole(final String errorMessage) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                activity.outputErrorToConsole(errorMessage);
            }
        });
    }

    void stopRunning() {
        this.running = false;
    }

    public Boolean getRunning(){
        return running;
    }

    public void setRunning(Boolean b){
        running = b;
    }

    public MainActivity getActivity(){
        return activity;
    }

    public void setActivity(MainActivity a){
        activity = a;
    }

    public Handler getHandler(){
        return handler;
    }

    public void setHandler(Handler h){
        handler = h;
    }
}