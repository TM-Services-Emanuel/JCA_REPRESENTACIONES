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
import com.example.latinodistribuidora.CRUD.Access_Clientes;
import com.example.latinodistribuidora.CRUD.Access_Division;
import com.example.latinodistribuidora.CRUD.Access_IVA;
import com.example.latinodistribuidora.CRUD.Access_Productos;
import com.example.latinodistribuidora.CRUD.Access_UnicadMedida;
import com.example.latinodistribuidora.Modelos.Ciudades;
import com.example.latinodistribuidora.Modelos.Division;
import com.example.latinodistribuidora.Modelos.IVA;
import com.example.latinodistribuidora.Modelos.UnidadMedida;
import com.example.latinodistribuidora.R;

import java.util.ArrayList;

public class Editar_Producto extends AppCompatActivity {
    private int productoEditar;
    private EditText txtcodI,txtcodB,txtdescripcion,txtprecio;
    private TextView txtidclasificacion, txtclasificacion;
    private TextView txtidum, txtum;
    private TextView txtidiva, txtiva;

    Spinner comboClasificacion, comboUM, comboIVA;
    ArrayList<String> listadiv, listaum, listaiva;
    ArrayList<Division> divlist;
    ArrayList<UnidadMedida> umlist;
    ArrayList<IVA> ivalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_producto);
        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            productoEditar = extras.getInt("idproducto");
        }
        txtcodI = findViewById(R.id.id_codi_m);
        txtcodB = findViewById(R.id.id_codb_m);
        txtdescripcion = findViewById(R.id.id_descrip_m);
        txtprecio = findViewById(R.id.id_pv_m);

        comboClasificacion = findViewById(R.id.id_spiclasif_m);
        comboUM = findViewById(R.id.id_spium_m);
        comboIVA = findViewById(R.id.id_spiiva_m);

        txtidclasificacion = findViewById(R.id.id_clasif_m);
        txtclasificacion = findViewById(R.id.id_descclasif_m);
        txtidum = findViewById(R.id.idum_m);
        txtum = findViewById(R.id.iddescum_m);
        txtidiva = findViewById(R.id.idiva_m);
        txtiva = findViewById(R.id.iddesciva_m);
        reflejarCampos();
        txtcodI.requestFocus();
    }

    public void reflejarCampos(){
        Access_Productos db = Access_Productos.getInstance(getApplicationContext());
        db.openReadable();
        Cursor c = db.getProducto_a_modificar(productoEditar);
        try {
            if(c.moveToNext()){
                txtcodI.setText(c.getString(1));
                txtcodB.setText(c.getString(2));
                txtdescripcion.setText(c.getString(3));
                txtprecio.setText(c.getString(4));
                txtclasificacion.setText(c.getString(9));
                txtum.setText(c.getString(8));
                txtiva.setText(c.getString(6));

                consultarlistaClasificacion();
                ArrayAdapter<String> adaptadorClasificacion = new ArrayAdapter(this, R.layout.spinner_item_ld,listadiv);
                comboClasificacion.setAdapter(adaptadorClasificacion);
                int indexCl= getIndexSpinnerClasificacion(comboClasificacion, txtclasificacion.getText().toString());
                comboClasificacion.setSelection(indexCl);
                comboClasificacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        try{
                            if(position!=0){
                                txtidclasificacion.setText(String.valueOf(divlist.get(position-1).getIddivision()));
                            }else{
                                txtidclasificacion.setText("0");
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
                ArrayAdapter<String> adaptadorUM = new ArrayAdapter(this, R.layout.spinner_item_ld,listaum);
                comboUM.setAdapter(adaptadorUM);
                int indexUM= getIndexSpinnerUM(comboUM, txtum.getText().toString());
                comboUM.setSelection(indexUM);
                comboUM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        try{
                            if(position!=0){
                                txtidum.setText(String.valueOf(umlist.get(position-1).getIdunidad()));
                            }else{
                                txtidum.setText("0");
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
                ArrayAdapter<String> adaptadorIVA = new ArrayAdapter(this, R.layout.spinner_item_ld,listaiva);
                comboIVA.setAdapter(adaptadorIVA);
                int indexIVA= getIndexSpinnerIVA(comboIVA, txtiva.getText().toString());
                comboIVA.setSelection(indexIVA);
                comboIVA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        try{
                            if(position!=0){
                                txtidiva.setText(String.valueOf(ivalist.get(position-1).getIdiva()));
                            }else{
                                txtidiva.setText("0");
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

    private void consultarlistaClasificacion() {
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

    private void obtenerlistaClasificacion(){
        listadiv = new ArrayList<String>();
        listadiv.add("Seleccione una clasificación");
        for(int i=0; i < divlist.size();i++) {
            listadiv.add(divlist.get(i).getDivision().toString());
        }
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

    private void obtenerlistaUM(){
        listaum = new ArrayList<String>();
        listaum.add("Seleccione una U. de medida");
        for(int i=0; i < umlist.size();i++) {
            listaum.add(umlist.get(i).getUm().toString());
        }
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
            iva.setDescripcion(cursor.getString(2));

            ivalist.add(iva);
        }
        obtenerlistaIVA();
    }

    private void obtenerlistaIVA(){
        listaiva = new ArrayList<String>();
        listaiva.add("Seleccione un impuesto");
        for(int i=0; i < ivalist.size();i++) {
            listaiva.add(ivalist.get(i).getDescripcion().toString());
        }
    }

    public static int getIndexSpinnerClasificacion(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
            }
        }
        return index;
    }

    public static int getIndexSpinnerUM(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
            }
        }
        return index;
    }

    public static int getIndexSpinnerIVA(Spinner spinner, String myString)
    {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
            }
        }
        return index;
    }

    public void Modificar(View v){
        if(txtcodI.getText().toString().trim().isEmpty()){
            txtcodI.requestFocus();
        }else if(txtcodB.getText().toString().trim().isEmpty()){
            txtcodB.requestFocus();
        }else if(txtdescripcion.getText().toString().trim().isEmpty()){
            txtdescripcion.requestFocus();
        }else if(txtprecio.getText().toString().trim().isEmpty()){
            txtprecio.requestFocus();
        }else if(txtidclasificacion.getText().toString().equals("0")){
            Toast.makeText(getApplicationContext(),"Seleccione una clasificación",Toast.LENGTH_SHORT).show();
        }else if(txtum.getText().toString().equals("0")){
            Toast.makeText(getApplicationContext(),"Seleccione una U. de medida",Toast.LENGTH_SHORT).show();
        }else if(txtiva.getText().toString().equals("0")){
            Toast.makeText(getApplicationContext(),"Seleccione un impuesto",Toast.LENGTH_SHORT).show();
        }else{
            Access_Productos db = Access_Productos.getInstance(getApplicationContext());
            db.openWritable();
            ContentValues values = new ContentValues();
            values.put("cod_interno",txtcodI.getText().toString());
            values.put("cod_barra",txtcodB.getText().toString());
            values.put("descripcion",txtdescripcion.getText().toString());
            values.put("precio_venta",txtprecio.getText().toString());
            values.put("um_idunidad", txtidum.getText().toString());
            values.put("division_iddivision", txtidclasificacion.getText().toString());
            values.put("iva_idiva", txtidiva.getText().toString());
            long respuesta =db.ActualizarProduto(values, productoEditar);
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
        Intent i = new Intent(getApplicationContext(), Listar_Productos.class);
        startActivity(i);
        txtcodI.setText("");
        txtcodB.setText("");
        txtdescripcion.setText("");
        txtprecio.setText("");
        comboClasificacion.setSelection(0);
        txtidclasificacion.setText("0");
        txtclasificacion.setText("");
        comboUM.setSelection(0);
        txtidum.setText("0");
        txtum.setText("");
        comboIVA.setSelection(0);
        txtidiva.setText("0");
        txtiva.setText("");
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, Listar_Productos.class);
        startActivity(i);
        finish();
    }
}