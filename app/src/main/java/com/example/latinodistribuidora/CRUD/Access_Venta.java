package com.example.latinodistribuidora.CRUD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.latinodistribuidora.Conexion.DatabaseOpenHelper;

public class Access_Venta {
    private final SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static Access_Venta instance;
    Cursor registros = null;
    Cursor Filtrar=null;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     */
    public Access_Venta(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static Access_Venta getInstance(Context context) {
        if (instance == null) {
            instance = new Access_Venta(context);
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



    public Cursor getv_venta(String fechaactual){
        this.openReadable();
        registros = db.rawQuery("Select * from v_ventas where fecha like '%"+fechaactual+"%'", null);
        return registros;
    }
    public Cursor getv_ventaServer(){
        this.openReadable();
        registros = db.rawQuery("Select * from v_ventas", null);
        return registros;
    }

    public Cursor getv_ventadetalle(String fechaactual){
        this.openReadable();
        registros = db.rawQuery("Select * from v_vd where fecha like '%"+fechaactual+"%'", null);
        return registros;
    }

    public Cursor getReimpresion(int id){
        this.openReadable();
        registros = db.rawQuery("Select * from v_vd where id="+id, null);
        return registros;
    }


    public Cursor getTotal(String fechaactual){
        this.openReadable();
        registros = db.rawQuery("SELECT sum(totalfinal) FROM v_ventas where fecha like'%"+fechaactual+"%' and estado='S'", null);
        return registros;
    }

    public Cursor getFiltrarv_venta(String fechaactual,String texto){
        this.openReadable();
        Filtrar = db.rawQuery("select * from v_ventas where fecha like '%"+fechaactual+"%' and " +
                "factura like '%"+texto+"%' or razon_social like '%"+texto+"%' or ruc like'%"+texto+"%' order by id asc", null);
        return  Filtrar;
    }

    public Cursor getVenta(){
        this.openWritable();
        registros = db.rawQuery("Select * from ventas where estado='S'", null);
        return registros;
    }

    public Cursor getVentaSync(){
        this.openWritable();
        registros = db.rawQuery("Select * from ventas", null);
        return registros;
    }

    public Cursor getDetalleSync(){
        this.openWritable();
        registros = db.rawQuery("Select * from detalleventa", null);
        return registros;
    }

    public Cursor getCodigo(int idemision) {
        this.openWritable();
        registros = db.rawQuery("select nventa from ref where idemision="+idemision,null);
        return registros;
    }

    /*public long insertarventa(int idventas,String nrofactura, String condicion, String fecha,String hora, int total, int exenta, int iva5, int iva10, int idcliente,
                              int idusuario, int idtimbrado, int idemision){
        ContentValues values = new ContentValues();
        values.put("idventas", idventas);
        values.put("nrofactura", nrofactura);
        values.put("condicion", condicion);
        values.put("fecha", fecha);
        values.put("hora", hora);
        values.put("total", total);
        values.put("exenta", exenta);
        values.put("iva5", iva5);
        values.put("iva10", iva10);
        values.put("estado", "S");
        values.put("cliente_idcliente", idcliente);
        values.put("usuario_idusuario", idusuario);
        values.put("idtimbrado", idtimbrado);
        values.put("idemision", idemision);

        return db.insert("ventas",null,values);
    }*/

    public long insertarventa1(int idventas, int idemision,int idusuario,String ruc, String descripcion, String nrofactura,
                               String condicion, String fecha,String hora, int total, int exenta,
                               int iva5, int iva10){
        ContentValues values = new ContentValues();
        values.put("idventas", idventas);
        values.put("idemision", idemision);
        values.put("idusuario", idusuario);
        values.put("ruc", ruc);
        values.put("descripcion", descripcion);
        values.put("nrofactura", nrofactura);
        values.put("condicion", condicion);
        values.put("fecha", fecha);
        values.put("hora", hora);
        values.put("total", total);
        values.put("exenta", exenta);
        values.put("iva5", iva5);
        values.put("iva10", iva10);
        values.put("estado", "S");
        return db.insert("ventas_1",null,values);
    }

    public long insertarDetalle(int idventa,int idemision, int idproducto, String cantidad, int precio,int total, int impuesto, String um){
        ContentValues values = new ContentValues();
        values.put("venta_idventa", idventa);
        values.put("idemision", idemision);
        values.put("productos_idproductos", idproducto);
        values.put("cantidad", cantidad);
        values.put("precio", precio);
        values.put("total", total);
        values.put("impuesto_aplicado", impuesto);
        values.put("um", um);
        return db.insert("detalleventa",null,values);
    }

    public long insertarDetalle1(int idventa,int idemision, int idproducto, String cantidad, int precio,int total, int impuesto, String um){
        ContentValues values = new ContentValues();
        values.put("venta_1_idventa_1", idventa);
        values.put("idemision", idemision);
        values.put("productos_idproductos", idproducto);
        values.put("cantidad", cantidad);
        values.put("precio", precio);
        values.put("total", total);
        values.put("impuesto_aplicado", impuesto);
        values.put("um", um);
        return db.insert("detalleventa1",null,values);
    }

    public void ActualizarOP(int idemision, int OP) {
        this.openWritable();
        db.execSQL("UPDATE ref SET nventa="+OP+" WHERE idemision="+idemision);
    }
    public void ActualizarFacturaActual(int OP, int ID) {
        this.openWritable();
        db.execSQL("UPDATE puntoemision SET facturaactual="+OP+" WHERE idemision="+ID);
    }

    public long EliminarVenta(int ID) {
        ContentValues values = new ContentValues();
        values.put("estado", "N");
        this.openWritable();
        return db.update("ventas",values, "idventas="+ID,null);
    }

    public void borrarVentas() {
        this.openWritable();
        db.execSQL("DELETE FROM ventas");
    }
    public void borrarDetallesVenta() {
        this.openWritable();
        db.execSQL("DELETE FROM detalleventa");
    }

}
