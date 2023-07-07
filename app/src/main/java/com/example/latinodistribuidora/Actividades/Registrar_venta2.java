package com.example.latinodistribuidora.Actividades;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.latinodistribuidora.CRUD.Access_PE;
import com.example.latinodistribuidora.CRUD.Access_Venta;
import com.example.latinodistribuidora.R;

public class Registrar_venta2 extends AppCompatActivity{
    private int idCliente;
    private String razonsocial,ruc,fecha,vendedor,idvendedor,establecimiento,puntoexpdicion,facturaA, idEmision, idTimbra;
    private TextView txtrazonsocial,txtruc,
            txtfecha, txtvendedor,txtfacturaA,txtoperacion,txtcondicion,txthora;

    private String condicion, hora;

    private Fragment_Venta2 fragment_venta;
    private Fragment_DetalleV fragment_detalleV;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_venta2);
        txtrazonsocial= findViewById(R.id.id_DescCliente);
        txtruc= findViewById(R.id.id_ruc_venta);
        txtfecha= findViewById(R.id.id_Fecha);
        txtvendedor= findViewById(R.id.id_NombVendedor);
        txtfacturaA = findViewById(R.id.id_nFact);
        txtoperacion = findViewById(R.id.id_noper);
        txtcondicion = findViewById(R.id.id_condicion);
        txthora = findViewById(R.id.id_hora);

        Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            condicion = extras.getString("condicion");
           // idCliente = extras.getInt("idcliente");
            razonsocial= extras.getString("razonsocial");
            ruc= extras.getString("ruc");
            fecha= extras.getString("fecha");
            hora = extras.getString("hora");
            idvendedor= extras.getString("idvendedor");
            vendedor= extras.getString("vendedor");
            establecimiento= extras.getString("establecimiento");
            puntoexpdicion= extras.getString("puntoexpedicion");
            facturaA = extras.getString("facturaactual");
            idEmision = extras.getString("idemision");
            idTimbra = extras.getString("idtimbrado");

            txtrazonsocial.setText(razonsocial);
            txtruc.setText(ruc);
            txtfecha.setText(fecha);
            txthora.setText(hora);
            txtvendedor.setText(vendedor);
            txtfacturaA.setText(establecimiento+"-"+puntoexpdicion+"-"+facturaA);
            txtoperacion.setText(String.valueOf(ObtenerCodigo()));
            txtcondicion.setText(condicion);
        }
        fragment_venta = new Fragment_Venta2();
        fragment_detalleV = new Fragment_DetalleV();
        getSupportFragmentManager().beginTransaction().add(R.id.contenedorFragmen, fragment_detalleV).hide(fragment_detalleV).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.contenedorFragmen, fragment_venta).commit();
        EnviarDatosaFragmentDetalle();


    }

    public void onClick(View view) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (view.getId()) {
            case R.id.btnCVenta:
                if (fragment_venta.isAdded()) {
                    fragmentTransaction
                            .show(fragment_venta)
                            .hide(fragment_detalleV);

                } else {
                    fragmentTransaction
                            .add(R.id.contenedorFragmen, fragment_venta)
                            .hide(fragment_detalleV);

                }
                break;
            case R.id.btnDVenta:
                if (fragment_detalleV.isAdded()) {
                    fragmentTransaction
                            .hide(fragment_venta)
                            .show(fragment_detalleV);

                } else {
                    fragmentTransaction
                            .hide(fragment_venta)
                            .add(R.id.contenedorFragmen, fragment_detalleV);
                }
                break;

        }
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertaCerrar();
    }

    private void AlertaCerrar(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("¿Seguro que desea cancelar esta venta?");
        alertDialog.setTitle("CANCELAR VENTA");
        alertDialog.setIcon(R.drawable.ic_cancl);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("SI, CANCELAR", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        });
        alertDialog.setNegativeButton("CONTINUAR VENDIENDO", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void EnviarDatosaFragmentDetalle(){
        Bundle datos = new Bundle();
        datos.putInt("idventas", Integer.parseInt(txtoperacion.getText().toString()));
        datos.putString("nestablecimiento", establecimiento);
        datos.putString("nemision", puntoexpdicion );
        datos.putString("nrofactura", facturaA);
        datos.putString("condicion", condicion);
        datos.putString("fecha",fecha);
        datos.putString("hora",hora);
        //datos.putInt("idcliente", idCliente);
        datos.putString("Vcliente",razonsocial);
        datos.putString("Vruc",ruc);
        datos.putInt("idusuario",Integer.parseInt(idvendedor));
        datos.putString("vendedor",vendedor);
        //datos.putInt("idtimbrado", Integer.parseInt(idTimbra));
        datos.putInt("idemision", Integer.parseInt(idEmision));
        fragment_detalleV.setArguments(datos);
    }

    private int ObtenerCodigo(){
        Access_Venta db = Access_Venta.getInstance(getApplicationContext());
        int idOP = 0;
        Cursor cod=db.getCodigo(ObtenerPE());
        if(cod.moveToFirst()){
            idOP= (cod.getInt(0))+1;
        }
        return idOP;
    }
    private int ObtenerPE(){
        Access_PE db = Access_PE.getInstance(getApplicationContext());
        int idPE = 0;
        Cursor PuntoActivo = db.getPEActivo();
        if (PuntoActivo.moveToFirst()){
            do {
                idPE=PuntoActivo.getInt(0);
            }while (PuntoActivo.moveToNext());
        }
        return idPE;
    }
}