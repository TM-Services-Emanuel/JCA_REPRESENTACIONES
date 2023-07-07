package com.example.latinodistribuidora.CRUD;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.latinodistribuidora.Conexion.DatabaseOpenHelper;

public class Access_PE {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static Access_PE instance;
    Cursor c = null;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    public Access_PE(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static Access_PE getInstance(Context context) {
        if (instance == null) {
            instance = new Access_PE(context);
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


    public Cursor getPE(){
        this.openWritable();
        Cursor registros = db.rawQuery("Select idemision, establecimiento,puntoemision,direccion,facturainicio,facturafin,facturaactual,estado from puntoemision order by idemision asc", null);
        return registros;
    }
    public Cursor getPEActivo(){
        this.openWritable();
        Cursor registros = db.rawQuery("Select idemision, establecimiento,puntoemision,direccion,facturainicio,facturafin,facturaactual,estado from puntoemision where estado='Activo'", null);
        return registros;
    }

    public Cursor getPEServer(){
        this.openWritable();
        Cursor registros = db.rawQuery("Select * from v_puntoemision where estado='Activo'", null);
        return registros;
    }


    public long insertarPE(String establecimiento, String pe, String direccion, int desde, int hasta){
        ContentValues values = new ContentValues();
        values.put("establecimiento", establecimiento);
        values.put("puntoemision", pe);
        values.put("direccion", direccion);
        values.put("facturainicio", desde);
        values.put("facturafin", hasta);
        values.put("facturaactual", (desde-1));
        values.put("estado", "Activo");
        long insertado = db.insert("puntoemision",null,values);

        return insertado;
    }

    public long InsertarPESErver(int idemision,int idtimbrado, String establecimiento, String pe, String direccion, int desde, int hasta, int actual, String estado){
        ContentValues values = new ContentValues();
        values.put("idemision", idemision);
        values.put("idtimbrado", idtimbrado);
        values.put("establecimiento", establecimiento);
        values.put("puntoemision", pe);
        values.put("direccion", direccion);
        values.put("facturainicio", desde);
        values.put("facturafin", hasta);
        values.put("facturaactual", actual);
        values.put("estado", estado);
        //long insertado = db.insert("puntoemision",null,values);
        long insertado = db.insert("puntoemision", null, values);

        return insertado;
    }

    public long ActualizarPESErver(int idemision, int idtimbrado, String establecimiento, String pe, String direccion, int desde, int hasta, int actual){
        ContentValues values = new ContentValues();
        values.put("idtimbrado", idtimbrado);
        values.put("establecimiento", establecimiento);
        values.put("puntoemision", pe);
        values.put("direccion", direccion);
        values.put("facturainicio", desde);
        values.put("facturafin", hasta);
        values.put("facturaactual", actual);
        //values.put("estado", "Activo");
        //long insertado = db.insert("puntoemision",null,values);
        long insertado = db.update("puntoemision",values,"idemision="+idemision, null);

        return insertado;
    }

    public long InsertRef(int idemision, int nventa) {
        ContentValues values = new ContentValues();
        values.put("idemision", idemision);
        values.put("nventa", nventa);
        //long accion = db.update("ref",values, "idemision="+idemision,null);
        long accion = db.insert("ref",null,values);
        return accion;
    }
    public long ActualizarRef(int idemision, int nventa) {
        ContentValues values = new ContentValues();
        //values.put("idemision", nventa);
        values.put("nventa", nventa);
        long accion = db.update("ref",values, "idemision="+idemision,null);
        //long accion = db.insert("ref",null,values);
        return accion;
    }

    public Cursor getPE_a_Modificar(int peEditar) {
        this.openReadable();
        Cursor registro = db.rawQuery("Select * from puntoemision where idemision="+peEditar, null);
        return  registro;
    }

    public long ActualizarPE(ContentValues values, int peEditar) {
        this.openWritable();
        long accion = db.update("puntoemision",values, "idemision="+peEditar,null);
        return accion;
    }
    public long CerrarPE(int ID) {
        ContentValues values = new ContentValues();
        values.put("estado", "Inactivo");
        this.openWritable();
        long accion = db.update("puntoemision",values, "idemision="+ID,null);
        return accion;
    }

    public void borrarPuntoEmision(int id) {
        this.openWritable();
        db.execSQL("DELETE FROM puntoemision WHERE idemision="+id);
        this.close();
    }

    public void borrarPuntoEmisionGeneral() {
        this.openWritable();
        db.execSQL("DELETE FROM puntoemision");
        this.close();
    }

    public void borrarRef() {
        this.openWritable();
        db.execSQL("DELETE FROM ref");
        this.close();
    }
}

