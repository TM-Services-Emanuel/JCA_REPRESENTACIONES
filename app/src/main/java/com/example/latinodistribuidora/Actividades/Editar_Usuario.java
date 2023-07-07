package com.example.latinodistribuidora.Actividades;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.latinodistribuidora.CRUD.Access_Usuarios;
import com.example.latinodistribuidora.R;

public class Editar_Usuario extends AppCompatActivity {
    private int usuarioEditar;
    private EditText nombre,ci,direccion,celular,usuario,contrasena,confirmarcontrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);
        Bundle extras = this.getIntent().getExtras();
        if(extras!=null){
            usuarioEditar = extras.getInt("idusuario");
        }
        nombre = findViewById(R.id.id_nombre_modificar);
        ci = findViewById(R.id.id_cedula_modificar);
        direccion = findViewById(R.id.id_direccion_modificar);
        celular = findViewById(R.id.id_celular_modificar);
        usuario = findViewById(R.id.id_usuario_modificar);
        contrasena = findViewById(R.id.id_contrasena_modificar);
        confirmarcontrasena = findViewById(R.id.id_confirmarcontrasena_modificar);

        reflejarCampos();
    }

    public void reflejarCampos(){
        Access_Usuarios db = Access_Usuarios.getInstance(getApplicationContext());
        db.openReadable();
        Cursor c = db.getUsuario_a_modificar(usuarioEditar);
        try {
            if(c.moveToNext()){
                nombre.setText(c.getString(1));
                ci.setText(c.getString(2));
                direccion.setText(c.getString(3));
                celular.setText(c.getString(4));
                usuario.setText(c.getString(5));
                contrasena.setText(c.getString(6));
                confirmarcontrasena.setText(c.getString(6));
            }
        }finally {}
    }

    public void Modificar(View v){
        if(nombre.getText().toString().trim().isEmpty()){
            nombre.requestFocus();
        }else if(ci.getText().toString().trim().isEmpty()){
            ci.requestFocus();
        }else if(direccion.getText().toString().trim().isEmpty()){
            direccion.requestFocus();
        }else if(celular.getText().toString().trim().isEmpty()){
            celular.requestFocus();
        }else if(usuario.getText().toString().trim().isEmpty()){
            usuario.requestFocus();
        }else if(contrasena.getText().toString().trim().isEmpty()){
            contrasena.requestFocus();
        }else if(confirmarcontrasena.getText().toString().trim().isEmpty()){
            confirmarcontrasena.requestFocus();
        }else if(!contrasena.getText().toString().equals(confirmarcontrasena.getText().toString())){
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
            confirmarcontrasena.requestFocus();
        }else{
            Access_Usuarios db = Access_Usuarios.getInstance(getApplicationContext());
            db.openWritable();
            ContentValues values = new ContentValues();
            values.put("nombre",nombre.getText().toString());
            values.put("ci",ci.getText().toString());
            values.put("direccion",direccion.getText().toString());
            values.put("telefono",celular.getText().toString());
            values.put("usuario",usuario.getText().toString());
            values.put("contrasena",contrasena.getText().toString());
            long respuesta =db.ActualizarUsuario(values, usuarioEditar);
            db.close();
            if(respuesta >0){
                Toast.makeText(this, "Modificación realizada exitosamente", Toast.LENGTH_LONG).show();
                Limpiar_y_volver_a_Lista();
            }else{
                Toast.makeText(this, "Ocurrio un error", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void Limpiar_y_volver_a_Lista(){
        Intent i = new Intent(getApplicationContext(), Listar_Usuario.class);
        startActivity(i);
        nombre.setText("");
        ci.setText("");
        direccion.setText("");
        celular.setText("");
        usuario.setText("");
        contrasena.setText("");
        confirmarcontrasena.setText("");
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Listar_Usuario.class);
        startActivity(i);
        finish();
    }
}