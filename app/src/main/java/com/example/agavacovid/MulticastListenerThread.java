package com.example.agavacovid;

import android.app.Activity;
import android.os.Handler;

import java.io.IOException;
import java.net.DatagramPacket;

public class MulticastListenerThread extends MulticastThread {

    public MulticastListenerThread(MainActivity activity, String multicastIP, int multicastPort) {
        super("MulticastListenerThread", activity, multicastIP, multicastPort, new Handler());
    }

    @Override
    public void run() {
        super.run();

        DatagramPacket packet = new DatagramPacket(new byte[512], 512);

        while (this.getRunning()) {
            packet.setData(new byte[1024]);

            try {
                if (multicastSocket != null)
                    multicastSocket.receive(packet);
                else
                    break;
            } catch (IOException ignored) {
                continue;
            }

            String data = new String(packet.getData()).trim();

            final String consoleMessage = "[" + ((getLocalIP().equals(packet.getAddress().getHostAddress())) ? "You" : packet.getAddress().getHostAddress()) + "] " + data + "\n";

            this.getHandler().post(() -> getActivity().outputTextToConsole(consoleMessage));
        }
        if (multicastSocket != null)
            this.multicastSocket.close();
    }

}