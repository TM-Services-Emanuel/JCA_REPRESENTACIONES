package com.example.latinodistribuidora.CRUD;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.latinodistribuidora.Conexion.DatabaseOpenHelper;

public class Access_Departamentos {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static Access_Departamentos instance;
    Cursor c = null;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    public Access_Departamentos(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static Access_Departamentos getInstance(Context context) {
        if (instance == null) {
            instance = new Access_Departamentos(context);
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


    public Cursor getDepartamentos(){
        this.openWritable();
        Cursor registros = db.rawQuery("Select * from departamento where estado='S'", null);
        return registros;
    }

    public long insertarDepartamento(String departamento){
        ContentValues values = new ContentValues();
        values.put("departamento", departamento);
        values.put("estado", "S");
        long insertado = db.insert("departamento",null,values);

        return insertado;
    }
    public long insertarDepartamentoServer(int iddepartamento,String departamento, String estado){
        ContentValues values = new ContentValues();
        values.put("iddepartamento", iddepartamento);
        values.put("departamento", departamento);
        values.put("estado", estado);
        long insertado = db.insert("departamento",null,values);

        return insertado;
    }

    public Cursor getDepartamento_a_Modificar(int departamentoEditar) {
        this.openReadable();
        Cursor registro = db.rawQuery("Select * from departamento where iddepartamento="+departamentoEditar, null);
        return  registro;
    }

    public long ActualizarDepartamento(ContentValues values, int departamentoEditar) {
        this.openWritable();
        long accion = db.update("departamento",values, "iddepartamento="+departamentoEditar,null);
        return accion;
    }
    public long EliminarDepartamento(int ID) {
        ContentValues values = new ContentValues();
        values.put("estado", "N");
        this.openWritable();
        long accion = db.update("departamento",values, "iddepartamento="+ID,null);
        return accion;
    }

    public void borrarDepartamentos() {
        this.openWritable();
        db.execSQL("DELETE FROM departamento");
        this.close();
    }
}

