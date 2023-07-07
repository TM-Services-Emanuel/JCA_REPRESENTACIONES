package com.example.latinodistribuidora.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.latinodistribuidora.CRUD.Access_IVA;
import com.example.latinodistribuidora.CRUD.Access_UnicadMedida;
import com.example.latinodistribuidora.R;

public class Registrar_UM extends AppCompatActivity {
    public static EditText um,cant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_um);
        um = findViewById(R.id.id_um);
        cant = findViewById(R.id.id_cant_um);
    }

    public void btn_registrar(View v){
        if (um.getText().toString().trim().isEmpty()) {
            um.requestFocus();
        }else if (cant.getText().toString().trim().isEmpty()) {
            cant.requestFocus();
        }else {
            try {
                Access_UnicadMedida db = Access_UnicadMedida.getInstance(getApplicationContext());
                db.openWritable();
                long insertado= db.insertarUnidadMedida(um.getText().toString(), Integer.parseInt(cant.getText().toString()));
                db.close();
                if(insertado>0){
                    Toast.makeText(getApplicationContext(),"Unidad de medida registrado exitosamente",Toast.LENGTH_SHORT).show();
                    Limpiar_y_volver_a_Principal();
                }else{
                    Toast.makeText(getApplicationContext(),"No se pudo registra la unidad de medida",Toast.LENGTH_SHORT).show();
                }

            } catch (Exception E) {

                Toast.makeText(getApplicationContext(), "Error fatal: " + E.getMessage(), Toast.LENGTH_LONG).show();

            }
        }
    }
    public void Limpiar_y_volver_a_Principal(){
        um.setText("");
        cant.setText("");

        Intent i = new Intent(getApplicationContext(), ListarUM.class);
        startActivity(i);
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, ListarUM.class);
        startActivity(i);
    }
}