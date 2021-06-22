package com.example.agavacovid;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.widget.Toast;

import com.example.agavacovid.persistence.AgavaContract;
import com.example.agavacovid.persistence.DbHelper;

import java.io.DataOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


/**
 *
 * @author Juan
 */
public class AgavaClient extends AgavaSocket {
    private long code;
    private String fecha;
    private Context context;

    public AgavaClient() throws IOException{super("cliente");} //Se usa el constructor para cliente de Conexion
    public AgavaClient(long code, String fecha, Context context) throws IOException{
        super("cliente");
        this.code = code;
        this.fecha = fecha;
        this.context = context;
    } //Se usa el constructor para cliente de Conexion

    public void startClient() throws IOException{ //Método para iniciar el cliente
        //try{

        //Flujo de datos hacia el servidor
        salidaServidor = new DataOutputStream(cs.getOutputStream());

        //Se enviarán dos mensajes
            /*for (int i = 0; i < 2; i++)
            {

                salidaServidor.writeUTF("Este es el mensaje número " + (i+1) + "\n");
            }*/



        //cs.close();//Fin de la conexión

        //}
        //catch (Exception e){

        //  System.out.println(e.getMessage());
        //}

        try {
            // ACCESO A BASE DE DATOS Y PASAMOS UNICAMNETE LOS DATOS. LA CONSULTA SE CREA AL RECIBIR
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String[] projection = {
                    BaseColumns._ID,
                    AgavaContract.IdsPropios.ID_EF,
                    AgavaContract.IdsPropios.CLAVE_GEN,
                    AgavaContract.IdsPropios.FECHA_GEN,
            };

            // Filter results WHERE "title" = 'My Title'

            Cursor cursor = db.query(
                    AgavaContract.IDS_PROPIOS_TABLA,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    null,              // The columns for the WHERE clause
                    null,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null               // The sort order
            );

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(code).append(",").append(fecha).append(",");

            while(cursor.moveToNext()) {
                String clavegen = cursor.getString(
                        cursor.getColumnIndexOrThrow(AgavaContract.IdsPropios.CLAVE_GEN));
                String fechagen = cursor.getString(
                        cursor.getColumnIndexOrThrow(AgavaContract.IdsPropios.FECHA_GEN));
                stringBuilder.append(clavegen).append(",").append(fechagen).append(",");

            }
            cursor.close();
            String mensaje = stringBuilder.toString();
          //  Toast.makeText(context,
            //        "Cacnea salvanos", Toast.LENGTH_SHORT).show();
        //    Toast.makeText(context,
          //          "WUUU" + Encriptado.encriptar(mensaje, "cacnea"), Toast.LENGTH_SHORT).show();
            //Toast.makeText(context, mensaje, Toast.LENGTH_LONG).show();
            salidaServidor.writeUTF(Encriptado.encriptar(mensaje, "cacnea"));


            MainActivity.setEstado(2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        //cs.close(); //SI FALLA LO DEL GRUPO
    }
}
