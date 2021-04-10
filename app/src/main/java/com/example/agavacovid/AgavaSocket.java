package com.example.agavacovid;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Juan
 */
public class AgavaSocket {
    private final int PUERTO = 3384; //Puerto para la conexión
    private final String HOST = "192.168.56.1"; //Host para la conexión
    protected String mensajeServidor; //Mensajes entrantes (recibidos) en el servidor
    protected ServerSocket ss; //Socket del servidor
    protected Socket cs; //Socket del cliente
    protected DataOutputStream salidaServidor, salidaCliente; //Flujo de datos de salida

    public AgavaSocket(String tipo) throws IOException{ //Constructor
        if(tipo.equalsIgnoreCase("servidor")){
            ss = new ServerSocket(PUERTO);//Se crea el socket para el servidor en puerto 3384
            cs = new Socket(); //Socket para el cliente

        }
        else{
            cs = new Socket(HOST, PUERTO); //Socket para el cliente en localhost en puerto 3384

        }



    }
}
