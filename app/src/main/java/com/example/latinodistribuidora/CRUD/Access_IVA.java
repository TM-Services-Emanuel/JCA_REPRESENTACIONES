package com.example.latinodistribuidora.CRUD;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.latinodistribuidora.Conexion.DatabaseOpenHelper;

public class Access_IVA {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static Access_IVA instance;
    Cursor c = null;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    public Access_IVA(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static Access_IVA getInstance(Context context) {
        if (instance == null) {
            instance = new Access_IVA(context);
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


    public Cursor getIVA(){
        this.openWritable();
        Cursor registros = db.rawQuery("Select * from iva where estado='S'", null);
        return registros;
    }

    public long insertarIVA(int iva, String descripcion){
        ContentValues values = new ContentValues();
        values.put("impuesto", iva);
        values.put("descripcion", descripcion);
        values.put("estado", "S");
        long insertado = db.insert("iva",null,values);

        return insertado;
    }

    public long insertarIVAServer(int idiva,int iva, String descripcion,String estado){
        ContentValues values = new ContentValues();
        values.put("idiva", idiva);
        values.put("impuesto", iva);
        values.put("descripcion", descripcion);
        values.put("estado", estado);
        long insertado = db.insert("iva",null,values);

        return insertado;
    }

    public Cursor getIVA_a_Modificar(int ivaEditar) {
        this.openReadable();
        Cursor registro = db.rawQuery("Select * from iva where idiva="+ivaEditar, null);
        return  registro;
    }

    public long ActualizarIVA(ContentValues values, int ivaEditar) {
        this.openWritable();
        long accion = db.update("iva",values, "idiva="+ivaEditar,null);
        return accion;
    }
    public long EliminarIVA(int ID) {
        ContentValues values = new ContentValues();
        values.put("estado", "N");
        this.openWritable();
        long accion = db.update("iva",values, "idiva="+ID,null);
        return accion;
    }

    public void borrarIVA() {
        this.openWritable();
        db.execSQL("DELETE FROM iva");
        this.close();
    }
}

