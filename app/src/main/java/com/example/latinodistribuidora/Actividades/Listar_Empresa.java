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
import com.example.latinodistribuidora.CRUD.Access_Empresa;
import com.example.latinodistribuidora.CRUD.Access_Servidor;
import com.example.latinodistribuidora.Conexion.MySingleton;
import com.example.latinodistribuidora.Modelos.Empresas;
import com.example.latinodistribuidora.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class Listar_Empresa extends AppCompatActivity {
    private ListView lv;
    private static ArrayList<Empresas> lista = new ArrayList<>();
    private ArrayAdapter<String> adaptador;
    private int empresaseleccionado = -1;
    public Object mActionMode;
    private TextView pie;
    private static long insertadoEmpresa=0;
    private String IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_empresa);
        pie = findViewById(R.id.id_emp_pie);
        ObtnerIP();
        llenarLista();

        findViewById(R.id.btnDOWNEMP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Listar_Empresa.this);
                alertDialog.setMessage("¿Seguro que desea descargar la información de mi empresa para esta aplicación?");
                alertDialog.setTitle("ACTUALIZACIÓN DE DATOS");
                alertDialog.setIcon(R.drawable.ic_descargarpcdark);
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("SI, COMENZAR DESCARGA", (dialog, which) -> sincronizarEmpresa(Listar_Empresa.this));
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
                empresaseleccionado = position;
                mActionMode = Listar_Empresa.this.startActionMode(amc);
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
        if(id==R.id.item_nuevo){
            ir_a_RegistrarEmpresa(null);
            return true;
        }*/
        /*if(id==R.id.item_download){
            sincronizarEmpresa(Listar_Empresa.this);
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
                    Empresas empresas = lista.get(empresaseleccionado);
                    Intent in = new Intent(getApplicationContext(), Editar_Empresa.class);
                    in.putExtra("idempresa", empresas.getIdempresa());
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
        alertDialog.setMessage("¿Desea eliminar la empresa seleccionada?");
        alertDialog.setTitle("Eliminar");
        alertDialog.setIcon(android.R.drawable.ic_delete);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Sí", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                EliminarEmpresa();
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

    public void EliminarEmpresa(){
        try{
            Access_Empresa db = Access_Empresa.getInstance(getApplicationContext());
            Empresas empresas = lista.get(empresaseleccionado);
            //db.openWritable();
            //Toast.makeText(getApplicationContext(),empresas.getIdempresa(), Toast.LENGTH_LONG).show();
            long resultado = db.EliminarEmpresa(empresas.getIdempresa());
            if(resultado > 0){
                Toast.makeText(getApplicationContext(),"Empresa eliminada satisfactoriamente", Toast.LENGTH_LONG).show();
                lista.removeAll(lista);
                llenarLista();
            }else{
                Toast.makeText(getApplicationContext(),"Se produjo un error al eliminar empresa", Toast.LENGTH_LONG).show();
            }
            db.close();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error Fatal: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void llenarLista(){
        try{
            lv = (ListView) findViewById(R.id.id_lista_empresas);
            Access_Empresa db = Access_Empresa.getInstance(getApplicationContext());
            Cursor c = db.getEmpresas();
            if (c.moveToFirst()){
                do {
                    lista.add( new Empresas (c.getInt(0),c.getString(1),
                            c.getString(2), c.getString(3), c.getString(4),
                            c.getString(6)));
                }while (c.moveToNext());
            }
            String[] arreglo = new String[lista.size()];
            for (int i = 0;i<arreglo.length;i++){
                arreglo[i] = "Razón Social: "+lista.get(i).getRazon_social()+"\nR.U.C.: "+lista.get(i).getRuc()+
                "\nDirección: "+lista.get(i).getDireccion()+"\nTelefono: "+lista.get(i).getTelefono()+
                "\nCiudad: "+lista.get(i).getCiudad();
            }
            adaptador = new ArrayAdapter<String>(getApplicationContext(), R.layout.listview_item_ld,arreglo);
            lv.setAdapter(adaptador);
            int cant = lv.getCount();
            if(cant==0){
                pie.setText("Lista vacía".toUpperCase());
            }
            if(cant<=1){
                pie.setText(String .valueOf(cant).concat(" empresa listada"));
            }else{
                pie.setText(String .valueOf(cant).concat(" empresas listadas"));
            }
            db.close();

        }catch (Exception e){
            Toast.makeText(this, "Error cargando lista: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /*public void ir_a_RegistrarEmpresa(View view){
        Intent i = new Intent(getApplicationContext(), Registrar_Empresa.class);
        startActivity(i);
        finish();
    }*/
    public static boolean checkNetworkConnection(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }

    public void sincronizarEmpresa(Context context)
    {
        if(checkNetworkConnection(context))
        {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://"+IP+context.getResources().getString(R.string.URL_UPDATE_EMPRESA), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Access_Empresa db = Access_Empresa.getInstance(context.getApplicationContext());
                    db.borrarEmpresa();
                    ProgressDialog progressDialog = new ProgressDialog(Listar_Empresa.this);
                    progressDialog.setMessage("Sincronizando Empresa");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        new Sincronizar(Listar_Empresa.this,progressDialog).execute();
                    try {
                        JSONArray array = new JSONArray(response);
                        for(int i = 0; i<array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);

                            db.openWritable();
                            insertadoEmpresa= db.insertarEmpresaServer(object.getInt("idempresa"), object.getString("razon_social"),
                                    object.getString("ruc"),object.getString("direccion"), object.getString("telefono"),
                                    object.getString("estado"), object.getInt("ciudad_idciudad"));
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
                    //Toast.makeText(context.getApplicationContext(),message,Toast.LENGTH_LONG).show();
                    Snackbar.make(findViewById(R.id.linearLayout21), message, Snackbar.LENGTH_SHORT)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .setBackgroundTint(Color.parseColor("#11232E"))
                            .setActionTextColor(Color.parseColor("#FFFFFF")).show();
                    Log.d(TAG, "jsArrayRequest Error : "+ message);
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
            progressDialog.onBackPressed();
            if(insertadoEmpresa > 0){
               // Toast.makeText(context.getApplicationContext(), "Sincronizado Correctamente", Toast.LENGTH_LONG).show();
                Snackbar.make(findViewById(R.id.linearLayout21), "Sincronizado Correctamente!.", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
                lista.clear();
                llenarLista();
            }else{
                //Toast.makeText(context.getApplicationContext(),"Error insertado datos del servidor",Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(R.id.linearLayout21), "Error insertando datos del servidor.", Snackbar.LENGTH_SHORT)
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
               // Toast.makeText(this, "IP DEL SERVIDOR NO ENCONTRADO.", Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(R.id.linearLayout21), "IP DEL SERVIDOR NO ENCONTRADO.", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
            }
        } catch (Exception e) {
            //Toast.makeText(this, "TABLA DE SERVIDOR NO ENCONTRADO.", Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.linearLayout21), "TABLA DE SERVIDOR NO ENCONTRADO.", Snackbar.LENGTH_SHORT)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setBackgroundTint(Color.parseColor("#11232E"))
                    .setActionTextColor(Color.parseColor("#FFFFFF")).show();
        }
    }
}