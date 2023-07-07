package com.example.latinodistribuidora.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.latinodistribuidora.CRUD.Access_Ciudades;
import com.example.latinodistribuidora.CRUD.Access_Departamentos;
import com.example.latinodistribuidora.Modelos.Departamentos;
import com.example.latinodistribuidora.R;

import java.util.ArrayList;

public class Registrar_Ciudad extends AppCompatActivity {
    Spinner comboDepartamentos;
    TextView txtIddepartamento, txtCiudad;
    ArrayList<String> listadep;
    ArrayList<Departamentos> deplist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_ciudad);
        comboDepartamentos = findViewById(R.id.id_spi_dep);
        txtCiudad = findViewById(R.id.id_reg_ciu);
        txtIddepartamento = findViewById(R.id.id_iddepartamento);
        txtCiudad.requestFocus();
        consultarlistadepartamentos();
        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter(this, R.layout.spinner_item_ld,listadep);
        comboDepartamentos.setAdapter(adaptador);
        comboDepartamentos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{
                    if(position!=0){
                        txtIddepartamento.setText(String.valueOf(deplist.get(position-1).getIddepartamento()));
                    }else{
                        txtIddepartamento.setText("0");
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

    private void consultarlistadepartamentos() {
        Access_Departamentos db = Access_Departamentos.getInstance(getApplicationContext());
        db.openReadable();
        Departamentos departamentos=null;
        deplist = new ArrayList<Departamentos>();
        Cursor cursor = db.getDepartamentos();
        while (cursor.moveToNext()){
            departamentos = new Departamentos();
            departamentos.setIddepartamento(cursor.getInt(0));
            departamentos.setDepartamento(cursor.getString(1));

            Log.i("id:", String.valueOf(departamentos.getIddepartamento()));
            Log.i("departamento:",departamentos.getDepartamento().toString());

            deplist.add(departamentos);
        }
        obtenerlista();
    }

    private void obtenerlista(){
        listadep = new ArrayList<String>();
        listadep.add("Seleccione un departamento");
        for(int i=0; i < deplist.size();i++){
            listadep.add(deplist.get(i).getDepartamento().toString());
        }
    }

    public void Guardar(View view){
        if(txtCiudad.getText().toString().trim().isEmpty()){
            txtCiudad.requestFocus();
        }else if (txtIddepartamento.getText().toString().equals("0")){
            Toast.makeText(getApplicationContext(),"Seleccione un departamento",Toast.LENGTH_SHORT).show();
        }else{
            Access_Ciudades db = Access_Ciudades.getInstance(getApplicationContext());
            db.openWritable();
            long insertado = db.insertarCiudad(txtCiudad.getText().toString(),Integer.parseInt(txtIddepartamento.getText().toString()));
            db.close();
            if(insertado>0){
                Toast.makeText(getApplicationContext(),"Ciudad registrado exitosamente",Toast.LENGTH_SHORT).show();
                Limpiar_y_volver_a_Principal();
            }else{
                Toast.makeText(getApplicationContext(),"No se pudo registra la ciudad",Toast.LENGTH_SHORT).show();
            }
            }

        }
    private void Limpiar_y_volver_a_Principal(){
        txtCiudad.setText("");
        comboDepartamentos.setSelection(0);
        txtIddepartamento.setText("0");

        Intent i = new Intent(getApplicationContext(), Listar_Ciudad.class);
        startActivity(i);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Listar_Ciudad.class);
        startActivity(i);
    }
    }
