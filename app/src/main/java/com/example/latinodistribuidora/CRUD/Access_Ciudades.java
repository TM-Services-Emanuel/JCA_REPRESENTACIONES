package com.example.latinodistribuidora.CRUD;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.latinodistribuidora.Conexion.DatabaseOpenHelper;

public class Access_Ciudades {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static Access_Ciudades instance;
    Cursor c = null;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    public Access_Ciudades(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static Access_Ciudades getInstance(Context context) {
        if (instance == null) {
            instance = new Access_Ciudades(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void openWritable() {
        this.db = openHelper.getWritableDatabase();
    }

    public void openReadable() {
        this.db = openHelper.getReadableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (db != null) {
            this.db.close();
        }
    }

    //CRUD TABLA CIUDADES*********************************************************************************************


    public Cursor getCiudades(){
        this.openWritable();
        Cursor registros = db.rawQuery("SELECT * FROM v_ciudades WHERE estado='S'", null);
        return registros;
    }

    public long insertarCiudad(String ciudad, int iddepartamento){
        ContentValues values = new ContentValues();
        values.put("ciudad", ciudad);
        values.put("estado", "S");
        values.put("departamento_iddepartamento", iddepartamento);
        long insertado = db.insert("ciudad",null,values);

        return insertado;
    }

    public long insertarCiudadServer(int idciudad, String ciudad,String estado, int iddepartamento){
        ContentValues values = new ContentValues();
        values.put("idciudad", idciudad);
        values.put("ciudad", ciudad);
        values.put("estado", estado);
        values.put("departamento_iddepartamento", iddepartamento);
        long insertado = db.insert("ciudad",null,values);

        return insertado;
    }

    public Cursor getCiudad_a_Modificar(int ciudadEditar) {
        this.openReadable();
        Cursor registro = db.rawQuery("SELECT * FROM v_ciudades WHERE idciudad="+ciudadEditar, null);
        return  registro;
    }

    public long ActualizarCiudad(ContentValues values, int ciudadEditar) {
        this.openWritable();
        long accion = db.update("ciudad",values, "idciudad="+ciudadEditar,null);
        return accion;
    }
    public long EliminarCiudad(int ID) {
        ContentValues values = new ContentValues();
        values.put("estado", "N");
        this.openWritable();
        long accion = db.update("ciudad",values, "idciudad="+ID,null);
        return accion;
    }

    public void borrarCiudades() {
        this.openWritable();
        db.execSQL("DELETE FROM ciudad");
        this.close();
    }
}

