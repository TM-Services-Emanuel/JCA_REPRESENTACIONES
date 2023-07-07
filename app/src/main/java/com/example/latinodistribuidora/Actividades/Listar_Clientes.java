package com.example.latinodistribuidora.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.example.latinodistribuidora.Adaptador_Clientes;
import com.example.latinodistribuidora.CRUD.Access_Clientes;
import com.example.latinodistribuidora.CRUD.Access_Servidor;
import com.example.latinodistribuidora.Conexion.MySingleton;
import com.example.latinodistribuidora.MainActivity;
import com.example.latinodistribuidora.Modelos.Clientes;
import com.example.latinodistribuidora.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

import static android.content.ContentValues.TAG;

public class Listar_Clientes extends AppCompatActivity {
    private ListView lv;
    private ArrayList<Clientes> lista = new ArrayList<>();
    private Adaptador_Clientes adaptadorClientes;
    private int clienteseleccionado = -1;
    private Object mActionMode;
    private TextView pie;
    private EditText buscar;
    private long insertadoClientes=0;
    private Button Filtrar;
    public String IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_clientes);
        pie = findViewById(R.id.id_cli_nue_pie);
        buscar = findViewById(R.id.id_buscarcliente);
        Filtrar = (Button) findViewById(R.id.btn_FiltarClientes);

        llenarLista();
        //onClick();

        findViewById(R.id.btnADDC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    ir_a_RegistrarClientes();
            }
        });

        findViewById(R.id.btnDOWC).setOnClickListener(v -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Listar_Clientes.this);
            alertDialog.setMessage("¿Seguro que desea descargar la lista completa de clientes habilitados para esta aplicación?");
            alertDialog.setTitle("ACTUALIZACIÓN DE DATOS");
            alertDialog.setIcon(R.drawable.ic_descargarpcdark);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("SI, COMENZAR DESCARGA", (dialog, which) -> sincronizarClientes(Listar_Clientes.this));
            alertDialog.setNegativeButton("CANCELAR", (dialog, which) -> dialog.cancel());
            alertDialog.show();
        });

        Filtrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bajarTeclado();
                String filtro= String.valueOf(buscar.getText().toString());
                if(filtro.length()<=0){
                    lista.removeAll(lista);
                    llenarLista();
                }else{
                    Log.i("",filtro);
                    lista.removeAll(lista);
                    llenarListaFiltrada(filtro);
                }

            }
        });

        /*buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String filtro= String.valueOf(s.toString());
                    if(filtro.length()>=0){
                        Log.i("",filtro);
                        lista.removeAll(lista);
                        llenarListaFiltrada(filtro);
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/


    }

    public void onResume() {
        super.onResume();
        lista.removeAll(lista);
        llenarLista();
    }

    public void onClick() {
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                clienteseleccionado = position;
                mActionMode = Listar_Clientes.this.startActionMode(amc);
                view.setSelected(true);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opcion_download_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
     /*   int id=menuItem.getItemId();
        if(id==R.id.item_nuevo){
            ir_a_RegistrarClientes(null);
            return true;
        }
        if(id==R.id.item_download){
            sincronizarClientes(Listar_Clientes.this);
            return true;
        }*/
        return super.onOptionsItemSelected(menuItem);
    }

    private ActionMode.Callback amc = new ActionMode.Callback() {

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
                Clientes clientes = lista.get(clienteseleccionado);
                Intent in = new Intent(getApplicationContext(), Editar_Clientes.class);
                in.putExtra("idcliente", clientes.getIdcliente());
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
        alertDialog.setMessage("¿Desea eliminar el cliente seleccionado?");
        alertDialog.setTitle("Eliminar");
        alertDialog.setIcon(R.drawable.ic_eliminar173546);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Sí", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                eliminarCliente();
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

    public void eliminarCliente(){
        try{
            Access_Clientes db = Access_Clientes.getInstance(getApplicationContext());
            Clientes clientes = lista.get(clienteseleccionado);
            db.openWritable();
            long resultado = db.EliminarCliente(clientes.getIdcliente());
            if(resultado > 0){
                //Toast.makeText(getApplicationContext(),"Cliente eliminado satisfactoriamente", Toast.LENGTH_LONG).show();
                Snackbar.make(findViewById(R.id.linearLayout48), "Cliente eliminado satisfactoriamente!.", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
                lista.removeAll(lista);
                llenarLista();
            }else{
                //Toast.makeText(getApplicationContext(),"Se produjo un error al eliminar cliente", Toast.LENGTH_LONG).show();
                Snackbar.make(findViewById(R.id.linearLayout48), "Error eliminado cliente.", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
            }
            db.close();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error Fatal: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void llenarLista(){
        try{
            lv = (ListView) findViewById(R.id.id_lista_cliente);
            Access_Clientes db = Access_Clientes.getInstance(getApplicationContext());
            Cursor c = db.getClientes();
            if (c.moveToFirst()){
                do {
                    lista.add( new Clientes (c.getInt(0),c.getString(1),c.getString(2),c.getString(3)
                            ,c.getString(4),c.getInt(5),c.getString(6)));
                }while (c.moveToNext());
            }
            adaptadorClientes = new Adaptador_Clientes(this,lista);
            lv.setAdapter(adaptadorClientes);
            int cant = lv.getCount();
            if(cant==0){
                pie.setText("Lista vacía".toUpperCase());
            }else if(cant==1){
                pie.setText(cant+" cliente listado".toUpperCase());
            }else{
                pie.setText(cant+" clientes listados".toUpperCase());
            }
            db.close();

        }catch (Exception e){
            Toast.makeText(this, "Error cargando lista de clientes: ".toUpperCase()+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void llenarListaFiltrada(String filtro){
        try{
            lv = findViewById(R.id.id_lista_cliente);
            Access_Clientes db = Access_Clientes.getInstance(getApplicationContext());
            Cursor c = db.getFiltrarClientes(filtro);
            if (c.moveToFirst()){
                do {
                    lista.add( new Clientes (c.getInt(0),c.getString(1),c.getString(2),c.getString(3)
                            ,c.getString(4),c.getInt(5),c.getString(6)));
                }while (c.moveToNext());
            }
            adaptadorClientes = new Adaptador_Clientes(this,lista);
            lv.setAdapter(adaptadorClientes);
            int cant = lv.getCount();
            if(cant==0){
                pie.setText("Lista vacía");
            }else if(cant==1){
                pie.setText(cant+" cliente listado");
            }else{
                pie.setText(cant+" clientes listados");
            }
            db.close();

        }catch (Exception e){
            Toast.makeText(this, "Error cargando lista: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public void ir_a_RegistrarClientes(){
        Intent i = new Intent(getApplicationContext(), Registrar_Clientes.class);
        startActivity(i);
        finish();
    }

    public static boolean checkNetworkConnection(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }

    public void sincronizarClientes(Context context)
    {
        try{
        Access_Servidor db = Access_Servidor.getInstance(getApplicationContext());
        Cursor cursor= db.getServidor();
        if(cursor.moveToNext()){
            db.close();
            IP= cursor.getString(1);
        if(checkNetworkConnection(context))
        {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://"+IP+context.getResources().getString(R.string.URL_UPDATE_CLIENTES), response -> {
                Access_Clientes db1 = Access_Clientes.getInstance(context.getApplicationContext());
                db1.borrarClientes();
                AlertDialog mDialog= new SpotsDialog.Builder()
                        .setContext(Listar_Clientes.this)
                        .setMessage("Sincronizando Clientes")
                        .setCancelable(false)
                        .build();
                new Sincronizar(Listar_Clientes.this,mDialog).execute();
                try {
                    JSONArray array = new JSONArray(response);
                    for(int i = 0; i<array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        db1.openWritable();
                        insertadoClientes = db1.insertarClientesServer(object.getInt("idcliente"), object.getString("razon_social"), object.getString("ruc"),
                                object.getString("direccion"), object.getString("telefono"), object.getString("estado"), object.getInt("ciudad_idciudad"));
                        Log.i("insertadoClientes", String.valueOf(insertadoClientes));
                    }
                } catch (JSONException e) {
                    //e.printStackTrace();
                }
            }, error -> {
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
                Snackbar.make(findViewById(R.id.linearLayout48), message, Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
                Log.d(TAG, "jsArrayRequest Error : "+ message);
            });
            MySingleton.getInstance(context).addToRequestQue(stringRequest);
        }
        }else{
            //Toast.makeText(this, "IP DEL SERVIDOR NO ENCONTRADO.", Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.linearLayout48), "IP DEL SERVIDOR NO ENCONTRADO.", Snackbar.LENGTH_SHORT)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setBackgroundTint(Color.parseColor("#11232E"))
                    .setActionTextColor(Color.parseColor("#FFFFFF")).show();
        }
        }catch(Exception e){
            //Toast.makeText(this, "TABLA DE SERVIDOR NO ENCONTRADO.", Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.linearLayout48), "TABLA DE SERVIDOR NO ENCONTRADO.", Snackbar.LENGTH_SHORT)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setBackgroundTint(Color.parseColor("#11232E"))
                    .setActionTextColor(Color.parseColor("#FFFFFF")).show();
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
                Thread.sleep(30000);
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
            if(insertadoClientes > 0){
                //Toast.makeText(context.getApplicationContext(), "Sincronizado Correctamente", Toast.LENGTH_LONG).show();
                Snackbar.make(findViewById(R.id.linearLayout48), "Sincronizado correctamente!.", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
                lista.removeAll(lista);
                llenarLista();
            }else{
                //Toast.makeText(context.getApplicationContext(),"Error insertado datos del servidor",Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(R.id.linearLayout48), "Error insertando datos del servidor.", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
            }
        }
    }

    private void bajarTeclado() {
        View view = this.getCurrentFocus();
        if(view !=null){
            InputMethodManager imn = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imn.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }

}