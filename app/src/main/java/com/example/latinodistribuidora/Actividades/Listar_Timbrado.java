package com.example.latinodistribuidora.Actividades;

import static android.content.ContentValues.TAG;

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
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.latinodistribuidora.CRUD.Access_Servidor;
import com.example.latinodistribuidora.CRUD.Access_Timbrado;
import com.example.latinodistribuidora.Conexion.MySingleton;
import com.example.latinodistribuidora.Modelos.Timbrado;
import com.example.latinodistribuidora.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Listar_Timbrado extends AppCompatActivity {
    private ListView lv;
    private final ArrayList<Timbrado> lista = new ArrayList<>();
    private int timbradoseleccionado = -1;
    public Object mActionMode;
    private TextView pie;
    private static long insertadoTimbrado=0;
    private String IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_timbrado);
        pie = findViewById(R.id.id_tim_pie);
        ObtnerIP();
        llenarLista();
        onClick();

        findViewById(R.id.btnDOWNTIM).setOnClickListener(v -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Listar_Timbrado.this);
            alertDialog.setMessage("¿Seguro que desea descargar la lista completa de timbrados habilitados para esta aplicación?");
            alertDialog.setTitle("ACTUALIZACIÓN DE DATOS");
            alertDialog.setIcon(R.drawable.ic_descargarpcdark);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("SI, COMENZAR DESCARGA", (dialog, which) -> sincronizarTimbrado(Listar_Timbrado.this));
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
            timbradoseleccionado = position;
            mActionMode = Listar_Timbrado.this.startActionMode(amc);
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
            //ir_a_RegistrarTimbrado(null);
            return true;
        }
        if(id==R.id.item_download){
            sincronizarTimbrado(Listar_Timbrado.this);
            return true;
        }*/
        return super.onOptionsItemSelected(menuItem);
    }

    private final ActionMode.Callback amc = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            getMenuInflater().inflate(R.menu.opciones_upd, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {return false;}

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.item_modificar) {
                Timbrado timbrado = lista.get(timbradoseleccionado);
                Intent in = new Intent(getApplicationContext(), Editar_Timbrado.class);
                in.putExtra("idtimbrado", timbrado.getIdtimbrado());
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

  /*  private void AlertaEliminacion(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("¿Desea Desactivar el timbrado seleccionado?");
        alertDialog.setTitle("Desactivar");
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Sí", (dialog, which) -> eliminarTimbrado());
        alertDialog.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }*/

    public void eliminarTimbrado(){
        try{
            Access_Timbrado db = Access_Timbrado.getInstance(getApplicationContext());
            Timbrado timbrado = lista.get(timbradoseleccionado);
            db.openWritable();
            long resultado = db.CerrarTimbrado(timbrado.getIdtimbrado());
            if(resultado > 0){
                Toast.makeText(getApplicationContext(),"Timbrado desactivado satisfactoriamente", Toast.LENGTH_LONG).show();
                lista.removeAll(lista);
                llenarLista();
            }else{
                Toast.makeText(getApplicationContext(),"Se produjo un error al desactivar timbrado", Toast.LENGTH_LONG).show();
            }
            db.close();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error Fatal: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void llenarLista(){
        try{
            lv = (ListView) findViewById(R.id.id_lista_timbrado);
            Access_Timbrado db = Access_Timbrado.getInstance(getApplicationContext());
            Cursor c = db.getTimbradoActivo();
            if (c.moveToFirst()){
                do {
                    lista.add( new Timbrado (c.getInt(0),c.getString(1),c.getString(2),
                            c.getString(3),c.getString(4)));
                }while (c.moveToNext());
            }
            String[] arreglo = new String[lista.size()];
            for (int i = 0;i<arreglo.length;i++){
                arreglo[i] = "TIMBRADO NRO: "+lista.get(i).getTimbrado()+"\n"
                            +"VALIDEZ INICIO: "+lista.get(i).getDesde()+"\n"
                            +"VALIDEZ FIN: "+lista.get(i).getHasta()+"\n"
                            +"ESTADO: "+lista.get(i).getEstado();
            }
            ArrayAdapter<String> adaptador = new ArrayAdapter<>(getApplicationContext(), R.layout.listview_item_ld, arreglo);
            lv.setAdapter(adaptador);
            int cant = lv.getCount();
            if(cant==0){
                pie.setText("Lista vacía".toUpperCase());
            }else if(cant==1){
                pie.setText(String.valueOf(cant).concat(" timbrado listado"));
            }else{
                pie.setText(String.valueOf(cant).concat(" timbrados listados"));
            }
            db.close();

        }catch (Exception e){
            Toast.makeText(this, "Error cargando lista: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
   /* public void ir_a_RegistrarTimbrado(View view){
        Intent i = new Intent(getApplicationContext(), Registar_Timbrado.class);
        startActivity(i);
        finish();
    }*/

    public static boolean checkNetworkConnection(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }

    public void sincronizarTimbrado(Context context) {
        if (checkNetworkConnection(context)) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://"+IP+context.getResources().getString(R.string.URL_UPDATE_TIMBRADO), response -> {
                Access_Timbrado db = Access_Timbrado.getInstance(context.getApplicationContext());
                db.borrarTimbrado();
                ProgressDialog progressDialog = new ProgressDialog(Listar_Timbrado.this);
                progressDialog.setMessage("Sincronizando Timbrado");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                new Sincronizar(Listar_Timbrado.this,progressDialog).execute();
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject objectT = array.getJSONObject(i);
                        db.openWritable();
                        insertadoTimbrado= db.insertarTimbradoServer(objectT.getInt("idtimbrado"), objectT.getString("descripcion"),
                                objectT.getString("fechadesde"), objectT.getString("fechahasta"), objectT.getString("estado"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

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
                    Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    Snackbar.make(findViewById(R.id.linearLayout41), message, Snackbar.LENGTH_SHORT)
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
        ProgressDialog progressDialog;
        public Sincronizar(Context context,ProgressDialog progressDialog)
        {
            this.progressDialog = progressDialog;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //sincronizarEmpresa(context);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //progressDialog.hide();
            progressDialog.onBackPressed();
            if(insertadoTimbrado > 0){
                //Toast.makeText(context.getApplicationContext(), "Sincronizado Correctamente", Toast.LENGTH_LONG).show();
                Snackbar.make(findViewById(R.id.linearLayout41), "Sincronizado Correctamente!.", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
                lista.clear();
                llenarLista();
            }else{
               // Toast.makeText(context.getApplicationContext(),"Error insertado datos del servidor",Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(R.id.linearLayout41), "Error insertando datos del servidor.", Snackbar.LENGTH_SHORT)
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
                Snackbar.make(findViewById(R.id.linearLayout41), "IP DEL SERVIDOR NO ENCONTRADO.", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
            }
        } catch (Exception e) {
           // Toast.makeText(this, "TABLA DE SERVIDOR NO ENCONTRADO.", Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.linearLayout41), "TABLA DE SERVIDOR NO ENCONTRADO.", Snackbar.LENGTH_SHORT)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setBackgroundTint(Color.parseColor("#11232E"))
                    .setActionTextColor(Color.parseColor("#FFFFFF")).show();
        }
    }
}