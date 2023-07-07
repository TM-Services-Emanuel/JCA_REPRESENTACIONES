package com.example.latinodistribuidora.Actividades;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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
import com.example.latinodistribuidora.CRUD.Access_Clientes;
import com.example.latinodistribuidora.Modelos.Ciudades;
import com.example.latinodistribuidora.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Locale;

public class Editar_Clientes extends AppCompatActivity {
    private int clienteEditar;
    private EditText txtrazonsocial,txtruc,txtdireccion,txttelefono;
    private TextView txtidciudad;
    private TextView txtciudad;

    Spinner comboCiudades;
    ArrayList<String> listaciu;
    ArrayList<Ciudades> ciulist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_clientes);
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            clienteEditar = extras.getInt("idcliente");
        }
        txtrazonsocial = findViewById(R.id.id_rz_mod);
        txtruc = findViewById(R.id.id_ruc_mod);
        txtdireccion = findViewById(R.id.id_dire_mod);
        txttelefono = findViewById(R.id.id_telef_mod);

        comboCiudades = findViewById(R.id.id_spinner_cl_mod);
        txtidciudad = findViewById(R.id.id_txtidciudad);
        txtciudad = findViewById(R.id.id_txtciudad_mod);
        reflejarCampos();
        txtrazonsocial.requestFocus();

        findViewById(R.id.id_btnMC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(txtrazonsocial.getText().toString().trim().isEmpty()){
                        txtrazonsocial.requestFocus();
                    }else if(txtruc.getText().toString().trim().isEmpty()){
                        txtruc.requestFocus();
                    }else if(txtdireccion.getText().toString().trim().isEmpty()){
                        txtdireccion.requestFocus();
                    }else if(txttelefono.getText().toString().trim().isEmpty()){
                        txttelefono.requestFocus();
                    }else if(txtidciudad.getText().toString().equals("0")){
                        //Toast.makeText(getApplicationContext(),"Seleccione una ciudad",Toast.LENGTH_SHORT).show();
                        Snackbar.make(findViewById(R.id.linearLayout56), "Seleccione una ciudad.", Snackbar.LENGTH_SHORT)
                                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                                .setBackgroundTint(Color.parseColor("#11232E"))
                                .setActionTextColor(Color.parseColor("#FFFFFF")).show();
                    }else{
                        Access_Clientes db = Access_Clientes.getInstance(getApplicationContext());
                        db.openWritable();
                        ContentValues values = new ContentValues();
                        values.put("razon_social",txtrazonsocial.getText().toString().toUpperCase());
                        values.put("ruc",txtruc.getText().toString().toUpperCase());
                        values.put("direccion",txtdireccion.getText().toString().toUpperCase());
                        values.put("telefono",txttelefono.getText().toString().toUpperCase());
                        values.put("ciudad_idciudad", txtidciudad.getText().toString().toUpperCase());
                        long respuesta =db.ActualizarCliente(values, clienteEditar);
                        db.close();
                        if(respuesta >0){
                            txtrazonsocial.setEnabled(false);
                            txtruc.setEnabled(false);
                            txtdireccion.setEnabled(false);
                            txttelefono.setEnabled(false);
                            comboCiudades.setEnabled(false);
                            //Toast.makeText(getApplicationContext(), "Modificación realizada exitosamente".toUpperCase(), Toast.LENGTH_LONG).show();
                            Snackbar.make(findViewById(R.id.linearLayout56), "Modificación realizado exitosamente!.", Snackbar.LENGTH_INDEFINITE)
                                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                                    .setBackgroundTint(Color.parseColor("#11232E"))
                                    .setActionTextColor(Color.parseColor("#FFFFFF"))
                                    .setAction("VOLVER", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Limpiar_y_volver_a_Lista();
                                        }
                                    })
                                    .show();

                        }else{
                            //Toast.makeText(getApplicationContext(), "Ocurrio un error".toUpperCase(), Toast.LENGTH_LONG).show();
                            Snackbar.make(findViewById(R.id.linearLayout56), "Error modificando cliente.", Snackbar.LENGTH_SHORT)
                                    .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                                    .setBackgroundTint(Color.parseColor("#11232E"))
                                    .setActionTextColor(Color.parseColor("#FFFFFF"))
                                    .show();
                        }
                    }
            }
        });
    }

    public void reflejarCampos(){
        Access_Clientes db = Access_Clientes.getInstance(getApplicationContext());
        db.openReadable();
        Cursor c = db.getCliente_a_modificar(clienteEditar);
        try {
            if(c.moveToNext()){
                txtrazonsocial.setText(c.getString(1));
                txtruc.setText(c.getString(2));
                txtdireccion.setText(c.getString(3));
                txttelefono.setText(c.getString(4));
                txtciudad.setText(c.getString(6));
                consultarlistaciudades();
                ArrayAdapter<String> adaptador = new ArrayAdapter(this, R.layout.spinner_item_ld,listaciu);
                comboCiudades.setAdapter(adaptador);
                int index= getIndexSpinner(comboCiudades, txtciudad.getText().toString());
                comboCiudades.setSelection(index);
                comboCiudades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        try{
                            if(position!=0){
                                txtidciudad.setText(String.valueOf(ciulist.get(position-1).getIdciudad()));
                            }else{
                                txtidciudad.setText("0");
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
        Intent i = new Intent(getApplicationContext(), Listar_Clientes.class);
        startActivity(i);
        finish();
        /*txtrazonsocial.setText("");
        txtruc.setText("");
        txtdireccion.setText("");
        txttelefono.setText("");
        comboCiudades.setSelection(0);
        txtidciudad.setText("0");*/

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Listar_Clientes.class);
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