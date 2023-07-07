package com.example.latinodistribuidora.Actividades;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.latinodistribuidora.CRUD.Access_Usuarios;
import com.example.latinodistribuidora.Modelos.Usuarios;
import com.example.latinodistribuidora.R;

import java.util.ArrayList;

public class Listar_Usuario extends AppCompatActivity {
    private ListView lv;
    private ArrayList<Usuarios> lista = new ArrayList<>();
    private ArrayAdapter <String> adaptador;
    private int usuarioseleccionado = -1;
    private Object mActionMode;
    private TextView pie;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_usuarios);
        pie = findViewById(R.id.id_vend_pie);
        llenarLista();
        //onClick();

    }
    public void onResume(){
        super.onResume();
        lista.removeAll(lista);
        llenarLista();
    }
    public void onClick(){
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                usuarioseleccionado=position;
                mActionMode = Listar_Usuario.this.startActionMode(amc);
                view.setSelected(true);
                return true;
            }
        });
    }
    private ActionMode.Callback amc = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            getMenuInflater().inflate(R.menu.opciones_del_upd, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if(item.getItemId() == R.id.item_eliminar){
                Alerta_eliminacion();
                mode.finish();

            }else if(item.getItemId() == R.id.item_modificar){
                Usuarios usu = lista.get(usuarioseleccionado);
                Intent in = new Intent(getApplicationContext(), Editar_Usuario.class);
                in.putExtra("idusuario", usu.getIdusuario());
                startActivity(in);
                mode.finish();
                finish();
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    };

    private void Alerta_eliminacion(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("¿Desea eliminar el usuario seleccionado?");
        alertDialog.setTitle("Eliminar");
        alertDialog.setIcon(android.R.drawable.ic_delete);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Sí", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                eliminarUsuario();
            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void eliminarUsuario(){
        try{
            Access_Usuarios db = Access_Usuarios.getInstance(getApplicationContext());
            Usuarios usu = lista.get(usuarioseleccionado);
            db.openWritable();
            long resultado = db.EliminarUsuario(usu.getIdusuario());
            if(resultado > 0){
                Toast.makeText(getApplicationContext(),"Usuario eliminado satisfactoriamente", Toast.LENGTH_LONG).show();
                lista.removeAll(lista);
                llenarLista();
            }else{
                Toast.makeText(getApplicationContext(),"Se produjo un error al eliminar usuario", Toast.LENGTH_LONG).show();
            }
            db.close();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error Fatal eliminado usuario: "+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void llenarLista(){
        try{
            lv = (ListView) findViewById(R.id.id_lista_departamento);
            Access_Usuarios db = Access_Usuarios.getInstance(getApplicationContext());
            Cursor c = db.getUsuarios();
            if (c.moveToFirst()){
                do {
                    lista.add( new Usuarios (c.getInt(0),c.getString(1),c.getString(2),
                            c.getString(5)));
                }while (c.moveToNext());
            }
            String[] arreglo = new String[lista.size()];
            for (int i = 0;i<arreglo.length;i++){
                arreglo[i] = lista.get(i).getNombre()+" -- "+lista.get(i).getCi()+"\n"+"Usuario utilizado: "+lista.get(i).getUsuario();
            }
            adaptador = new ArrayAdapter<String>(getApplicationContext(), R.layout.listview_item_ld,arreglo);
            lv.setAdapter(adaptador);
            int cant = lv.getCount();
            if(cant==0){
                pie.setText("Lista vacía");
            }else if(cant==1){
                pie.setText(cant+" vendedor listado");
            }else{
                pie.setText(cant+" vendedores listados");
            }
            db.close();

        }catch (Exception e){
            Toast.makeText(this, "Error cargando lista: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}