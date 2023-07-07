package com.example.latinodistribuidora.CRUD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.latinodistribuidora.Conexion.DatabaseOpenHelper;

public class Access_Clientes {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static Access_Clientes instance;
    Cursor registros = null;
    Cursor Filtrar=null;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    public Access_Clientes(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static Access_Clientes getInstance(Context context) {
        if (instance == null) {
            instance = new Access_Clientes(context);
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

    //CRUD TABLA CLIENTES*********************************************************************************************


    public Cursor getClientes(){
        //ArrayList<Usuarios> lista = new ArrayList<>();
        this.openWritable();
        registros = db.rawQuery("Select * from v_clientes where estado='S'", null);
        return registros;
    }

    public Cursor getFiltrarClientes(String texto){
        this.openReadable();
        Filtrar = db.rawQuery("select * from v_clientes where razon_social LIKE '%"+texto+"%' OR ruc LIKE '%"+texto+"%' ", null);
        //Filtrar = db.rawQuery("select * from v_clientes where ruc='"+texto+"'", null);
        return  Filtrar;
    }

    public long insertarClientes(String razon_social, String ruc, String direccion, String celular, int idciudad){
        ContentValues values = new ContentValues();
        values.put("razon_social", razon_social);
        values.put("ruc", ruc);
        values.put("direccion", direccion);
        values.put("telefono", celular);
        values.put("estado", "S");
        values.put("ciudad_idciudad", idciudad);

        long insertado = db.insert("clientes",null,values);

        return insertado;
    }

    public long insertarClientesServer(int idcliente, String razon_social, String ruc, String direccion, String celular,String estado, int idciudad){
        ContentValues values = new ContentValues();
        values.put("idcliente", idcliente);
        values.put("razon_social", razon_social);
        values.put("ruc", ruc);
        values.put("direccion", direccion);
        values.put("telefono", celular);
        values.put("estado", estado);
        values.put("ciudad_idciudad", idciudad);

        long insertado = db.insert("clientes",null,values);

        return insertado;
    }

    public Cursor getCliente_a_modificar(int clienteEditar) {
        this.openReadable();
        registros = db.rawQuery("Select * from v_clientes where idcliente="+clienteEditar, null);
        return  registros;
    }

    public long ActualizarCliente(ContentValues values, int clienteEditar) {
        this.openWritable();
        long accion = db.update("clientes",values, "idcliente="+clienteEditar,null);
        return accion;
    }
    public long EliminarCliente(int ID) {
        ContentValues values = new ContentValues();
        values.put("estado", "N");
        this.openWritable();
        long accion = db.update("clientes",values, "idcliente="+ID,null);
        return accion;
    }

    public void borrarClientes() {
        this.openWritable();
        db.execSQL("DELETE FROM clientes");
        this.close();
    }

}
