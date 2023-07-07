package com.example.latinodistribuidora.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.latinodistribuidora.CRUD.Access_Division;
import com.example.latinodistribuidora.CRUD.Access_Servidor;
import com.example.latinodistribuidora.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class Servidor extends AppCompatActivity {
    private EditText ipServidor;
    private String IP;
    private Button actualizarIP;
    private String newIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servidor);
        ipServidor = (EditText) findViewById(R.id.txt_ipservidor);
        actualizarIP = (Button) findViewById(R.id.btnActualizarIP);
        try{
            Access_Servidor db = Access_Servidor.getInstance(getApplicationContext());
            Cursor cursor= db.getServidor();
            if(cursor.moveToNext()) {
                db.close();
                IP = cursor.getString(1);
                ipServidor.setText(IP.trim().toString());
            }
        }catch (Exception e){
            //Toast.makeText(this, "TABLA DE SERVIDOR NO ENCONTRADO.", Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(R.id.linearLayout89), "TABLA DE SERVIDOR NO ENCONTRADO.", Snackbar.LENGTH_SHORT)
                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                    .setBackgroundTint(Color.parseColor("#11232E"))
                    .setActionTextColor(Color.parseColor("#FFFFFF")).show();
        }

        actualizarIP.setOnClickListener(v -> {
            if(ipServidor.getText().toString().trim().isEmpty()){
                Access_Servidor db = Access_Servidor.getInstance(getApplicationContext());
                db.openWritable();
                ContentValues values = new ContentValues();
                values.put("ip","127.0.0.1");
                long respuesta =db.ActualizarServidor(values);
                db.close();
                if(respuesta >0){
                    ipServidor.setEnabled(false);
                    //Toast.makeText(getApplicationContext(), "Modificación realizada exitosamente", Toast.LENGTH_LONG).show();
                    Snackbar.make(findViewById(R.id.linearLayout89), "Modificación realizada exitosamente!.", Snackbar.LENGTH_INDEFINITE)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .setBackgroundTint(Color.parseColor("#11232E"))
                            .setActionTextColor(Color.parseColor("#FFFFFF"))
                            .setAction("VOLVER", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Limpiar_y_volver_a_Opciones();
                                }
                            })
                            .show();
                }else{
                    //Toast.makeText(getApplicationContext(), "Ocurrio un error", Toast.LENGTH_LONG).show();
                    Snackbar.make(findViewById(R.id.linearLayout89), "Error modificando IP.", Snackbar.LENGTH_SHORT)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .setBackgroundTint(Color.parseColor("#11232E"))
                            .setActionTextColor(Color.parseColor("#FFFFFF")).show();
                }
            }else{
                Access_Servidor db = Access_Servidor.getInstance(getApplicationContext());
                db.openWritable();
                ContentValues values = new ContentValues();
                values.put("ip",ipServidor.getText().toString().trim());
                long respuesta =db.ActualizarServidor(values);
                db.close();
                if(respuesta >0){
                    ipServidor.setEnabled(false);
                    Snackbar.make(findViewById(R.id.linearLayout89), "Modificación realizada exitosamente!.", Snackbar.LENGTH_INDEFINITE)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .setBackgroundTint(Color.parseColor("#11232E"))
                            .setActionTextColor(Color.parseColor("#FFFFFF"))
                            .setAction("VOLVER", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Limpiar_y_volver_a_Opciones();
                                }
                            })
                            .show();
                }else{
                    Snackbar.make(findViewById(R.id.linearLayout89), "Error modificando IP.", Snackbar.LENGTH_SHORT)
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .setBackgroundTint(Color.parseColor("#11232E"))
                            .setActionTextColor(Color.parseColor("#FFFFFF")).show();
                }
            }


        });
    }

    public void Limpiar_y_volver_a_Opciones(){
        Intent i = new Intent(getApplicationContext(), Opciones_Mantenimiento.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Opciones_Mantenimiento.class);
        startActivity(i);
        finish();
    }
}