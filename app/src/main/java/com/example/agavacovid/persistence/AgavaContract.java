package com.example.agavacovid.persistence;

import android.provider.BaseColumns;

public class AgavaContract {
    public static final String DB_NAME = "agavacovid.db";
    public static final int DB_VERSION = 1;
    public static final String IDS_PROPIOS_TABLA = "ids_propios";
    public static final String IDS_AJENOS_TABLA = "ids_ajenos";

    public static class IdsPropios implements BaseColumns {

        public static final String ID_EF = "identificador_ef";
        public static final String CLAVE_GEN = "clave_gen";
        public static final String FECHA_GEN = "fecha_gen";
    }

    public static class IdsAjenos implements BaseColumns {

        public static final String ID_EF = "identificador_ef";
        public static final String FECHA_REC = "fecha_rec";
    }
}
