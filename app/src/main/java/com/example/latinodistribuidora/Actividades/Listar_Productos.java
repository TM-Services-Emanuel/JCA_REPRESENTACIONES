package com.example.latinodistribuidora.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.latinodistribuidora.Adaptador_Productos;
import com.example.latinodistribuidora.CRUD.Access_Productos;
import com.example.latinodistribuidora.CRUD.Access_Servidor;
import com.example.latinodistribuidora.Conexion.MySingleton;
import com.example.latinodistribuidora.Modelos.Productos;
import com.example.latinodistribuidora.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

import static android.content.ContentValues.TAG;

public class Listar_Productos extends AppCompatActivity {
    private ListView lv;
    private final ArrayList<Productos> lista = new ArrayList<>();
    private Adaptador_Productos adaptadorProductos;
    private int productoseleccionado = -1;
    public Object mActionMode;
    private TextView pie;
    private long insertadoProducto=0;
    private String IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_productos);

        pie = findViewById(R.id.id_productos_pie);
        EditText buscar = findViewById(R.id.id_buscarproductos);

        ObtnerIP();
        llenarLista();

        buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    String filtro= (s.toString());
                    if(!(filtro.length() == 0)) {
                        lista.removeAll(lista);
                        llenarListaFiltrada(filtro);
                    }
                }catch(Exception e){}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        findViewById(R.id.btnDOWP).setOnClickListener(v -> {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Listar_Productos.this);
                alertDialog.setMessage("¿Seguro que desea descargar la lista completa de productos habilitados para esta aplicación?");
                alertDialog.setTitle("ACTUALIZACIÓN DE DATOS");
                alertDialog.setIcon(R.drawable.ic_descargarpcdark);
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("SI, COMENZAR DESCARGA", (dialog, which) -> sincronizarProductos(Listar_Productos.this));
                alertDialog.setNegativeButton("CANCELAR", (dialog, which) -> dialog.cancel());
                alertDialog.show();
        });
    }

    public void onResume() {
        super.onResume();
        lista.removeAll(lista);
        llenarLista();
    }

    public void onClick() {
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setOnItemLongClickListener((parent, view, position, id) -> {
            productoseleccionado = position;
            mActionMode = Listar_Productos.this.startActionMode(amc);
            view.setSelected(true);
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opcion_download, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
            /* int id=menuItem.getItemId();
           if(id==R.id.item_download){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Listar_Productos.this);
                alertDialog.setMessage("¿Seguro que desea descargar la lista completa de productos habilitados para esta aplicación?");
                alertDialog.setTitle("ACTUALIZACIÓN DE DATOS");
                alertDialog.setIcon(R.drawable.download96);
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("SI, COMENZAR DESCARGA", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        sincronizarProductos(Listar_Productos.this);
                    }
                });
                alertDialog.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();

                return true;
            }*/

        return super.onOptionsItemSelected(menuItem);
    }

    private final ActionMode.Callback amc = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            getMenuInflater().inflate(R.menu.opciones_del_upd, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.item_eliminar) {
                AlertaEliminacion();
                mode.finish();
            } else if (item.getItemId() == R.id.item_modificar) {
                Productos productos = lista.get(productoseleccionado);
                Intent in = new Intent(getApplicationContext(), Editar_Producto.class);
                in.putExtra("idproducto", productos.getIdproducto());
                startActivity(in);
                mode.finish();
                finish();
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }


    };

    private void AlertaEliminacion(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("¿Desea eliminar el producto seleccionado?");
        alertDialog.setTitle("Eliminar");
        alertDialog.setIcon(android.R.drawable.ic_delete);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Sí", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                eliminarProducto();
            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void eliminarProducto(){
        try{
            Access_Productos db = Access_Productos.getInstance(getApplicationContext());
            Productos productos = lista.get(productoseleccionado);
            db.openWritable();
            long resultado = db.EliminarProducto(productos.getIdproducto());
            if(resultado > 0){
                Toast.makeText(getApplicationContext(),"Producto eliminado satisfactoriamente", Toast.LENGTH_LONG).show();
                lista.removeAll(lista);
                llenarLista();
            }else{
                Toast.makeText(getApplicationContext(),"Se produjo un error al eliminar producto", Toast.LENGTH_LONG).show();
            }
            db.close();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error Fatal: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void llenarLista(){
        try{
            lv = (ListView) findViewById(R.id.id_lista_productos);
            Access_Productos db = Access_Productos.getInstance(getApplicationContext());
            Cursor c = db.getProductos();
            if (c.moveToFirst()){
                do {
                    lista.add( new Productos (c.getInt(0),c.getString(1),c.getString(2),c.getString(3)
                            ,c.getString(4),c.getString(5),c.getString(9),
                            c.getString(10),c.getString(7),c.getInt(8)));
                }while (c.moveToNext());
            }
            adaptadorProductos = new Adaptador_Productos(this,lista);
            lv.setAdapter(adaptadorProductos);
            int cant = lv.getCount();
            if(cant==0){
                pie.setText("Lista vacía".toUpperCase());
            }else if(cant==1){
                pie.setText(String.valueOf(cant).concat(" producto listado".toUpperCase()));
            }else{
                pie.setText(String.valueOf(cant).concat(" productos listados".toUpperCase()));
            }
            db.close();

        }catch (Exception e){
            Toast.makeText(this, "Error cargando lista de productos: ".toUpperCase()+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void llenarListaFiltrada(String filtro){
        try{
            lv = (ListView) findViewById(R.id.id_lista_productos);
            Access_Productos db = Access_Productos.getInstance(getApplicationContext());
            Cursor c = db.getFiltrarProductos(filtro);
            if (c.moveToFirst()){
                do {
                    lista.add( new Productos (c.getInt(0),c.getString(1),c.getString(2),c.getString(3)
                            ,c.getString(4),c.getString(5),c.getString(9),
                            c.getString(10),c.getString(7),c.getInt(8)));
                }while (c.moveToNext());
            }
            adaptadorProductos = new Adaptador_Productos(this,lista);
            lv.setAdapter(adaptadorProductos);
            int cant = lv.getCount();
            if(cant==0){
                pie.setText("Lista vacía");
            }else if(cant==1){
                pie.setText(cant+" producto listado");
            }else{
                pie.setText(cant+" productos listados");
            }
            db.close();

        }catch (Exception e){
            Toast.makeText(this, "Error cargando lista: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void ir_a_RegistrarProducto(View view){
        Intent i = new Intent(getApplicationContext(), Registrar_Productos.class);
        startActivity(i);
        finish();
    }

    public static boolean checkNetworkConnection(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }

    private void sincronizarProductos(Context context)
    {
        if(checkNetworkConnection(context))
        {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://"+IP+context.getResources().getString(R.string.URL_UPDATE_PRODUCTOS), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Access_Productos db = Access_Productos.getInstance(context.getApplicationContext());
                    db.borrarProductos();
                    AlertDialog mDialog= new SpotsDialog.Builder()
                            .setContext(Listar_Productos.this)
                            .setMessage("Sincronizando Productos")
                            .setCancelable(false)
                            .build();
                    new Sincronizar(Listar_Productos.this,mDialog).execute();
                    try {
                        JSONArray array = new JSONArray(response);
                        for(int i = 0; i<array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            db.openWritable();
                            insertadoProducto = db.InsertarProductosServer(object.getInt("idproducto"), object.getString("cod_interno"), object.getString("cod_barra"),
                                    object.getString("descripcion"), object.getInt("precio_costo"), object.getInt("precio_venta"), object.getInt("stock"), object.getString("estado"),
                                    object.getInt("um_idunidad"), object.getInt("division_iddivision"), object.getInt("iva_idiva"));
                            Log.i("insertadoProducto", String.valueOf(insertadoProducto));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "Error Respuesta en JSON: " + error.getMessage());
                    String message = "";
                    if (error instanceof NetworkError) {
                        message = "¡Error de red!";
                    } else if (error instanceof ServerError) {
                        message = "No se pudo encontrar el servidor. ¡Inténtalo de nuevo después de un tiempo!";
                    } else if (error instanceof ParseError) {
                        message = "¡Error de sintáxis! ¡Inténtalo de nuevo después de un tiempo!";
                    } else if (error instanceof NoConnectionError) {
                        message = "No se puede conectar a Internet ... ¡Compruebe su conexión!";
                    } else if (error instanceof TimeoutError) {
                        message = "¡El tiempo de conexión expiro! Por favor revise su conexion a internet.";
                    }
                    //Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    Snackbar.make(findViewById(R.id.linearLayout51), message, Snackbar.LENGTH_SHORT)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .setBackgroundTint(Color.parseColor("#11232E"))
                            .setActionTextColor(Color.parseColor("#FFFFFF")).show();
                    Log.d(TAG, "jsArrayRequest Error : "+ message);
                }
            });
            MySingleton.getInstance(context).addToRequestQue(stringRequest);
        }
    }
    private class Sincronizar extends AsyncTask<Void,Void,Void>
    {
        Context context;
        AlertDialog alertDialog;
        private Sincronizar(Context context,AlertDialog alertDialog)
        {
            this.alertDialog = alertDialog;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            alertDialog.dismiss();
            if(insertadoProducto > 0){
                //Toast.makeText(context.getApplicationContext(), "Sincronizado Correctamente", Toast.LENGTH_LONG).show();
                Snackbar.make(findViewById(R.id.linearLayout51), "Sincronizado correctamente!.", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
                lista.removeAll(lista);
                llenarLista();
            }else{
                //Toast.makeText(context.getApplicationContext(),"Error insertado datos del servidor",Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(R.id.linearLayout51), "Error insertando datos del servidor.", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
            }
        }
    }

    public void ObtnerIP() {
        try {
            Access_Servidor db = Access_Servidor.getInstance(getApplicationContext());
            Cursor cursor = db.getServidor();
            if (cursor.moveToNext()) {
                db.close();
                IP = cursor.getString(1);
            } else {
                //Toast.makeText(this, "IP DEL SERVIDOR NO ENCONTRADO.", Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(R.id.linearLayout51), "IP DEL SERVIDOR NO ENCONTRADO.", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
            }
        } catch (Exception e) {
            //Toast.makeText(this, "TABLA DE SERVIDOR NO ENCONTRADO.", Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.linearLayout51), "TABLA DE SERVIDOR NO ENCONTRADO.", Snackbar.LENGTH_SHORT)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setBackgroundTint(Color.parseColor("#11232E"))
                    .setActionTextColor(Color.parseColor("#FFFFFF")).show();
        }
    }
}