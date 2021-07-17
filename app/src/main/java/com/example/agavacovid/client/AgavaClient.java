package com.example.agavacovid.client;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.example.agavacovid.security.Encriptado;
import com.example.agavacovid.MainActivity;
import com.example.agavacovid.persistence.AgavaContract;
import com.example.agavacovid.persistence.DbHelper;

import java.io.DataOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.example.agavacovid.sockets.AgavaSocket;


/**
 * @author Juan Velazquez Garcia
 * @author Maria Ruiz Molina
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

        //Flujo de datos hacia el servidor
        salidaServidor = new DataOutputStream(cs.getOutputStream());

        try {

            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String[] projection = {
                    BaseColumns._ID,
                    AgavaContract.IdsPropios.ID_EF,
                    AgavaContract.IdsPropios.CLAVE_GEN,
                    AgavaContract.IdsPropios.FECHA_GEN,
            };


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

    }
}
