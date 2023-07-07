package com.example.latinodistribuidora.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.latinodistribuidora.CRUD.Access_Departamentos;
import com.example.latinodistribuidora.CRUD.Access_Usuarios;
import com.example.latinodistribuidora.R;

public class Editar_Departamento extends AppCompatActivity {
    private int departamentoEditar;
    private EditText departamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_departamento);
        Bundle extras = this.getIntent().getExtras();
        if(extras!=null){
            departamentoEditar = extras.getInt("iddepartamento");
        }
        departamento = findViewById(R.id.id_depmodifc);

        reflejarCampos();

        findViewById(R.id.id_btnmoddepart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(departamento.getText().toString().trim().isEmpty()){
                    departamento.requestFocus();
                }else{
                    Access_Departamentos db = Access_Departamentos.getInstance(getApplicationContext());
                    db.openWritable();
                    ContentValues values = new ContentValues();
                    values.put("departamento",departamento.getText().toString());
                    long respuesta =db.ActualizarDepartamento(values, departamentoEditar);
                    db.close();
                    if(respuesta >0){
                        Toast.makeText(getApplicationContext(), "Modificación realizada exitosamente", Toast.LENGTH_LONG).show();
                        Limpiar_y_volver_a_Lista();
                    }else{
                        Toast.makeText(getApplicationContext(), "Ocurrio un error", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    public void reflejarCampos(){
        Access_Departamentos db = Access_Departamentos.getInstance(getApplicationContext());
        db.openReadable();
        Cursor c = db.getDepartamento_a_Modificar(departamentoEditar);
        try {
            if(c.moveToNext()){
                departamento.setText(c.getString(1));
                departamento.requestFocus();

            }
        }finally {}
    }

    public void Limpiar_y_volver_a_Lista(){
        Intent i = new Intent(getApplicationContext(), Listar_Departamento.class);
        startActivity(i);
        departamento.setText("");
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Listar_Departamento.class);
        startActivity(i);
        finish();
    }
}