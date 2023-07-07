package com.example.latinodistribuidora.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.latinodistribuidora.CRUD.Access_PE;
import com.example.latinodistribuidora.CRUD.Access_Timbrado;
import com.example.latinodistribuidora.R;

public class Registrar_PE extends AppCompatActivity {
    public static EditText establecimiento,pe,direccion, desde, hasta;
    public TextView est;
    public Switch switchE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_pe);
        establecimiento = findViewById(R.id.id_estable_crear);
        pe = findViewById(R.id.id_pe_crear);
        direccion = findViewById(R.id.id_dir_crear);
        desde = findViewById(R.id.id_inicio_crear);
        hasta = findViewById(R.id.id_fin_crear);
        est = findViewById(R.id.id_est_pe_crear);
        est.setText("Activo");
        switchE = findViewById(R.id.id_switchPE_crear);
    }

    public void onClick(View view){
        if(view.getId()==R.id.id_switchPE_crear){
            if(switchE.isChecked()){
                est.setText("Activo");
            }else{
                est.setText("Inactivo");
            }
        }
    }

    public void btn_registrar(View v){
        if (establecimiento.getText().toString().trim().isEmpty()) {
            establecimiento.requestFocus();
        }else if (pe.getText().toString().trim().isEmpty()) {
            pe.requestFocus();
        }else if (direccion.getText().toString().trim().isEmpty()) {
            direccion.requestFocus();
        }else if (desde.getText().toString().trim().isEmpty()) {
            desde.requestFocus();
        }else if (hasta.getText().toString().trim().isEmpty()) {
            hasta.requestFocus();
        }  else {
            try {
                Access_PE db = Access_PE.getInstance(getApplicationContext());
                db.openWritable();
                long insertado= db.insertarPE(establecimiento.getText().toString(),pe.getText().toString(),
                        direccion.getText().toString(),Integer.parseInt(desde.getText().toString()),Integer.parseInt(hasta.getText().toString()));
                db.close();
                if(insertado>0){
                    Toast.makeText(getApplicationContext(),"Punto de emisión registrado exitosamente",Toast.LENGTH_SHORT).show();
                    Limpiar_y_volver_a_Principal();
                }else{
                    Toast.makeText(getApplicationContext(),"No se pudo registra el punto de emisión",Toast.LENGTH_SHORT).show();
                }

            } catch (Exception E) {

                Toast.makeText(getApplicationContext(), "Error fatal: " + E.getMessage(), Toast.LENGTH_LONG).show();

            }
        }
    }
    public void Limpiar_y_volver_a_Principal(){
        establecimiento.setText("");
        pe.setText("");
        direccion.setText("");
        desde.setText("");
        hasta.setText("");

        Intent i = new Intent(getApplicationContext(), Listar_PE.class);
        startActivity(i);
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Listar_PE.class);
        startActivity(i);
    }
}