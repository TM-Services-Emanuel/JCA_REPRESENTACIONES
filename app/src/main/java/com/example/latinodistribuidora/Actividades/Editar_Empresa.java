package com.example.latinodistribuidora.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.latinodistribuidora.CRUD.Access_Ciudades;
import com.example.latinodistribuidora.CRUD.Access_Departamentos;
import com.example.latinodistribuidora.CRUD.Access_Empresa;
import com.example.latinodistribuidora.Modelos.Ciudades;
import com.example.latinodistribuidora.Modelos.Departamentos;
import com.example.latinodistribuidora.R;

import java.util.ArrayList;

public class Editar_Empresa extends AppCompatActivity {
    private int empresaEditar;
    private EditText txtrazon, txtruc, txtdirecion, txttelefono;
    private TextView txtIdciudad;
    private TextView txtCiudad;

    Spinner comboCiudades;
    ArrayList<String> listaciu;
    ArrayList<Ciudades> ciulist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_empresa);
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            empresaEditar = extras.getInt("idempresa");
        }
        txtrazon = findViewById(R.id.id_razon_modemp);
        txtruc = findViewById(R.id.id_ruc_modemp);
        txtdirecion = findViewById(R.id.id_direccion_modemp);
        txttelefono = findViewById(R.id.id_telefono_modemp);
        comboCiudades = findViewById(R.id.id_spi_modciudades);
        txtIdciudad= findViewById(R.id.id_idciudad_modemp);
        txtCiudad = findViewById(R.id.id_ciudad_modemp);
        reflejarCampos();
        txtrazon.requestFocus();

        findViewById(R.id.btnModEmpresa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtrazon.getText().toString().trim().isEmpty()){
                    txtrazon.requestFocus();
                }else if(txtruc.getText().toString().trim().isEmpty()){
                    txtruc.requestFocus();
                }else if(txtdirecion.getText().toString().trim().isEmpty()){
                    txtdirecion.requestFocus();
                }else if(txttelefono.getText().toString().trim().isEmpty()){
                    txttelefono.requestFocus();
                }else if(txtIdciudad.getText().toString().equals("0")){
                    Toast.makeText(getApplicationContext(),"Seleccione una ciudad",Toast.LENGTH_SHORT).show();
                }else{
                    Access_Empresa db = Access_Empresa.getInstance(getApplicationContext());
                    db.openWritable();
                    ContentValues values = new ContentValues();
                    values.put("razon_social",txtrazon.getText().toString());
                    values.put("ruc",txtruc.getText().toString());
                    values.put("direccion",txtdirecion.getText().toString());
                    values.put("telefono",txttelefono.getText().toString());
                    values.put("ciudad_idciudad", txtIdciudad.getText().toString());
                    long respuesta =db.ActualizarEmpresa(values, empresaEditar);
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
        Access_Empresa db = Access_Empresa.getInstance(getApplicationContext());
        db.openReadable();
        Cursor c = db.getEmpresa_a_Modificar(empresaEditar);
        try {
            if(c.moveToNext()){
                txtrazon.setText(c.getString(1));
                txtruc.setText(c.getString(2));
                txtdirecion.setText(c.getString(3));
                txttelefono.setText(c.getString(4));
                txtCiudad.setText(c.getString(6));
                consultarlistaciudades();
                ArrayAdapter<String> adaptador = new ArrayAdapter(this, R.layout.spinner_item_ld,listaciu);
                comboCiudades.setAdapter(adaptador);
                int index= getIndexSpinner(comboCiudades, txtCiudad.getText().toString());
                comboCiudades.setSelection(index);
                comboCiudades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        try{
                            if(position!=0){
                                txtIdciudad.setText(String.valueOf(ciulist.get(position-1).getIdciudad()));
                            }else{
                                txtIdciudad.setText("0");
                            }
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), "error: "+e.getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }finally {}
    }


    public void Limpiar_y_volver_a_Lista(){
        Intent i = new Intent(getApplicationContext(), Listar_Empresa.class);
        startActivity(i);
        txtrazon.setText("");
        txtruc.setText("");
        txtdirecion.setText("");
        txttelefono.setText("");
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Listar_Empresa.class);
        startActivity(i);
        finish();
    }

    private void consultarlistaciudades() {
        Access_Ciudades db = Access_Ciudades.getInstance(getApplicationContext());
        db.openReadable();
        Ciudades ciudades=null;
        ciulist = new ArrayList<Ciudades>();
        Cursor cursor = db.getCiudades();
        while (cursor.moveToNext()){
            ciudades = new Ciudades();
            ciudades.setIdciudad(cursor.getInt(0));
            ciudades.setCiudad(cursor.getString(1));

            Log.i("id:", String.valueOf(ciudades.getIdciudad()));
            Log.i("ciudad:",ciudades.getCiudad().toString());

            ciulist.add(ciudades);
        }
        obtenerlista();
    }

    private void obtenerlista(){
        listaciu = new ArrayList<String>();
        listaciu.add("Seleccione una ciudad");
        for(int i=0; i < ciulist.size();i++) {
            listaciu.add(ciulist.get(i).getCiudad().toString());
        }
    }

    public static int getIndexSpinner(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
            }
        }
        return index;
    }
}