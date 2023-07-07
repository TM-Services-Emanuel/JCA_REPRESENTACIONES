package com.example.latinodistribuidora.CRUD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.latinodistribuidora.Conexion.DatabaseOpenHelper;

public class Access_Usuarios {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static Access_Usuarios instance;
    Cursor c = null;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    public Access_Usuarios(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static Access_Usuarios getInstance(Context context) {
        if (instance == null) {
            instance = new Access_Usuarios(context);
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

    //CRUD TABLA USUARIO*********************************************************************************************


    public Cursor getUsuarios(){
        //ArrayList<Usuarios> lista = new ArrayList<>();
        this.openWritable();
        Cursor registros = db.rawQuery("Select * from usuario where estado='S'", null);
        return registros;
    }

    public long insertarUsuario(String nombre, String ci, String direccion, String celular, String usuario, String contrasena){
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("ci", ci);
        values.put("direccion", direccion);
        values.put("telefono", celular);
        values.put("usuario", usuario);
        values.put("contrasena", contrasena);
        values.put("estado", "S");
        long insertado = db.insert("usuario",null,values);

        return insertado;
    }

    public long insertarUsuarioServer(int idusuario,String nombre, int ci, String direccion, String celular, String usuario, String contrasena, String estado){
        ContentValues values = new ContentValues();
        values.put("idusuario", idusuario);
        values.put("nombre", nombre);
        values.put("ci", ci);
        values.put("direccion", direccion);
        values.put("telefono", celular);
        values.put("usuario", usuario);
        values.put("contrasena", contrasena);
        values.put("estado", estado);
        long insertado = db.insert("usuario",null,values);

        return insertado;
    }
    /*public long eliminarUsuario(int ID){
        this.openWritable();
        long u=db.delete("usuario", "idusuario="+ID,null);
        this.close();
        return u;
    }*/

    public Cursor getUsuario_a_modificar(int usuarioEditar) {
        this.openReadable();
        Cursor registro = db.rawQuery("Select * from usuario where idusuario="+usuarioEditar, null);
        return  registro;
    }

    public Cursor getUsuario_IniciarSesion(String usuario, String contrasena) {
        this.openReadable();
        Cursor respuesta = db.rawQuery("Select * from usuario where usuario='"+usuario+"' and contrasena='"+contrasena+"' and estado='S'", null);
        return  respuesta;
    }

    public long ActualizarUsuario(ContentValues values, int usuarioEditar) {
        this.openWritable();
        long accion = db.update("usuario",values, "idusuario="+usuarioEditar,null);
        return accion;
    }
    public long EliminarUsuario(int ID) {
        ContentValues values = new ContentValues();
        values.put("estado", "N");
        this.openWritable();
        long accion = db.update("usuario",values, "idusuario="+ID,null);
        return accion;
    }
    public void borrarUsuarios() {
        this.openWritable();
        db.execSQL("DELETE FROM usuario");
        this.close();
    }
}
