package com.example.agavacovid.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = DbHelper.class.getSimpleName();

    // Constructor
    public DbHelper(Context context) {
        super(context, AgavaContract.DB_NAME, null, AgavaContract.DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db){
        db.setForeignKeyConstraintsEnabled(true);
    }

    // Llamado para crear la tabla
    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(TAG, "onCreate con SQL.");

        String sql = String.format("CREATE TABLE %s" +   //LOS MIOS
                        " (" +
                        "  %s INTEGER PRIMARY KEY AUTOINCREMENT" + //El ID de la tabla
                        ", %s TEXT" +  // clave key
                        ", %s TEXT" +   // keydate
                        //ID lo metemos?
                        " )",
                AgavaContract.MY_EPHID_TABLE,
                MyEphidProvider.Column.ID,
                MyEphidProvider.Column.KEY,
                MyEphidProvider.Column.KEY_DATE);
        db.execSQL(sql);

        sql = String.format("CREATE TABLE %s" +    //LOS DE OTROS
                        " (" +
                        "  %s INTEGER PRIMARY KEY AUTOINCREMENT" + //El ID de la tabla
                        ", %s TEXT" + //id efimero recibido
                        ", %s TEXT" + //fecha de recepcion del id externo
                        " )",
                AgavaContract.EXTERNAL_EPHID_TABLE,
                ExternalEphidProvider.Column.ID,
                ExternalEphidProvider.Column.EPHID,
                ExternalEphidProvider.Column.RECEIVED_TIME);
        db.execSQL(sql);
    }

    // Llamado siempre que tengamos una nueva version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Borramos la vieja base de datos
        String dropTable = "DROP TABLE IF EXISTS ";
        db.execSQL(dropTable + AgavaContract.MY_EPHID_TABLE);
        db.execSQL(dropTable + AgavaContract.EXTERNAL_EPHID_TABLE);
        // Creamos una base de datos nueva
        onCreate(db);
        Log.d(TAG, "onUpgrade");
    }
}