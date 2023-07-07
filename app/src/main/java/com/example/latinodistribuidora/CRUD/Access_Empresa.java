package com.example.latinodistribuidora.CRUD;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.latinodistribuidora.Conexion.DatabaseOpenHelper;

public class Access_Empresa {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static Access_Empresa instance;
    Cursor c = null;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    public Access_Empresa(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static Access_Empresa getInstance(Context context) {
        if (instance == null) {
            instance = new Access_Empresa(context);
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


    public Cursor getEmpresas(){
        this.openWritable();
        Cursor registros = db.rawQuery("SELECT * FROM v_empresa WHERE estado='S'", null);
        return registros;
    }

    public long insertarEmpresa(String razon_social, String ruc, String direccion, String telefono, int idciudad){
        ContentValues values = new ContentValues();
        values.put("razon_social", razon_social);
        values.put("ruc", ruc);
        values.put("direccion", direccion);
        values.put("telefono", telefono);
        values.put("estado", "S");
        values.put("ciudad_idciudad", idciudad);
        long insertado = db.insert("empresa",null,values);

        return insertado;
    }

    public long insertarEmpresaServer(int idempresa,String razon_social, String ruc, String direccion, String telefono, String estado, int idciudad){
        ContentValues values = new ContentValues();
        values.put("idempresa", idempresa);
        values.put("razon_social", razon_social);
        values.put("ruc", ruc);
        values.put("direccion", direccion);
        values.put("telefono", telefono);
        values.put("estado", estado);
        values.put("ciudad_idciudad", idciudad);
        long insertado = db.insert("empresa",null,values);
        return insertado;
    }


    public Cursor getEmpresa_a_Modificar(int empresaEditar) {
        this.openReadable();
        Cursor registro = db.rawQuery("SELECT * FROM v_empresa WHERE idempresa="+empresaEditar, null);
        return  registro;
    }

    public long ActualizarEmpresa(ContentValues values, int empresaEditar) {
        this.openWritable();
        long accion = db.update("empresa",values, "idempresa="+empresaEditar,null);
        return accion;
    }
    public long EliminarEmpresa(int ID) {
        ContentValues values = new ContentValues();
        values.put("estado", "N");
        this.openWritable();
        long accion = db.update("empresa",values, "idempresa="+ID,null);
        return accion;
    }
    public void borrarEmpresa() {
        this.openWritable();
        db.execSQL("DELETE FROM empresa");
        this.close();
    }
}

