package com.example.latinodistribuidora.Actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.latinodistribuidora.CRUD.Access_Venta;
import com.example.latinodistribuidora.CRUD.Access_Venta1;
import com.example.latinodistribuidora.MainActivity;
import com.example.latinodistribuidora.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MenuPrincipal extends AppCompatActivity {
    private int idvendedor;
    private String vendedor;
    private TextView txtbienvenida,txtidvendedor, txtvendedor;
    private static final String CHANNEL_ID="canal";
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        txtidvendedor = findViewById(R.id.id_idvendedor);
        txtvendedor = findViewById(R.id.id_vendedor);
        txtbienvenida = findViewById(R.id.id_bienvenido);
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            idvendedor = bundle.getInt("idVendedor");
            vendedor= bundle.getString("Vendedor");
        }
        txtidvendedor.setText(String.valueOf(idvendedor));
        txtvendedor.setText(vendedor);
        txtbienvenida.setText("Bienvenido "+vendedor+", comencemos a trabajar!");

        findViewById(R.id.btnVender).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getApplicationContext(), Listar_ClientesVenta.class);
                i.putExtra("idVendedor",txtidvendedor.getText());
                i.putExtra("Vendedor",txtvendedor.getText());
                startActivity(i);
            }
        });

        findViewById(R.id.btnVenderCosto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getApplicationContext(), Listar_ClientesVentaCosto.class);
                i.putExtra("idVendedor",txtidvendedor.getText());
                i.putExtra("Vendedor",txtvendedor.getText());
                startActivity(i);
            }
        });

        findViewById(R.id.id_btnMant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( getApplicationContext(), Opciones_Mantenimiento.class);
                startActivity(i);
            }
        });

        findViewById(R.id.btnListarVentas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent( getApplicationContext(), Listar_ventas.class);
                    startActivity(i);
            }
        });

        findViewById(R.id.btnProducto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent( getApplicationContext(), Listar_Productos.class);
                    startActivity(i);
            }
        });

        findViewById(R.id.id_btnClient).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent( getApplicationContext(), Listar_Clientes.class);
                    startActivity(i);
            }
        });

    }

    public void onResume() {
        super.onResume();
        FaltaSync();
    }

    public void CerrarSesion(View view){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("¿Esta seguro que desea salir de esta aplicación cerrando sesión por completo?");
        alertDialog.setTitle("CERRAR SESIÓN");
        alertDialog.setIcon(R.drawable.ic_salir);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("SI, SALIR", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                Intent i = new Intent( getApplicationContext(), MainActivity.class);
                startActivity(i);
                txtidvendedor.setText("");
                txtvendedor.setText("");
                txtbienvenida.setText("");
                finish();
            }
        });
        alertDialog.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //Toast.makeText(this, "Una sesión se encuentra iniciada.\n\n", Toast.LENGTH_SHORT).show();
        Snackbar.make(findViewById(R.id.linearLayout26), "UNA SESIÓN SE ENCUETRA INICADA." +
                        "\nCierre Sesión si desea finalizar las operaciones.", Snackbar.LENGTH_SHORT)
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                .setBackgroundTint(Color.parseColor("#11232E"))
                .setActionTextColor(Color.parseColor("#FFFFFF")).show();
    }

    public void FaltaSync() {
        try {
            ConnectivityManager cm =
                    (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if(activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
                Access_Venta1 db = Access_Venta1.getInstance(getApplicationContext());
                db.openWritable();
                Cursor c = db.getFaltaSync();
                if(c.getCount() > 0){
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        showNotification();
                        //showNewNotification();
                    }else{
                        //showNotification();
                        showNewNotification();
                    }
                }
                db.close();
            }else{
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
        //setPendingIntent(Opciones_Mantenimiento.class);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.backup)
                .setContentTitle("ALERTA DE RESPALDO")
                .setContentText("Fact-Express identifica ventas que aun no han sido respaldados al servidor.\n" +
                        "La falta del mismo puede conllevar a perdidas de información.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
        managerCompat.notify(1,builder.build());
    }

    private void setPendingIntent(Class<?> clsActivity){
        Intent intent = new Intent(this, clsActivity);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(clsActivity);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

    }
}