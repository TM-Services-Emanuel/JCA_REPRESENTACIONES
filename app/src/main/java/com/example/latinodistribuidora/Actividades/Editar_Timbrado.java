package com.example.latinodistribuidora.Actividades;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.example.latinodistribuidora.CRUD.Access_Timbrado;
import com.example.latinodistribuidora.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import static com.example.latinodistribuidora.R.color.*;

public class Editar_Timbrado extends AppCompatActivity {
    private int timbradoEditar;
    private TextView timbrado, desde, hasta;
    private TextView  est;
    public Switch switchE;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_timbrado);
        Bundle extras = this.getIntent().getExtras();
        if(extras!=null){
            timbradoEditar = extras.getInt("idtimbrado");
        }
        timbrado = (TextView) findViewById(R.id.id_timb_mod);
        desde = (TextView) findViewById(R.id.id_desde_mod);
        hasta = (TextView) findViewById(R.id.id_hasta_mod);
        est = (TextView) findViewById(R.id.id_estado_modif);
        switchE = findViewById(R.id.id_switch_mod);

        reflejarCampos();
        if(est.getText().toString().equals("Activo")){
            switchE.setChecked(true);
            est.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }else{
            switchE.setChecked(false);
            est.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }

        findViewById(R.id.id_btnMT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(timbrado.getText().toString().trim().isEmpty()){
                    timbrado.requestFocus();
                }else if(desde.getText().toString().trim().isEmpty()){
                    desde.requestFocus();
                }else if(hasta.getText().toString().trim().isEmpty()){
                    hasta.requestFocus();
                }else{
                    Access_Timbrado db = Access_Timbrado.getInstance(getApplicationContext());
                    db.openWritable();
                    ContentValues values = new ContentValues();
                    values.put("descripcion",timbrado.getText().toString());
                    values.put("fechadesde",desde.getText().toString());
                    values.put("fechahasta",hasta.getText().toString());
                    values.put("estado",est.getText().toString());
                    long respuesta =db.ActualizarTimbrado(values,timbradoEditar);
                    db.close();
                    if(respuesta >0){
                        timbrado.setEnabled(false);
                        desde.setEnabled(false);
                        hasta.setEnabled(false);
                        switchE.setEnabled(false);
                        //Toast.makeText(getApplicationContext(), "Modificación realizada exitosamente", Toast.LENGTH_LONG).show();
                        Snackbar.make(findViewById(R.id.linearLayout80), "MODIFICACION REALIZADA EXITOSAMENTE!.", Snackbar.LENGTH_INDEFINITE)
                                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                                .setBackgroundTint(Color.parseColor("#11232E"))
                                .setActionTextColor(Color.parseColor("#FFFFFF")).setAction("VOLVER", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Limpiar_y_volver_a_Lista();
                                    }
                                }).show();

                    }else{
                        //Toast.makeText(getApplicationContext(), "Ocurrio un error", Toast.LENGTH_LONG).show();
                        Snackbar.make(findViewById(R.id.linearLayout80), "Error modificando timbrado.", Snackbar.LENGTH_SHORT)
                                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                                .setBackgroundTint(Color.parseColor("#11232E"))
                                .setActionTextColor(Color.parseColor("#FFFFFF")).show();
                    }
                }
            }
        });
    }

    public void onClick(View view){
        if(view.getId()==R.id.id_switch_mod){
            if(switchE.isChecked()){
                est.setText("Activo");
                est.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            }else{
                est.setText("Inactivo");
                est.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }
        }
    }

    public void reflejarCampos(){
        Access_Timbrado db = Access_Timbrado.getInstance(getApplicationContext());
        db.openReadable();
        Cursor c = db.getTimbrado_a_Modificar(timbradoEditar);
        try {
            if(c.moveToNext()){
                timbrado.setText(c.getString(1));
                timbrado.requestFocus();
                desde.setText(c.getString(2));
                hasta.setText(c.getString(3));
                est.setText(c.getString(4));

            }
            db.close();
        }finally {}
    }

    public void Limpiar_y_volver_a_Lista(){
        Intent i = new Intent(getApplicationContext(), Listar_Timbrado.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Listar_Timbrado.class);
        startActivity(i);
        finish();
    }
}