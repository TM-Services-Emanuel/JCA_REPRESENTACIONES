package com.example.latinodistribuidora.Actividades;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.latinodistribuidora.CRUD.Access_Ciudades;
import com.example.latinodistribuidora.CRUD.Access_Departamentos;
import com.example.latinodistribuidora.Modelos.Departamentos;
import com.example.latinodistribuidora.R;

import java.util.ArrayList;

public class Editar_Ciudad extends AppCompatActivity {
    private int ciudadEditar;
    private EditText txtCiudad;
    private TextView txtIddepartamento;
    private TextView txtDepartamento;

    Spinner comboDepartamentos;
    ArrayList<String> listadep;
    ArrayList<Departamentos> deplist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_ciudad);
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            ciudadEditar = extras.getInt("idciudad");
        }
        txtCiudad = findViewById(R.id.id_mod_ciu);
        comboDepartamentos = findViewById(R.id.id_spi_dep);
        txtIddepartamento = findViewById(R.id.id_iddepartamento_mod);
        txtDepartamento = findViewById(R.id.id_departamento_mod);
        reflejarCampos();
        txtCiudad.requestFocus();

        findViewById(R.id.id_btnmodif_ciu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(txtCiudad.getText().toString().trim().isEmpty()){
                        txtCiudad.requestFocus();
                    }else if(txtIddepartamento.getText().toString().equals("0")){
                        Toast.makeText(getApplicationContext(),"Seleccione un departamento",Toast.LENGTH_SHORT).show();
                    }else{
                        Access_Ciudades db = Access_Ciudades.getInstance(getApplicationContext());
                        db.openWritable();
                        ContentValues values = new ContentValues();
                        values.put("ciudad",txtCiudad.getText().toString());
                        values.put("departamento_iddepartamento", txtIddepartamento.getText().toString());
                        long respuesta =db.ActualizarCiudad(values, ciudadEditar);
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
        Access_Ciudades db = Access_Ciudades.getInstance(getApplicationContext());
        db.openReadable();
        Cursor c = db.getCiudad_a_Modificar(ciudadEditar);
        try {
            if(c.moveToNext()){
                txtCiudad.setText(c.getString(1));
                txtDepartamento.setText(c.getString(2));
                consultarlistadepartamentos();
                ArrayAdapter<String> adaptador = new ArrayAdapter(this, R.layout.spinner_item_ld,listadep);
                comboDepartamentos.setAdapter(adaptador);
                int index= getIndexSpinner(comboDepartamentos, txtDepartamento.getText().toString());
                comboDepartamentos.setSelection(index);
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
        }finally {}
    }

    public void Limpiar_y_volver_a_Lista(){
        Intent i = new Intent(getApplicationContext(), Listar_Ciudad.class);
        startActivity(i);
        txtCiudad.setText("");
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Listar_Ciudad.class);
        startActivity(i);
        finish();
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
        for(int i=0; i < deplist.size();i++) {
            listadep.add(deplist.get(i).getDepartamento().toString());
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