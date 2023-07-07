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

import com.example.latinodistribuidora.CRUD.Access_Clientes;
import com.example.latinodistribuidora.CRUD.Access_Division;
import com.example.latinodistribuidora.CRUD.Access_IVA;
import com.example.latinodistribuidora.CRUD.Access_Productos;
import com.example.latinodistribuidora.CRUD.Access_UnicadMedida;
import com.example.latinodistribuidora.Modelos.Division;
import com.example.latinodistribuidora.Modelos.IVA;
import com.example.latinodistribuidora.Modelos.UnidadMedida;
import com.example.latinodistribuidora.R;

import java.util.ArrayList;

public class Registrar_Productos extends AppCompatActivity {
    Spinner comboClasificacion, comboUM, comboIVA;
    TextView txtcodI, txtcodB, txtdescripcion, txtprecio,
            txtidClasificacion,txtidUM, txtidIVA;
    ArrayList<String> listadiv, listaum, listaiva;
    ArrayList<Division> divlist;
    ArrayList<UnidadMedida> umlist;
    ArrayList<IVA> ivalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_productos);
        comboClasificacion = findViewById(R.id.id_spiclasif_n);
        comboUM = findViewById(R.id.id_spium_n);
        comboIVA = findViewById(R.id.id_spiiva_n);
        txtcodI = findViewById(R.id.id_codi_n);
        txtcodB = findViewById(R.id.id_codb_n);
        txtdescripcion = findViewById(R.id.id_descrip_n);
        txtprecio = findViewById(R.id.id_pv_n);
        txtidClasificacion= findViewById(R.id.id_clasif_n);
        txtidUM= findViewById(R.id.idum_n);
        txtidIVA= findViewById(R.id.idiva_n);
        txtcodI.requestFocus();

        consultarlistaClasificaciones();
        ArrayAdapter<CharSequence> adaptadorClasificacion = new ArrayAdapter(this, R.layout.spinner_item_ld,listadiv);
        comboClasificacion.setAdapter(adaptadorClasificacion);
        comboClasificacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{
                    if(position!=0){
                        txtidClasificacion.setText(String.valueOf(divlist.get(position-1).getIddivision()));
                    }else{
                        txtidClasificacion.setText("0");
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "error: "+e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        consultarlistaUM();
        ArrayAdapter<CharSequence> adaptadorUM = new ArrayAdapter(this, R.layout.spinner_item_ld,listaum);
        comboUM.setAdapter(adaptadorUM);
        comboUM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{
                    if(position!=0){
                        txtidUM.setText(String.valueOf(umlist.get(position-1).getIdunidad()));
                    }else{
                        txtidUM.setText("0");
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "error: "+e.getMessage(),Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        consultarlistaIVA();
        ArrayAdapter<CharSequence> adaptadorIVA = new ArrayAdapter(this, R.layout.spinner_item_ld,listaiva);
        comboIVA.setAdapter(adaptadorIVA);
        comboIVA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{
                    if(position!=0){
                        txtidIVA.setText(String.valueOf(ivalist.get(position-1).getIdiva()));
                    }else{
                        txtidIVA.setText("0");
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

    private void consultarlistaClasificaciones() {
        Access_Division db = Access_Division.getInstance(getApplicationContext());
        db.openReadable();
        Division division=null;
        divlist = new ArrayList<Division>();
        Cursor cursor = db.getDivision();
        while (cursor.moveToNext()){
            division = new Division();
            division.setIddivision(cursor.getInt(0));
            division.setDivision(cursor.getString(1));
            divlist.add(division);
        }
        obtenerlistaClasificacion();
    }

    private void consultarlistaUM() {
        Access_UnicadMedida db = Access_UnicadMedida.getInstance(getApplicationContext());
        db.openReadable();
        UnidadMedida unidadMedida=null;
        umlist = new ArrayList<UnidadMedida>();
        Cursor cursor = db.getUnidadMedida();
        while (cursor.moveToNext()){
            unidadMedida = new UnidadMedida();
            unidadMedida.setIdunidad(cursor.getInt(0));
            unidadMedida.setUm(cursor.getString(1));
            unidadMedida.setCant(cursor.getInt(2));
            umlist.add(unidadMedida);
        }
        obtenerlistaUM();
    }

    private void consultarlistaIVA() {
        Access_IVA db = Access_IVA.getInstance(getApplicationContext());
        db.openReadable();
        IVA iva=null;
        ivalist = new ArrayList<IVA>();
        Cursor cursor = db.getIVA();
        while (cursor.moveToNext()){
            iva = new IVA();
            iva.setIdiva(cursor.getInt(0));
            iva.setImpuesto(cursor.getInt(1));
            iva.setDescripcion(cursor.getString(2));
            ivalist.add(iva);
        }
        obtenerlistaIVA();
    }

    private void obtenerlistaClasificacion(){
        listadiv = new ArrayList<String>();
        listadiv.add("Seleccione una Clasificación");
        for(int i=0; i < divlist.size();i++){
            listadiv.add(divlist.get(i).getDivision().toString());
        }
    }

    private void obtenerlistaUM(){
        listaum = new ArrayList<String>();
        listaum.add("Seleccione una U. de medida");
        for(int i=0; i < umlist.size();i++){
            listaum.add(umlist.get(i).getUm().toString());
        }
    }

    private void obtenerlistaIVA(){
        listaiva = new ArrayList<String>();
        listaiva.add("Seleccione un impuesto");
        for(int i=0; i < ivalist.size();i++){
            listaiva.add(ivalist.get(i).getDescripcion().toString());
        }
    }

    public void Guardar(View view){
        if(txtcodI.getText().toString().trim().isEmpty()){
            txtcodI.requestFocus();
        }else if(txtcodB.getText().toString().trim().isEmpty()){
            txtcodB.requestFocus();
        }else if(txtdescripcion.getText().toString().trim().isEmpty()){
            txtdescripcion.requestFocus();
        }else if(txtprecio.getText().toString().trim().isEmpty()){
            txtprecio.requestFocus();
        }else if (txtidClasificacion.getText().toString().equals("0")){
            Toast.makeText(getApplicationContext(),"Seleccione una clasificación",Toast.LENGTH_SHORT).show();
        }else if (txtidUM.getText().toString().equals("0")){
            Toast.makeText(getApplicationContext(),"Seleccione una U. de medida",Toast.LENGTH_SHORT).show();
        }else if (txtidIVA.getText().toString().equals("0")){
            Toast.makeText(getApplicationContext(),"Seleccione un impuesto",Toast.LENGTH_SHORT).show();
        }else{
            Access_Productos db = Access_Productos.getInstance(getApplicationContext());
            db.openWritable();
            long insertado = db.insertarProductos(txtcodI.getText().toString(),txtcodB.getText().toString(),txtdescripcion.getText().toString()
                    ,Integer.parseInt(txtprecio.getText().toString()),Integer.parseInt(txtprecio.getText().toString()),Integer.parseInt(txtidUM.getText().toString())
                            ,Integer.parseInt(txtidClasificacion.getText().toString()),Integer.parseInt(txtidIVA.getText().toString()));
            db.close();
            if(insertado>0){
                Toast.makeText(getApplicationContext(),"Producto registrado exitosamente",Toast.LENGTH_SHORT).show();
                Limpiar_y_volver_a_Principal();
            }else{
                Toast.makeText(getApplicationContext(),"No se pudo registra el producto",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void Limpiar_y_volver_a_Principal(){
        txtcodI.setText("");
        txtcodB.setText("");
        txtdescripcion.setText("");
        txtprecio.setText("");
        comboClasificacion.setSelection(0);
        txtidClasificacion.setText("0");
        comboUM.setSelection(0);
        txtidUM.setText("0");
        comboIVA.setSelection(0);
        txtidIVA.setText("0");

        Intent i = new Intent(getApplicationContext(), Listar_Productos.class);
        startActivity(i);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Listar_Productos.class);
        startActivity(i);
    }


}