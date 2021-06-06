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
        String dropTable = "DROP TABLE IF EXISTS ";
        db.execSQL(dropTable + AgavaContract.IDS_PROPIOS_TABLA);
        Log.d(TAG, "onCreate con SQL.");

        String sql = String.format("CREATE TABLE %s" +   //LOS MIOS
                        " (" +
                        "  %s INTEGER PRIMARY KEY AUTOINCREMENT" + //El ID de la tabla
                        ", %s TEXT" +  // Id efimero
                        ", %s TEXT" +  // clave key
                        ", %s TEXT" +   // keydate
                        //ID lo metemos?
                        " )",
                AgavaContract.IDS_PROPIOS_TABLA,
                AgavaContract.IdsPropios._ID,
                AgavaContract.IdsPropios.ID_EF,
                AgavaContract.IdsPropios.CLAVE_GEN,
                AgavaContract.IdsPropios.FECHA_GEN);
        db.execSQL(sql);

        sql = String.format("CREATE TABLE %s" +    //LOS DE OTROS
                        " (" +
                        "  %s INTEGER PRIMARY KEY AUTOINCREMENT" + //El ID de la tabla
                        ", %s TEXT" + //id efimero recibido
                        ", %s TEXT" + //fecha de recepcion del id externo
                        " )",
                AgavaContract.IDS_AJENOS_TABLA,
                AgavaContract.IdsAjenos._ID,
                AgavaContract.IdsAjenos.ID_EF,
                AgavaContract.IdsAjenos.FECHA_REC);
        db.execSQL(sql);
    }

    // Llamado siempre que tengamos una nueva version
    //Hecho solo para pruebas. De otro modo, se borrarian los IDs entre cada actualizacion
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Borramos la base de datos antigua
        String dropTable = "DROP TABLE IF EXISTS ";
        db.execSQL(dropTable + AgavaContract.IDS_PROPIOS_TABLA);
        db.execSQL(dropTable + AgavaContract.IDS_AJENOS_TABLA);
        // Creamos una base de datos nueva
        onCreate(db);
        Log.d(TAG, "onUpgrade");
    }
}