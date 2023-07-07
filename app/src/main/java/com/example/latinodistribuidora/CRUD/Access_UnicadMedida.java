package com.example.latinodistribuidora.CRUD;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.latinodistribuidora.Conexion.DatabaseOpenHelper;

public class Access_UnicadMedida {
    private final SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static Access_UnicadMedida instance;
    Cursor c = null;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    public Access_UnicadMedida(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static Access_UnicadMedida getInstance(Context context) {
        if (instance == null) {
            instance = new Access_UnicadMedida(context);
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


    public Cursor getUnidadMedida(){
        this.openWritable();
        Cursor registros = db.rawQuery("Select * from unidad_medida where estado='S'", null);
        return registros;
    }

    public long insertarUnidadMedida(String um, int cant){
        ContentValues values = new ContentValues();
        values.put("unidadmedida", um);
        values.put("cantidad", cant);
        values.put("estado", "S");
        long insertado = db.insert("unidad_medida",null,values);

        return insertado;
    }

    public long insertarUnidadMedidaServer(int idunidad,String um, int cant,String estado){
        ContentValues values = new ContentValues();
        values.put("idunidad", idunidad);
        values.put("unidadmedida", um);
        values.put("cantidad", cant);
        values.put("estado", estado);
        long insertado = db.insert("unidad_medida",null,values);

        return insertado;
    }

    public Cursor getUnidadMedida_a_Modificar(int umEditar) {
        this.openReadable();
        Cursor registro = db.rawQuery("Select * from unidad_medida where idunidad="+umEditar, null);
        return  registro;
    }

    public long ActualizarUnidadMedida(ContentValues values, int umEditar) {
        this.openWritable();
        long accion = db.update("unidad_medida",values, "idunidad="+umEditar,null);
        return accion;
    }
    public long EliminarUnidadMedida(int ID) {
        ContentValues values = new ContentValues();
        values.put("estado", "N");
        this.openWritable();
        long accion = db.update("unidad_medida",values, "idunidad="+ID,null);
        return accion;
    }

    public void borrarUM() {
        this.openWritable();
        db.execSQL("DELETE FROM unidad_medida");
        this.close();
    }
}

