package com.example.latinodistribuidora.Actividades;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.latinodistribuidora.CRUD.Access_IVA;
import com.example.latinodistribuidora.R;

public class Editar_iva extends AppCompatActivity {
    private int ivaEditar;
    private EditText descripcion, impuesto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_iva);
        Bundle extras = this.getIntent().getExtras();
        if(extras!=null){
            ivaEditar = extras.getInt("idiva");
        }
        descripcion = findViewById(R.id.id_descmod_iva);
        impuesto = findViewById(R.id.id_impu_iva);

        reflejarCampos();
    }

    public void reflejarCampos(){
        Access_IVA db = Access_IVA.getInstance(getApplicationContext());
        db.openReadable();
        Cursor c = db.getIVA_a_Modificar(ivaEditar);
        try {
            if(c.moveToNext()){
                descripcion.setText(c.getString(2));
                descripcion.requestFocus();
                impuesto.setText(c.getString(1));

            }
            db.close();
        }finally {}
    }
    public void Modificar(View v){
        if(descripcion.getText().toString().trim().isEmpty()){
            descripcion.requestFocus();
        }else if(impuesto.getText().toString().trim().isEmpty()){
            impuesto.requestFocus();
        }else{
            Access_IVA db = Access_IVA.getInstance(getApplicationContext());
            db.openWritable();
            ContentValues values = new ContentValues();
            values.put("impuesto",impuesto.getText().toString());
            values.put("descripcion",descripcion.getText().toString());
            long respuesta =db.ActualizarIVA(values, ivaEditar);
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
        Intent i = new Intent(getApplicationContext(), Listar_IVA.class);
        startActivity(i);
        descripcion.setText("");
        impuesto.setText("");
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Listar_IVA.class);
        startActivity(i);
        finish();
    }
}