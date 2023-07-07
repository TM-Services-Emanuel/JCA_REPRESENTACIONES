package com.example.latinodistribuidora.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.latinodistribuidora.CRUD.Access_Division;
import com.example.latinodistribuidora.CRUD.Access_Timbrado;
import com.example.latinodistribuidora.R;

public class Registar_Timbrado extends AppCompatActivity {
    public static EditText timbrado, desde, hasta;
    public TextView est;
    public Switch switchE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registar_timbrado);
        timbrado = findViewById(R.id.id_timb_crear);
        desde = findViewById(R.id.id_desde_crear);
        hasta = findViewById(R.id.id_hasta_crear);
        est = findViewById(R.id.id_estado_crear);
        est.setText("Activo");
        switchE = findViewById(R.id.id_switch_crear);

    }
    public void onClick(View view){
        if(view.getId()==R.id.id_switch_crear){
            if(switchE.isChecked()){
                est.setText("Activo");
            }else{
                est.setText("Inactivo");
            }
        }
    }

    public void btn_registrar(View v){
        if (timbrado.getText().toString().trim().isEmpty()) {
            timbrado.requestFocus();
        }else if (desde.getText().toString().trim().isEmpty()) {
            desde.requestFocus();
        }else if (hasta.getText().toString().trim().isEmpty()) {
            hasta.requestFocus();
        }  else {
            try {
                Access_Timbrado db = Access_Timbrado.getInstance(getApplicationContext());
                db.openWritable();
                long insertado= db.insertarTimbrado(timbrado.getText().toString(),desde.getText().toString(),hasta.getText().toString());
                db.close();
                if(insertado>0){
                    Toast.makeText(getApplicationContext(),"Timbrado registrado exitosamente",Toast.LENGTH_SHORT).show();
                    Limpiar_y_volver_a_Principal();
                }else{
                    Toast.makeText(getApplicationContext(),"No se pudo registra el timbrado",Toast.LENGTH_SHORT).show();
                }

            } catch (Exception E) {

                Toast.makeText(getApplicationContext(), "Error fatal: " + E.getMessage(), Toast.LENGTH_LONG).show();

            }
        }
    }
    public void Limpiar_y_volver_a_Principal(){
        timbrado.setText("");
        desde.setText("");
        hasta.setText("");

        Intent i = new Intent(getApplicationContext(), Listar_Timbrado.class);
        startActivity(i);
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Listar_Timbrado.class);
        startActivity(i);
    }
}