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
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.latinodistribuidora.CRUD.Access_Ciudades;
import com.example.latinodistribuidora.CRUD.Access_Departamentos;
import com.example.latinodistribuidora.CRUD.Access_Servidor;
import com.example.latinodistribuidora.Conexion.MySingleton;
import com.example.latinodistribuidora.Modelos.Ciudades;
import com.example.latinodistribuidora.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

import static android.content.ContentValues.TAG;

public class Listar_Ciudad extends AppCompatActivity {
    private ListView lv;
    private ArrayList<Ciudades> lista = new ArrayList<>();
    private ArrayAdapter<String> adaptador;
    private int ciudadseleccionado = -1;
    private Object mActionMode;
    private TextView pie;
    private long insertadoCiudad=0;
    private String IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_ciudad);
        pie = findViewById(R.id.id_ciu_pie);
        ObtnerIP();
        llenarLista();

        findViewById(R.id.btnDOWNCIU).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Listar_Ciudad.this);
                alertDialog.setMessage("¿Seguro que desea descargar la lista completa de ciudades habilitados para esta aplicación?");
                alertDialog.setTitle("ACTUALIZACIÓN DE DATOS");
                alertDialog.setIcon(R.drawable.ic_descargarpcdark);
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("SI, COMENZAR DESCARGA", (dialog, which) -> sincronizarCiudades(Listar_Ciudad.this));
                alertDialog.setNegativeButton("CANCELAR", (dialog, which) -> dialog.cancel());
                alertDialog.show();
            }
        });
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
                ciudadseleccionado = position;
                mActionMode = Listar_Ciudad.this.startActionMode(amc);
                view.setSelected(true);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opcion_download, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        /*int id=menuItem.getItemId();
        if(id==R.id.item_download){
            sincronizarCiudades(Listar_Ciudad.this);
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
                    Ciudades ciudades = lista.get(ciudadseleccionado);
                    Intent in = new Intent(getApplicationContext(), Editar_Ciudad.class);
                    in.putExtra("idciudad", ciudades.getIdciudad());
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
        alertDialog.setMessage("¿Desea eliminar la ciudad seleccionada?");
        alertDialog.setTitle("Eliminar");
        alertDialog.setIcon(android.R.drawable.ic_delete);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Sí", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                EliminarCiudad();
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

    public void EliminarCiudad(){
        try{
            Access_Ciudades db = Access_Ciudades.getInstance(getApplicationContext());
            Ciudades ciudades = lista.get(ciudadseleccionado);
            db.openWritable();
            long resultado = db.EliminarCiudad(ciudades.getIdciudad());
            if(resultado > 0){
                Toast.makeText(getApplicationContext(),"Ciudad eliminado satisfactoriamente", Toast.LENGTH_LONG).show();
                lista.removeAll(lista);
                llenarLista();
            }else{
                Toast.makeText(getApplicationContext(),"Se produjo un error al eliminar ciudad", Toast.LENGTH_LONG).show();
            }
            db.close();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error Fatal: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void llenarLista(){
        try{
            lv = (ListView) findViewById(R.id.id_lista_ciudades);
            Access_Ciudades db = Access_Ciudades.getInstance(getApplicationContext());
            Cursor c = db.getCiudades();
            if (c.moveToFirst()){
                do {
                    lista.add( new Ciudades (c.getInt(0),c.getString(1)
                            ,c.getString(2)));
                }while (c.moveToNext());
            }
            String[] arreglo = new String[lista.size()];
            for (int i = 0;i<arreglo.length;i++){
                arreglo[i] = lista.get(i).getCiudad()+" - Dpto.: "+lista.get(i).getDepartamento();
            }
            adaptador = new ArrayAdapter<String>(getApplicationContext(), R.layout.listview_item_ld,arreglo);
            lv.setAdapter(adaptador);
            int cant = lv.getCount();
            if(cant==0){
                pie.setText("Lista vacía".toUpperCase());
            }else if(cant==1){
                pie.setText(String.valueOf(cant).concat(" ciudad listada".toUpperCase()));
            }else{
                pie.setText(String.valueOf(cant).concat(" ciudades listadas".toUpperCase()));
            }
            db.close();

        }catch (Exception e){
            Toast.makeText(this, "Error cargando lista de ciudades: ".toUpperCase()+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void ir_a_RegistrarCiudad(View view){
        Intent i = new Intent(getApplicationContext(), Registrar_Ciudad.class);
        startActivity(i);
        finish();
    }

    private static boolean checkNetworkConnection(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }

    private void sincronizarCiudades(Context context) {
        if (checkNetworkConnection(context)) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://"+IP+context.getResources().getString(R.string.URL_UPDATE_CIUDADES), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Access_Ciudades db = Access_Ciudades.getInstance(context.getApplicationContext());
                    db.borrarCiudades();
                    AlertDialog mDialog= new SpotsDialog.Builder()
                            .setContext(Listar_Ciudad.this)
                            .setMessage("Sincronizando Ciudades")
                            .setCancelable(false)
                            .build();
                    new Sincronizar(Listar_Ciudad.this, mDialog).execute();
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);

                            db.openWritable();
                            insertadoCiudad= db.insertarCiudadServer(object.getInt("idciudad"), object.getString("ciudad")
                                    ,object.getString("estado"),object.getInt("departamento_iddepartamento"));
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
                    //Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    Snackbar.make(findViewById(R.id.linearLayout24), message, Snackbar.LENGTH_SHORT)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .setBackgroundTint(Color.parseColor("#11232E"))
                            .setActionTextColor(Color.parseColor("#FFFFFF")).show();
                    Log.d(TAG, "jsArrayRequest Error : " + message);
                }
            });
            MySingleton.getInstance(context).addToRequestQue(stringRequest);
        }
    }

    private class Sincronizar extends AsyncTask<Void,Void,Void> {
        Context context;
        AlertDialog alertDialog;
        public Sincronizar(Context context,AlertDialog alertDialog)
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
            if(insertadoCiudad > 0){
                //Toast.makeText(context.getApplicationContext(), "Sincronizado Correctamente", Toast.LENGTH_LONG).show();
                Snackbar.make(findViewById(R.id.linearLayout24), "Sincronizado Correctamente!.", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
                lista.clear();
                llenarLista();
            }else{
                //Toast.makeText(context.getApplicationContext(),"Error insertado datos del servidor",Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(R.id.linearLayout24), "Error insertando datos del servidor.", Snackbar.LENGTH_SHORT)
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
                Snackbar.make(findViewById(R.id.linearLayout24), "IP DEL SERVIDOR NO ENCONTRADO.", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
            }
        } catch (Exception e) {
           // Toast.makeText(this, "TABLA DE SERVIDOR NO ENCONTRADO.", Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.linearLayout24), "TABLA DE SERVIDOR NO ENCONTRADO.", Snackbar.LENGTH_SHORT)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setBackgroundTint(Color.parseColor("#11232E"))
                    .setActionTextColor(Color.parseColor("#FFFFFF")).show();
        }
    }
}