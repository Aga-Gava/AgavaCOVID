package com.example.agavacovid;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;


/**
 *
 * @author Juan
 */
public class AgavaClient extends AgavaSocket {
    public AgavaClient() throws IOException{super("cliente");} //Se usa el constructor para cliente de Conexion

    public void startClient() throws IOException{ //Método para iniciar el cliente
        try{

            //Flujo de datos hacia el servidor
            salidaServidor = new DataOutputStream(cs.getOutputStream());
            salidaServidor.writeUTF("INSERT INTO ids_infectados (clave_gen, fecha_gen, fecha_rec)"
                    + " VALUES ('cacnea', '2019-02-02', '2018-06-04')");
            //Se enviarán dos mensajes
            for (int i = 0; i < 2; i++)
            {
                //Se escribe en el servidor usando su flujo de datos
                salidaServidor.writeUTF("Este es el mensaje número " + (i+1) + "\n");
            }



            //cs.close();//Fin de la conexión

        }
        catch (Exception e){

            System.out.println(e.getMessage());
        }



        MulticastSocket socket = new MulticastSocket(4446);
        InetAddress group = InetAddress.getByName("224.0.0.251");
        socket.joinGroup(group);

        DatagramPacket packet;
        for (int i = 0; i < 5; i++) {
            byte[] buf = new byte[256];
            packet = new DatagramPacket(buf, buf.length);

            socket.receive(packet);

            System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbBBBBBBBBBBBBBBB");
            String received = new String(packet.getData());
            System.out.println("Quote of the Moment: " + received);

///////////////////////////////////////////KILL//////////////////////////////////////////////
            try{

                //Flujo de datos hacia el servidor
                salidaServidor = new DataOutputStream(cs.getOutputStream());
                salidaServidor.writeUTF("INSERT INTO ids_infectados (clave_gen, fecha_gen, fecha_rec)"
                        + " VALUES ('noivern', '2019-02-02', '2018-06-04')");
                //Se enviarán dos mensajes


            }
            catch (Exception e){

                System.out.println(e.getMessage());
            }

            ////////////////////////////////////KILL////////////////////////////////////////////////////

        }
        cs.close();
        socket.leaveGroup(group);
        socket.close();





    }
}
