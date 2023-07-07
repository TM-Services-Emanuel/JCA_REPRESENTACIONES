package com.example.latinodistribuidora.CRUD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.latinodistribuidora.Conexion.DatabaseOpenHelper;

public class Access_Servidor {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static Access_Servidor instance;
    Cursor c = null;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    public Access_Servidor(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static Access_Servidor getInstance(Context context) {
        if (instance == null) {
            instance = new Access_Servidor(context);
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

    //CRUD TABLA SERVIDOR*********************************************************************************************


    public Cursor getServidor(){
        //ArrayList<Usuarios> lista = new ArrayList<>();
        this.openWritable();
        Cursor registros = db.rawQuery("Select * from servidor where id=1", null);
        return registros;
    }
    public long ActualizarServidor(ContentValues values) {
        this.openWritable();
        long accion = db.update("servidor",values, "id=1",null);
        return accion;
    }

}
