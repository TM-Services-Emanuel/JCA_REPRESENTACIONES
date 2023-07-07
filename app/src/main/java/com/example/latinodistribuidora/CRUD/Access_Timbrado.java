package com.example.latinodistribuidora.CRUD;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.latinodistribuidora.Conexion.DatabaseOpenHelper;

public class Access_Timbrado {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static Access_Timbrado instance;
    Cursor c = null;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    public Access_Timbrado(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static Access_Timbrado getInstance(Context context) {
        if (instance == null) {
            instance = new Access_Timbrado(context);
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

    //CRUD TABLA DEPARTAMENTOS*********************************************************************************************


    public Cursor getTimbrado(){
        this.openWritable();
        Cursor registros = db.rawQuery("Select * from timbrado order by idtimbrado asc", null);
        return registros;
    }
    public Cursor getTimbradoActivo(){
        this.openWritable();
        Cursor registros = db.rawQuery("Select * from timbrado where estado='Activo'", null);
        return registros;
    }

    public long insertarTimbrado(String timbrado, String desde, String hasta){
        ContentValues values = new ContentValues();
        values.put("descripcion", timbrado);
        values.put("fechadesde", desde);
        values.put("fechahasta", hasta);
        values.put("estado", "Activo");
        long insertado = db.insert("timbrado",null,values);

        return insertado;
    }

    public long insertarTimbradoServer(int idtimbrado, String timbrado, String desde, String hasta, String estado){
        ContentValues values = new ContentValues();
        values.put("idtimbrado", idtimbrado);
        values.put("descripcion", timbrado);
        values.put("fechadesde", desde);
        values.put("fechahasta", hasta);
        values.put("estado", estado);
        long insertado = db.insert("timbrado",null,values);
        return insertado;
    }

    public Cursor getTimbrado_a_Modificar(int timbradoEditar) {
        this.openReadable();
        Cursor registro = db.rawQuery("Select * from timbrado where idtimbrado="+timbradoEditar, null);
        return  registro;
    }

    public long ActualizarTimbrado(ContentValues values, int timbradoEditar) {
        this.openWritable();
        long accion = db.update("timbrado",values, "idtimbrado="+timbradoEditar,null);
        return accion;
    }
    public long CerrarTimbrado(int ID) {
        ContentValues values = new ContentValues();
        values.put("estado", "Inactivo");
        this.openWritable();
        long accion = db.update("timbrado",values, "idtimbrado="+ID,null);
        return accion;
    }

    public void borrarTimbrado() {
        this.openWritable();
        db.execSQL("DELETE FROM timbrado");
        this.close();
    }
}

