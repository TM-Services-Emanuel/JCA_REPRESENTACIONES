package com.example.latinodistribuidora.CRUD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.latinodistribuidora.Conexion.DatabaseOpenHelper;

public class Access_Productos {
    private final SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static Access_Productos instance;
    Cursor registros = null;
    Cursor Filtrar=null;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     */
    public Access_Productos(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static Access_Productos getInstance(Context context) {
        if (instance == null) {
            instance = new Access_Productos(context);
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

    public Cursor getProductos(){
        this.openWritable();
        registros = db.rawQuery("Select * from v_productos order by idproducto asc", null);
        return registros;
    }

    public Cursor getFiltrarProductos(String texto){
        this.openReadable();
        Filtrar = db.rawQuery("select * from v_productos where cod_interno like '%"+texto+"' or " +
                "cod_barra like '%"+texto+"' or descripcion like '%"+texto+"%'order by idproducto asc", null);
        return  Filtrar;
    }

    public long insertarProductos(String cod_interno, String cod_barra, String descripcion,int precio_costo, int precio_venta, int idunidad, int iddivision, int idiva){
        ContentValues values = new ContentValues();
        values.put("cod_interno", cod_interno);
        values.put("cod_barra", cod_barra);
        values.put("descripcion", descripcion);
        values.put("precio_costo", precio_costo);
        values.put("precio_venta", precio_venta);
        values.put("stock", 0);
        values.put("estado", "S");
        values.put("um_idunidad", idunidad);
        values.put("division_iddivision", iddivision);
        values.put("iva_idiva", idiva);

        return db.insert("productos",null,values);
    }
    public long InsertarProductosServer(int idproducto,String cod_interno, String cod_barra, String descripcion, int precio_costo, int precio_venta, int stock, String estado, int idunidad, int iddivision, int idiva){
        ContentValues values = new ContentValues();
        values.put("idproducto", idproducto);
        values.put("cod_interno", cod_interno);
        values.put("cod_barra", cod_barra);
        values.put("descripcion", descripcion);
        values.put("precio_costo", precio_costo);
        values.put("precio_venta", precio_venta);
        values.put("stock", stock);
        values.put("estado", estado);
        values.put("um_idunidad", idunidad);
        values.put("division_iddivision", iddivision);
        values.put("iva_idiva", idiva);

        return db.insert("productos",null,values);
    }

    public Cursor getProducto_a_modificar(int productoEditar) {
        this.openReadable();
        registros = db.rawQuery("Select * from v_productos where idproducto="+productoEditar, null);
        return  registros;
    }

    public long ActualizarProduto(ContentValues values, int productoEditar) {
        this.openWritable();
        return db.update("productos",values, "idproducto="+productoEditar,null);
    }
    public long EliminarProducto(int ID) {
        ContentValues values = new ContentValues();
        values.put("estado", "N");
        this.openWritable();
        return db.update("productos",values, "idproducto ="+ID,null);
    }

    public void borrarProductos() {
        this.openWritable();
        db.execSQL("DELETE FROM productos");
        this.close();
    }

}
