package com.example.latinodistribuidora.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.latinodistribuidora.CRUD.Access_IVA;
import com.example.latinodistribuidora.R;

public class Registrar_iva extends AppCompatActivity {
    public static EditText descripcion,impuesto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_iva);
        descripcion = findViewById(R.id.id_descmod_iva);
        impuesto = findViewById(R.id.id_impu_iva);
    }

    public void btn_registrar(View v){
        if (descripcion.getText().toString().trim().isEmpty()) {
            descripcion.requestFocus();
        }else if (impuesto.getText().toString().trim().isEmpty()) {
            impuesto.requestFocus();
        }else {
            try {
                Access_IVA db = Access_IVA.getInstance(getApplicationContext());
                db.openWritable();
                long insertado= db.insertarIVA(Integer.parseInt(impuesto.getText().toString()), descripcion.getText().toString());
                db.close();
                if(insertado>0){
                    Toast.makeText(getApplicationContext(),"I.V.A. registrado exitosamente",Toast.LENGTH_SHORT).show();
                    Limpiar_y_volver_a_Principal();
                }else{
                    Toast.makeText(getApplicationContext(),"No se pudo registra el I.V.A.",Toast.LENGTH_SHORT).show();
                }

            } catch (Exception E) {

                Toast.makeText(getApplicationContext(), "Error fatal: " + E.getMessage(), Toast.LENGTH_LONG).show();

            }
        }
    }
    public void Limpiar_y_volver_a_Principal(){
        descripcion.setText("");
        impuesto.setText("");

        Intent i = new Intent(getApplicationContext(), Listar_IVA.class);
        startActivity(i);
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Listar_IVA.class);
        startActivity(i);
    }
}