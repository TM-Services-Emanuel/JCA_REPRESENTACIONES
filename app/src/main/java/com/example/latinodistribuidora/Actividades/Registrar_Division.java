package com.example.latinodistribuidora.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.latinodistribuidora.CRUD.Access_Division;
import com.example.latinodistribuidora.R;

public class Registrar_Division extends AppCompatActivity {
    public static EditText division;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_division);
        division = findViewById(R.id.id_division);
    }

    public void btn_registrar(View v){
        if (division.getText().toString().trim().isEmpty()) {
            division.requestFocus();
        }  else {
            try {
                Access_Division db = Access_Division.getInstance(getApplicationContext());
                db.openWritable();
                long insertado= db.insertarDivision(division.getText().toString());
                db.close();
                if(insertado>0){
                    Toast.makeText(getApplicationContext(),"Clasificación registrado exitosamente",Toast.LENGTH_SHORT).show();
                    Limpiar_y_volver_a_Principal();
                }else{
                    Toast.makeText(getApplicationContext(),"No se pudo registra la clasificación",Toast.LENGTH_SHORT).show();
                }

            } catch (Exception E) {

                Toast.makeText(getApplicationContext(), "Error fatal: " + E.getMessage(), Toast.LENGTH_LONG).show();

            }
        }
    }
    public void Limpiar_y_volver_a_Principal(){
        division.setText("");

        Intent i = new Intent(getApplicationContext(), Listar_Division.class);
        startActivity(i);
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Listar_Division.class);
        startActivity(i);
    }
}