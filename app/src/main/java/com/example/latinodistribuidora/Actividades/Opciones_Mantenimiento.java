package com.example.latinodistribuidora.Actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.latinodistribuidora.Adaptador_ventas;
import com.example.latinodistribuidora.CRUD.Access_Empresa;
import com.example.latinodistribuidora.CRUD.Access_PE;
import com.example.latinodistribuidora.CRUD.Access_Productos;
import com.example.latinodistribuidora.CRUD.Access_Servidor;
import com.example.latinodistribuidora.CRUD.Access_Timbrado;
import com.example.latinodistribuidora.CRUD.Access_Venta;
import com.example.latinodistribuidora.CRUD.Access_Venta1;
import com.example.latinodistribuidora.Conexion.MySingleton;
import com.example.latinodistribuidora.MainActivity;
import com.example.latinodistribuidora.Modelos.DetalleVentaSync;
import com.example.latinodistribuidora.Modelos.PuntoEmision;
import com.example.latinodistribuidora.Modelos.PuntoEmisionSync;
import com.example.latinodistribuidora.Modelos.Ventas;
import com.example.latinodistribuidora.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static android.content.ContentValues.TAG;


public class Opciones_Mantenimiento extends AppCompatActivity {
    private Button btnRespaldarDatos;
    private ImageButton btnActualizarPE;
    private ArrayList<Ventas> listaVentas = new ArrayList<>();
    private ArrayList<DetalleVentaSync> listaDetalles = new ArrayList<>();
    private ArrayList<PuntoEmisionSync> listaPuntoEmision = new ArrayList<>();
    private static long respaldadoVentas;
    private static long respaldPE;
    String jsonStrD;
    String jsonStrV;
    private String IP;
    private static final String CHANNEL_ID = "canal";
    private PendingIntent pendingIntent;

    private static long insertadoPuntoEmision = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones_mantenimiento);
        ObtnerIP();
        ObtenerVentas();
        ObtenerDetalle();
        ObtenerPuntoEmision();

        btnRespaldarDatos = findViewById(R.id.id_btnSincronizarServidor);
        btnActualizarPE = (ImageButton) findViewById(R.id.id_updatePE);

        btnRespaldarDatos.setOnClickListener(v -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Opciones_Mantenimiento.this);
            alertDialog.setMessage("¿Seguro que desea respaldar todos los datos de ventas, operaciones y referencias generados en la aplicación?");
            alertDialog.setTitle("BACKUP DE DATOS");
            alertDialog.setIcon(R.drawable.ic_respaldo);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("SI; COMENZAR RESPALDO", (dialog, which) -> {
                SyncVentas();
                SyncPuntoEmision();
            });
            alertDialog.setNegativeButton("CANCELAR", (dialog, which) -> dialog.cancel());
            alertDialog.show();


        });

        btnActualizarPE.setOnClickListener(v -> {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Opciones_Mantenimiento.this);
            alertDialog.setMessage("¿Seguro que desea descargar la lista completa de puntos de emisión habilitados para el Timbrado asignado en la aplicación?");
            alertDialog.setTitle("ACTUALIZACIÓN DE DATOS");
            alertDialog.setIcon(R.drawable.ic_descargarpcdark);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("SI, COMENZAR DESCARGA", (dialog, which) -> ActualizarPuntoEmision(Opciones_Mantenimiento.this));
            alertDialog.setNegativeButton("CANCELAR", (dialog, which) -> dialog.cancel());
            alertDialog.show();
        });

    }

    /*public void onResume() {
        super.onResume();
        FaltaSync();
    }*/

    public static boolean checkNetworkConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void SyncVentas() {
        if (listaVentas.size() == 0) {
            //Toast.makeText(this, "0 registro de ventas a respaldar", Toast.LENGTH_LONG).show();
            Snackbar.make(findViewById(R.id.linearLayout7), "\"0\" registros de ventas a respaldar", Snackbar.LENGTH_SHORT)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setBackgroundTint(Color.parseColor("#11232E"))
                    .setActionTextColor(Color.parseColor("#FFFFFF")).show();
        } else {
            JSONArray jsonArrayVentas = new JSONArray();
            for (int i = 0; i < listaVentas.size(); i++) {
                JSONObject jsonObjectProducto = new JSONObject();
                try {
                    jsonObjectProducto.put("idventas", listaVentas.get(i).getId());
                    jsonObjectProducto.put("idemision", listaVentas.get(i).getIdemision());
                    jsonObjectProducto.put("idusuario", listaVentas.get(i).getIdusuario());
                    jsonObjectProducto.put("ruc", listaVentas.get(i).getRuc());
                    jsonObjectProducto.put("descripcion", listaVentas.get(i).getDescripcion());
                    jsonObjectProducto.put("nrofactura", listaVentas.get(i).getNrofactura());
                    jsonObjectProducto.put("condicion", listaVentas.get(i).getCondicion());
                    jsonObjectProducto.put("fecha", listaVentas.get(i).getFecha());
                    jsonObjectProducto.put("hora", listaVentas.get(i).getHora());
                    jsonObjectProducto.put("total", listaVentas.get(i).getTotal());
                    jsonObjectProducto.put("exenta", listaVentas.get(i).getExenta());
                    jsonObjectProducto.put("iva5", listaVentas.get(i).getIva5());
                    jsonObjectProducto.put("iva10", listaVentas.get(i).getIva10());
                    jsonObjectProducto.put("estado", listaVentas.get(i).getEstado());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArrayVentas.put(jsonObjectProducto);
            }
            JSONObject json = new JSONObject();
            try {
                json.put("Ventas", jsonArrayVentas);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            jsonStrV = json.toString();
            //
        }
        if (listaDetalles.size() == 0) {
            //Toast.makeText(this, "0 registro de detalles de ventas a respaldar", Toast.LENGTH_LONG).show();
            Snackbar.make(findViewById(R.id.linearLayout7), "\"0\" registros de detalles de ventas a respaldar", Snackbar.LENGTH_SHORT)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setBackgroundTint(Color.parseColor("#11232E"))
                    .setActionTextColor(Color.parseColor("#FFFFFF")).show();
        } else {
            JSONArray jsonArrayDetalle = new JSONArray();
            for (int i = 0; i < listaDetalles.size(); i++) {
                JSONObject jsonObjectDetalle = new JSONObject();
                try {
                    jsonObjectDetalle.put("venta_idventa", listaDetalles.get(i).getIdventa());
                    jsonObjectDetalle.put("idemision", listaDetalles.get(i).getIdemision());
                    jsonObjectDetalle.put("productos_idproductos", listaDetalles.get(i).getProductos_idproductos());
                    jsonObjectDetalle.put("cantidad", listaDetalles.get(i).getCantidad());
                    jsonObjectDetalle.put("precio", listaDetalles.get(i).getPrecio());
                    jsonObjectDetalle.put("total", listaDetalles.get(i).getTotal());
                    jsonObjectDetalle.put("impuesto_aplicado", listaDetalles.get(i).getImpuesto_aplicado());
                    jsonObjectDetalle.put("um", listaDetalles.get(i).getUm());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArrayDetalle.put(jsonObjectDetalle);
            }
            JSONObject json = new JSONObject();
            try {
                json.put("Detalles", jsonArrayDetalle);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            jsonStrD = json.toString();

            RespaldarVentas(jsonStrV, jsonStrD, Opciones_Mantenimiento.this);
        }
    }

    public void RespaldarVentas(final String jsonV, final String jsonD, Context context) {

        if (checkNetworkConnection(context)) {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + IP + getResources().getString(R.string.URL_SAVE_VENTAS), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    AlertDialog mDialog = new SpotsDialog.Builder()
                            .setContext(Opciones_Mantenimiento.this)
                            .setMessage("Respaldando datos al servidor")
                            .setCancelable(false)
                            .build();
                    new SincronizarVenta(Opciones_Mantenimiento.this, mDialog).execute();
                    if (response.trim().equals("OK")) {
                        respaldadoVentas = 1;
                        Access_Venta1 db = Access_Venta1.getInstance(getApplicationContext());
                        db.openWritable();
                        //db.borrarVentas();
                        //db.borrarDetallesVenta();
                        db.actualizarSync();
                        db.actualizarDSync();
                        listaVentas.clear();
                        listaDetalles.clear();
                        db.close();
                    } else if (response.trim().equals("NO")) {
                        respaldadoVentas = 0;
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //error.printStackTrace();
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
                    //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    Snackbar.make(findViewById(R.id.linearLayout7), message, Snackbar.LENGTH_SHORT)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .setBackgroundTint(Color.parseColor("#11232E"))
                            .setActionTextColor(Color.parseColor("#FFFFFF")).show();
                    Log.d(TAG, "jsArrayRequest Error : " + message);
                }
            }) {
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("jsonV", jsonV);
                    params.put("jsonD", jsonD);

                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }

    public void SyncPuntoEmision() {
        if (listaPuntoEmision.size() == 0) {
            //Toast.makeText(this, "No existe punto de emisión activo a respaldar.", Toast.LENGTH_LONG).show();
            Snackbar.make(findViewById(R.id.linearLayout7), "\"0\" información de punto de emisión a respaldar.", Snackbar.LENGTH_SHORT)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setBackgroundTint(Color.parseColor("#11232E"))
                    .setActionTextColor(Color.parseColor("#FFFFFF")).show();
        } else {
            JSONArray jsonArrayPE = new JSONArray();
            for (int i = 0; i < listaPuntoEmision.size(); i++) {
                JSONObject jsonObjectPE = new JSONObject();
                try {
                    jsonObjectPE.put("facturaactual", listaPuntoEmision.get(i).getActual());
                    jsonObjectPE.put("nventa", listaPuntoEmision.get(i).getNventa());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArrayPE.put(jsonObjectPE);
            }
            JSONObject json = new JSONObject();
            try {
                json.put("PE", jsonArrayPE);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            String jsonStrPE = json.toString();
            RespaldarPE(jsonStrPE, Opciones_Mantenimiento.this);
        }
    }

    public void RespaldarPE(final String json, Context context) {
        if (checkNetworkConnection(context)) {
            Access_PE db = Access_PE.getInstance(context.getApplicationContext());
            int idPE = 0;
            Cursor PuntoActivo = db.getPEActivo();
            if (PuntoActivo.moveToFirst()) {
                do {
                    idPE = PuntoActivo.getInt(0);
                } while (PuntoActivo.moveToNext());
            }
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://" + IP + getResources().getString(R.string.URL_SETUPDATE_PUNTOEMISION) + idPE, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    AlertDialog mDialog = new SpotsDialog.Builder()
                            .setContext(Opciones_Mantenimiento.this)
                            .setMessage("Respaldando datos al servidor")
                            .setCancelable(false)
                            .build();
                    new SincronizarPE(Opciones_Mantenimiento.this, mDialog).execute();
                    if (response.trim().equals("OK")) {
                        respaldPE = 1;
                    } else if (response.trim().equals("NO")) {
                        respaldPE = 0;
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //error.printStackTrace();
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
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    Snackbar.make(findViewById(R.id.linearLayout7), message, Snackbar.LENGTH_SHORT)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .setBackgroundTint(Color.parseColor("#11232E"))
                            .setActionTextColor(Color.parseColor("#FFFFFF")).show();
                    Log.d(TAG, "jsArrayRequest Error : " + message);
                }
            }) {
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("json", json);

                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }

    public void ActualizarPuntoEmision(Context context) {
        if (checkNetworkConnection(context)) {
            Access_Timbrado db = Access_Timbrado.getInstance(context.getApplicationContext());
            int idT = 0;
            Cursor PuntoActivo = db.getTimbradoActivo();
            if (PuntoActivo.moveToFirst()) {
                do {
                    idT = PuntoActivo.getInt(0);
                } while (PuntoActivo.moveToNext());
            }
            StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://" + IP + context.getResources().getString(R.string.URL_UPDATE_PUNTOEMISIONGENERAL) + idT, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Access_PE db = Access_PE.getInstance(context.getApplicationContext());
                    db.borrarPuntoEmisionGeneral();
                    db.borrarRef();
                    AlertDialog mDialog = new SpotsDialog.Builder()
                            .setContext(Opciones_Mantenimiento.this)
                            .setMessage("Cargando Listado de Puntos de Emisión")
                            .setCancelable(false)
                            .build();
                    new Sincronizar(Opciones_Mantenimiento.this, mDialog).execute();
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            db.openWritable();
                            insertadoPuntoEmision = db.InsertarPESErver(object.getInt("idemision"), object.getInt("idtimbrado"), object.getString("establecimiento"),
                                    object.getString("puntoemision"), object.getString("direccion"), object.getInt("facturainicio"),
                                    object.getInt("facturafin"), object.getInt("facturaactual"), object.getString("estado"));
                            db.InsertRef(object.getInt("idemision"), object.getInt("nventa"));
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
                    Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    Snackbar.make(findViewById(R.id.linearLayout7), message, Snackbar.LENGTH_SHORT)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .setBackgroundTint(Color.parseColor("#11232E"))
                            .setActionTextColor(Color.parseColor("#FFFFFF")).show();
                    Log.d(TAG, "jsArrayRequest Error : " + message);
                }
            });
            MySingleton.getInstance(context).addToRequestQue(stringRequest);
        }
    }

    private class Sincronizar extends AsyncTask<Void, Void, Void> {
        Context context;
        AlertDialog alertDialog;

        public Sincronizar(Context context, AlertDialog alertDialog) {
            this.alertDialog = alertDialog;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(7500);
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
            if (insertadoPuntoEmision > 0) {
                //Toast.makeText(context.getApplicationContext(), "Lista cargada correctamente!", Toast.LENGTH_LONG).show();
                Snackbar.make(findViewById(R.id.linearLayout7), "LISTA CARGADA CORRECTAMENTE!.", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
            } else {
                //Toast.makeText(context.getApplicationContext(), "Error insertado datos del servidor", Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(R.id.linearLayout7), "Error insertando datos del servidor.", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
            }
        }
    }

    public void irServidor(View view) {
        Intent i = new Intent(this, Servidor.class);
        startActivity(i);
        finish();
    }

    public void irListaDepartamentos(View view) {
        Intent i = new Intent(this, Listar_Departamento.class);
        startActivity(i);
    }

    public void irListaUsuarios(View v) {
        Intent i = new Intent(getApplicationContext(), Listar_Usuario.class);
        startActivity(i);
    }

    public void irListaCiudad(View view) {
        Intent i = new Intent(getApplicationContext(), Listar_Ciudad.class);
        startActivity(i);
    }

    public void irListaEmpresa(View view) {
        Intent i = new Intent(getApplicationContext(), Listar_Empresa.class);
        startActivity(i);
    }

    public void irListaDivision(View view) {
        Intent i = new Intent(getApplicationContext(), Listar_Division.class);
        startActivity(i);
    }

    public void irListaIVA(View view) {
        Intent i = new Intent(getApplicationContext(), Listar_IVA.class);
        startActivity(i);
    }

    public void irListaUM(View view) {
        Intent i = new Intent(getApplicationContext(), ListarUM.class);
        startActivity(i);
    }

    public void irListaTimbrado(View view) {
        Intent i = new Intent(getApplicationContext(), Listar_Timbrado.class);
        startActivity(i);
    }

    public void irListaPE(View view) {
        Intent i = new Intent(getApplicationContext(), Listar_PE.class);
        startActivity(i);
    }

    public class SincronizarVenta extends AsyncTask<Void, Void, Void> {
        Context context;
        AlertDialog alertDialog;

        public SincronizarVenta(Context context, AlertDialog alertDialog) {
            this.alertDialog = alertDialog;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(7500);
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
            if (respaldadoVentas == 1) {
                //Toast.makeText(context.getApplicationContext(), "Los datos de ventas fueron respaldados exitosamente al servidor", Toast.LENGTH_LONG).show();
                Snackbar.make(findViewById(R.id.linearLayout7), "Los datos de ventas fueron respaldados exitosamente al servidor!.", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
            } else if (respaldadoVentas == 0) {
                //Toast.makeText(context.getApplicationContext(), "Error respaldando datos del ventas al servidor", Toast.LENGTH_LONG).show();
                Snackbar.make(findViewById(R.id.linearLayout7), "Error respaldando datos de ventas al servidor.", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
            }

        }
    }

    public class SincronizarPE extends AsyncTask<Void, Void, Void> {
        Context context;
        AlertDialog alertDialog;

        public SincronizarPE(Context context, AlertDialog alertDialog) {
            this.alertDialog = alertDialog;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(7500);
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
            if (respaldPE == 1) {
                //Toast.makeText(context.getApplicationContext(), "Los datos de operaciones y referencias fueron respaldados exitosamente al servidor", Toast.LENGTH_LONG).show();
                Snackbar.make(findViewById(R.id.linearLayout7), "Los datos de operaciones y referencias fueron respaldados exitosamente al servidor!.", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
            } else if (respaldPE == 0) {
                //Toast.makeText(context.getApplicationContext(), "Error respaldando operaciones y referencias al servidor", Toast.LENGTH_LONG).show();
                Snackbar.make(findViewById(R.id.linearLayout7), "Error respaldando operaciones y referencias al servidor.", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
            }
        }
    }

    public void ObtenerVentas() {
        try {
            Access_Venta1 db = Access_Venta1.getInstance(getApplicationContext());
            db.openWritable();
            Cursor c = db.getv_ventaServer();
            if (c.moveToFirst()) {
                do {
                    listaVentas.add(new Ventas(c.getInt(0), c.getInt(1), c.getString(2), c.getString(3),
                            c.getString(4), c.getString(6), c.getString(7), c.getString(8),
                            c.getString(9), c.getString(10), c.getString(11), c.getInt(12), c.getInt(13),
                            c.getInt(14), c.getInt(15), c.getInt(16), c.getString(17),
                            c.getString(18), c.getString(19)));
                } while (c.moveToNext());
            }
            db.close();

        } catch (Exception e) {
            Toast.makeText(this, "Error cargando lista de ventas: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void ObtenerDetalle() {
        try {
            Access_Venta1 db = Access_Venta1.getInstance(getApplicationContext());
            db.openWritable();
            Cursor c = db.getDetalleSync();
            if (c.moveToFirst()) {
                do {
                    listaDetalles.add(new DetalleVentaSync(c.getInt(0), c.getInt(1), c.getInt(2), c.getString(3),
                            c.getInt(4), c.getInt(5), c.getInt(6), c.getString(7)));
                } while (c.moveToNext());
            }
            db.close();

        } catch (Exception e) {
            Toast.makeText(this, "Error cargando Detalles de ventas: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void ObtenerPuntoEmision() {
        try {
            Access_PE db = Access_PE.getInstance(getApplicationContext());
            db.openWritable();
            Cursor c = db.getPEServer();
            if (c.moveToFirst()) {
                do {
                    listaPuntoEmision.add(new PuntoEmisionSync(c.getInt(6), c.getInt(8)));
                } while (c.moveToNext());
            }
            db.close();

        } catch (Exception e) {
            Toast.makeText(this, "Error cargando lista de punto de emision: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Snackbar.make(findViewById(R.id.linearLayout7), "IP DEL SERVIDOR NO ENCONTRADO.", Snackbar.LENGTH_SHORT)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .setBackgroundTint(Color.parseColor("#11232E"))
                        .setActionTextColor(Color.parseColor("#FFFFFF")).show();
            }
        } catch (Exception e) {
            //Toast.makeText(this, "TABLA DE SERVIDOR NO ENCONTRADO.", Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.linearLayout7), "TABLA DE SERVIDOR NO ENCONTRADO.", Snackbar.LENGTH_SHORT)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setBackgroundTint(Color.parseColor("#11232E"))
                    .setActionTextColor(Color.parseColor("#FFFFFF")).show();
        }
    }


    public void FaltaSync() {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                Access_Venta1 db = Access_Venta1.getInstance(getApplicationContext());
                db.openWritable();
                Cursor c = db.getFaltaSync();
                if (c.getCount() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        showNotification();
                        //showNewNotification();
                    } else {
                        //showNotification();
                        showNewNotification();
                    }
                }
                db.close();
            } else {
                // Toast.makeText(getApplicationContext().getApplicationContext(), "MOVIL SIN ACCESO A INTERNET\nLa aplicación no podra realizar exportaciones de datos al servidor en estos momentos.", Toast.LENGTH_LONG).show();
                /*Snackbar.make(findViewById(R.id.linearLayout68), "MOVIL SIN ACCESO A INTERNET\nLa aplicación no podra realizar exportaciones de datos al servidor en estos momentos.", Snackbar.LENGTH_LONG)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
                        .setBackgroundTint(Color.parseColor("#FF0000"))
                        .setActionTextColor(Color.parseColor("#C0C0C0")).show();*/
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error buscando ventas por sincronizar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void showNotification() {
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID, "Alerta de Respaldo", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
            showNewNotification();
        }
    }

    private void showNewNotification() {
        //setPendingIntent(MainActivity.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.backup)
                .setContentTitle("ALERTA DE RESPALDO")
                .setContentText("Fact-Express identifica ventas que aun no han sido respaldados al servidor.\n" +
                        "La falta del mismo puede conllevar a perdidas de información.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
        managerCompat.notify(1, builder.build());
    }

    private void setPendingIntent(Class<?> clsActivity) {
        Intent intent = new Intent(this, clsActivity);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(clsActivity);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

    }
}