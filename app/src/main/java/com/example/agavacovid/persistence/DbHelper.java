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

        String sql = String.format("CREATE TABLE %s" +
                        " (" +
                        "  %s INT PRIMARY KEY" +
                        ", %s TEXT" +
                        ", %s TEXT" +
                        ", %s TEXT" +  ///VAMOS POR AQUIIIIIIIIII AAAAAAAAAH
                        ", %s TEXT" +
                        " )",
                AgavaContract.MY_EPHID_TABLE,
                MyEphidProvider.Column.ID,
                MyEphidProvider.Column.KEY,
                MyEphidProvider.Column.KEY_DATE);
        db.execSQL(sql);

        sql = String.format("CREATE TABLE %s" +
                        " (" +
                        "  %s INTEGER PRIMARY KEY AUTOINCREMENT" +
                        ", %s TEXT" +
                        " )",
                AgavaContract.EXTERNAL_EPHID_TABLE,
                PlaylistProvider.Column.ID,
                PlaylistProvider.Column.NAME);
        db.execSQL(sql);

        sql = String.format("CREATE TABLE %s" +
                        " (" +
                        "  %s INTEGER PRIMARY KEY AUTOINCREMENT" +
                        ", %s INT" +
                        ", %s INT" +
                        ", UNIQUE (%s, %s)" +
                        ", CONSTRAINT fk_playlist" +
                        "    FOREIGN KEY (%s)" +
                        "    REFERENCES %s(%s)" +
                        "    ON DELETE CASCADE" +
                        " )",
                FullMusicContract.PLAYLIST_SONG_TABLE,
                PlaylistSongProvider.Column.ID,
                PlaylistSongProvider.Column.ID_PLAYLIST,
                PlaylistSongProvider.Column.ID_SONG,
                PlaylistSongProvider.Column.ID_PLAYLIST,
                PlaylistSongProvider.Column.ID_SONG,
                PlaylistSongProvider.Column.ID_PLAYLIST,
                FullMusicContract.PLAYLIST_TABLE,
                PlaylistProvider.Column.ID
        );
        db.execSQL(sql);
    }

    // Llamado siempre que tengamos una nueva version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Aqui irían las sentencias del tipo ALTER SONG_TABLE, de momento lo hacemos mas sencillo...
        // Borramos la vieja base de datos
        String dropTable = "DROP TABLE IF EXISTS ";
        db.execSQL(dropTable + FullMusicContract.PLAYLIST_SONG_TABLE);
        db.execSQL(dropTable + FullMusicContract.SONG_TABLE);
        db.execSQL(dropTable + FullMusicContract.PLAYLIST_TABLE);
        // Creamos una base de datos nueva
        onCreate(db);
        Log.d(TAG, "onUpgrade");
    }
}