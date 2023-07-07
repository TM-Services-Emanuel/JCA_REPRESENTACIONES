package com.example.latinodistribuidora.CRUD;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.latinodistribuidora.Conexion.DatabaseOpenHelper;

public class Access_Division {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static Access_Division instance;
    Cursor c = null;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    public Access_Division(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static Access_Division getInstance(Context context) {
        if (instance == null) {
            instance = new Access_Division(context);
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


    public Cursor getDivision(){
        this.openWritable();
        Cursor registros = db.rawQuery("Select * from division where estado='S'", null);
        return registros;
    }

    public long insertarDivision(String division){
        ContentValues values = new ContentValues();
        values.put("descripcion", division);
        values.put("estado", "S");
        long insertado = db.insert("division",null,values);

        return insertado;
    }

    public long insertarDivisionServer(int iddivision,String division,String estado){
        ContentValues values = new ContentValues();
        values.put("iddivision", iddivision);
        values.put("descripcion", division);
        values.put("estado", estado);
        long insertado = db.insert("division",null,values);

        return insertado;
    }

    public Cursor getDivision_a_Modificar(int divisionEditar) {
        this.openReadable();
        Cursor registro = db.rawQuery("Select * from division where iddivision="+divisionEditar, null);
        return  registro;
    }

    public long ActualizarDivision(ContentValues values, int divisionEditar) {
        this.openWritable();
        long accion = db.update("division",values, "iddivision="+divisionEditar,null);
        return accion;
    }
    public long EliminarDivision(int ID) {
        ContentValues values = new ContentValues();
        values.put("estado", "N");
        this.openWritable();
        long accion = db.update("division",values, "iddivision="+ID,null);
        return accion;
    }

    public void borrarDivision() {
        this.openWritable();
        db.execSQL("DELETE FROM division");
        this.close();
    }
}

