package com.example.latinodistribuidora.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.latinodistribuidora.CRUD.Access_Departamentos;
import com.example.latinodistribuidora.CRUD.Access_Usuarios;
import com.example.latinodistribuidora.MainActivity;
import com.example.latinodistribuidora.R;

public class Registrar_Departamento extends AppCompatActivity {
    public static EditText departamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_departamento);
        departamento = findViewById(R.id.id_newdepart);
    }
    public void btn_registrar(View v){
        if (departamento.getText().toString().trim().isEmpty()) {
            departamento.requestFocus();
        }  else {
            try {
                Access_Departamentos db = Access_Departamentos.getInstance(getApplicationContext());
                db.openWritable();
                long insertado= db.insertarDepartamento(departamento.getText().toString());
                db.close();
                if(insertado>0){
                    Toast.makeText(getApplicationContext(),"Departamento registrado exitosamente",Toast.LENGTH_SHORT).show();
                    Limpiar_y_volver_a_Principal();
                }else{
                    Toast.makeText(getApplicationContext(),"No se pudo registra el departamento",Toast.LENGTH_SHORT).show();
                }

            } catch (Exception E) {

                Toast.makeText(getApplicationContext(), "Error fatal: " + E.getMessage(), Toast.LENGTH_LONG).show();

            }
        }
    }
    public void Limpiar_y_volver_a_Principal(){
        departamento.setText("");

        Intent i = new Intent(getApplicationContext(), Listar_Departamento.class);
        startActivity(i);
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Listar_Departamento.class);
        startActivity(i);
    }
}