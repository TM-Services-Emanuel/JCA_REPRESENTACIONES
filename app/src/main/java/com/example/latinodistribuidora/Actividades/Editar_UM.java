package com.example.latinodistribuidora.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.latinodistribuidora.CRUD.Access_IVA;
import com.example.latinodistribuidora.CRUD.Access_UnicadMedida;
import com.example.latinodistribuidora.Modelos.IVA;
import com.example.latinodistribuidora.Modelos.UnidadMedida;
import com.example.latinodistribuidora.R;

import java.util.ArrayList;

public class Editar_UM extends AppCompatActivity {
    private int umEditar;
    private EditText um, cant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_um);
        Bundle extras = this.getIntent().getExtras();
        if(extras!=null){
            umEditar = extras.getInt("idunidad");
        }
        um = findViewById(R.id.id_mod_um);
        cant = findViewById(R.id.id_mod_cant_um);

        reflejarCampos();
    }

    public void reflejarCampos(){
        Access_UnicadMedida db = Access_UnicadMedida.getInstance(getApplicationContext());
        db.openReadable();
        Cursor c = db.getUnidadMedida_a_Modificar(umEditar);
        try {
            if(c.moveToNext()){
                um.setText(c.getString(1));
                um.requestFocus();
                cant.setText(c.getString(2));

            }
            db.close();
        }finally {}
    }
    public void Modificar(View v){
        if(um.getText().toString().trim().isEmpty()){
            um.requestFocus();
        }else if(cant.getText().toString().trim().isEmpty()){
            cant.requestFocus();
        }else{
            Access_UnicadMedida db = Access_UnicadMedida.getInstance(getApplicationContext());
            db.openWritable();
            ContentValues values = new ContentValues();
            values.put("unidadmedida",um.getText().toString());
            values.put("cantidad",cant.getText().toString());
            long respuesta =db.ActualizarUnidadMedida(values, umEditar);
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
        Intent i = new Intent(getApplicationContext(), ListarUM.class);
        startActivity(i);
        um.setText("");
        cant.setText("");
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, ListarUM.class);
        startActivity(i);
        finish();
    }
}