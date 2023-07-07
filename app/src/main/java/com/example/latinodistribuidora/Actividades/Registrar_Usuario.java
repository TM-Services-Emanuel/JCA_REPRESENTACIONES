package com.example.latinodistribuidora.Actividades;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.latinodistribuidora.CRUD.Access_Usuarios;
import com.example.latinodistribuidora.MainActivity;
import com.example.latinodistribuidora.R;

public class Registrar_Usuario extends AppCompatActivity {
    public static EditText nombre;
    public static EditText ci;
    public static EditText direccion;
    public static EditText celular;
    public static EditText contrasena;
    public static EditText confirmar_contrasena;
    public static EditText usuario;
    //public Button btnRegistrar;
    //public Button btnListar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);

        nombre = findViewById(R.id.id_nombre_registrar);
        ci = findViewById(R.id.id_cedula_registrar);
        direccion = findViewById(R.id.id_direccion_registrar);
        celular = findViewById(R.id.id_celular_registrar);
        contrasena = findViewById(R.id.id_contrasena);
        confirmar_contrasena = findViewById(R.id.id_confirmarcontrasena);
        usuario = findViewById(R.id.id_usuario_registrar);
        }

    public void btn_registrar(View v){
        if (nombre.getText().toString().trim().isEmpty()) {
            nombre.requestFocus();
        } else if (ci.getText().toString().trim().isEmpty()) {
            ci.requestFocus();
        } else if (direccion.getText().toString().trim().isEmpty()) {
            direccion.requestFocus();
        } else if (celular.getText().toString().trim().isEmpty()) {
            celular.requestFocus();
        } else if (usuario.getText().toString().trim().isEmpty()) {
            usuario.requestFocus();
        } else if (contrasena.getText().toString().trim().isEmpty()) {
            contrasena.requestFocus();
        } else if (confirmar_contrasena.getText().toString().trim().isEmpty()) {
            confirmar_contrasena.requestFocus();
        } else if (!contrasena.getText().toString().equals(confirmar_contrasena.getText().toString())) {
            Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden, por favor vuelva a verificarlos", Toast.LENGTH_LONG).show();
            confirmar_contrasena.requestFocus();
        } else {
            try {
                Access_Usuarios db = Access_Usuarios.getInstance(getApplicationContext());
                db.openWritable();
                long insertado= db.insertarUsuario(nombre.getText().toString(), ci.getText().toString(), direccion.getText().toString(),
                        celular.getText().toString(), usuario.getText().toString(), contrasena.getText().toString());
                db.close();
                if(insertado>0){
                    Toast.makeText(getApplicationContext(),"Usuario registrado exitosamente",Toast.LENGTH_SHORT).show();
                    Limpiar_y_volver_a_Principal();
                }else{
                    Toast.makeText(getApplicationContext(),"No se pudo registra el usuario",Toast.LENGTH_SHORT).show();
                }

            } catch (Exception E) {

                Toast.makeText(getApplicationContext(), "Error fatal: " + E.getMessage(), Toast.LENGTH_LONG).show();

            }
        }
    }
    public void Limpiar_y_volver_a_Principal(){
        nombre.setText("");
        ci.setText("");
        direccion.setText("");
        celular.setText("");
        usuario.setText("");
        contrasena.setText("");
        confirmar_contrasena.setText("");

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        this.finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Registrar_Usuario.this, MainActivity.class);
        startActivity(i);
    }
}