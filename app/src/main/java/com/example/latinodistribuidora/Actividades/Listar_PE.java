package com.example.latinodistribuidora.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
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
import com.example.latinodistribuidora.Adaptador_PE;
import com.example.latinodistribuidora.CRUD.Access_PE;
import com.example.latinodistribuidora.CRUD.Access_Servidor;
import com.example.latinodistribuidora.Conexion.MySingleton;
import com.example.latinodistribuidora.Modelos.PuntoEmision;
import com.example.latinodistribuidora.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

import static android.content.ContentValues.TAG;

public class Listar_PE extends AppCompatActivity {
    private ListView lv;
    private ArrayList<PuntoEmision> lista = new ArrayList<>();
    private Adaptador_PE adaptador_pe;
    private int peseleccionado = -1;
    private Object mActionMode;
    private TextView pie;
    private static long insertadoPuntoEmision=0;
    private String IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_pe);
        pie = findViewById(R.id.id_pe_pie);
        ObtnerIP();
        llenarLista();
        onClick();

        findViewById(R.id.btnDOWNPE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Listar_PE.this);
                alertDialog.setMessage("¿Seguro que desea descargar los datos relacionados al puntos de emisión habilitado para este móvil?");
                alertDialog.setTitle("ACTUALIZACIÓN DE DATOS");
                alertDialog.setIcon(R.drawable.ic_descargarpcdark);
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("SI, COMENZAR DESCARGA", (dialog, which) -> sincronizarPuntoEmision(Listar_PE.this));
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
        lv.setOnItemLongClickListener((parent, view, position, id) -> {
            peseleccionado = position;
            mActionMode = Listar_PE.this.startActionMode(amc);
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
        /*int id=menuItem.getItemId();
        if(id==R.id.item_nuevo){
            //ir_a_RegistrarPE(null);
            return true;
        }
        if(id==R.id.item_download){
            sincronizarPuntoEmision(Listar_PE.this);
            return true;
        }*/
        return super.onOptionsItemSelected(menuItem);
    }

    private ActionMode.Callback amc = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            getMenuInflater().inflate(R.menu.opciones_upd, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.item_modificar) {
                PuntoEmision pe = lista.get(peseleccionado);
                Intent in = new Intent(getApplicationContext(), Editar_PE.class);
                in.putExtra("idemision", pe.getIdemision());
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

    private void llenarLista(){
        try{
            lv = (ListView) findViewById(R.id.id_lista_pe);
            Access_PE db = Access_PE.getInstance(getApplicationContext());
            Cursor c = db.getPE();
            if (c.moveToFirst()){
                do {
                    lista.add( new PuntoEmision (c.getInt(0),c.getString(1),c.getString(2),c.getString(3),
                            c.getString(4),c.getString(5),c.getString(6),c.getString(7)));
                }while (c.moveToNext());
            }
            adaptador_pe = new Adaptador_PE(this,lista);
            lv.setAdapter(adaptador_pe);
            int cant = lv.getCount();
            if(cant==0){
                pie.setText("Lista vacía");
            }else if(cant==1){
                pie.setText(cant+" punto de emisión listado");
            }else{
                pie.setText(cant+" puntos de emisiones listados");
            }
            db.close();

        }catch (Exception e){
            Toast.makeText(this, "Error cargando lista: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void ir_a_RegistrarPE(View view){
        Intent i = new Intent(getApplicationContext(), Registrar_PE.class);
        startActivity(i);
        finish();
    }

    public static boolean checkNetworkConnection(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }

    public void sincronizarPuntoEmision(Context context) {
        if (checkNetworkConnection(context)) {
            Access_PE db = Access_PE.getInstance(context.getApplicationContext());
            int idPE = 0;
            Cursor PuntoActivo = db.getPEActivo();
            if (PuntoActivo.moveToFirst()){
                do {
                    idPE=PuntoActivo.getInt(0);
                }while (PuntoActivo.moveToNext());
            }
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://"+IP+context.getResources().getString(R.string.URL_UPDATE_PUNTOEMISION)+idPE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    AlertDialog mDialog= new SpotsDialog.Builder()
                            .setContext(Listar_PE.this)
                            .setMessage("Sincronizando Punto de Emisión")
                            .setCancelable(false)
                            .build();
                    new Sincronizar(Listar_PE.this, mDialog).execute();
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            db.openWritable();
                            insertadoPuntoEmision= db.ActualizarPESErver(object.getInt("idemision"),object.getInt("idtimbrado"), object.getString("establecimiento"),
                                    object.getString("puntoemision"), object.getString("direccion"),object.getInt("facturainicio"),
                                    object.getInt("facturafin"),object.getInt("facturaactual"));
                                    db.ActualizarRef(object.getInt("idemision"),object.getInt("nventa"));
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
                   // Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    Snackbar.make(findViewById(R.id.linearLayout45), message, Snackbar.LENGTH_SHORT)
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
                Thread.sleep(10000);
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
            if(insertadoPuntoEmision > 0){
                //Toast.makeText(context.getApplicationContext(), "Sincronizado Correctamente", Toast.LENGTH_LONG).show();
                Snackbar.make(findViewById(R.id.linearLayout45), "Sincronizado correctamente!.", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
                lista.clear();
                llenarLista();
            }else{
                //Toast.makeText(context.getApplicationContext(),"Error insertado datos del servidor",Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(R.id.linearLayout45), "Error insertando datos del servidor.", Snackbar.LENGTH_SHORT)
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
                Snackbar.make(findViewById(R.id.linearLayout45), "IP DEL SERVIDOR NO ENCONTRADO.", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
            }
        } catch (Exception e) {
            //Toast.makeText(this, "TABLA DE SERVIDOR NO ENCONTRADO.", Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.linearLayout45), "TABLA DE SERVIDOR NO ENCONTRADO.", Snackbar.LENGTH_SHORT)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setBackgroundTint(Color.parseColor("#11232E"))
                    .setActionTextColor(Color.parseColor("#FFFFFF")).show();
        }
    }
}