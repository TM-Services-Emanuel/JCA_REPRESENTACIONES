package com.example.latinodistribuidora;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.latinodistribuidora.Modelos.DetalleVenta;

import java.util.ArrayList;


public class Adaptador_Productos_detalleventa extends BaseAdapter {
    private final Context context;
    private ArrayList<DetalleVenta> listItems;



    public Adaptador_Productos_detalleventa(Context context, ArrayList<DetalleVenta> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DetalleVenta item = (DetalleVenta) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_detalleventa, null);

        TextView descripcion = convertView.findViewById(R.id.id_descripcion);
        TextView ID = convertView.findViewById(R.id.id_ID);
        TextView precio = convertView.findViewById(R.id.id_precio);
        TextView cantidad = convertView.findViewById(R.id.id_cantidad);
        TextView um = convertView.findViewById(R.id.idunidadM);
        TextView total = convertView.findViewById(R.id.id_totalv);
        TextView ivadescripcion = convertView.findViewById(R.id.id_ivadescripcion);
        TextView exenta = convertView.findViewById(R.id.id_Exenta);
        TextView iva5 = convertView.findViewById(R.id.id_iva5);
        TextView iva10 = convertView.findViewById(R.id.id_iva10);
        TextView posicion = convertView.findViewById(R.id.id_posicion);

        descripcion.setText(item.getProducto());
        ID.setText(String.valueOf(item.getIdproducto()));
        precio.setText(item.getPrecio());
        cantidad.setText(item.getCantidad());
        um.setText(item.getUm());
        total.setText(String.valueOf(item.getTotal()));
        ivadescripcion.setText(item.getIvadescripcion());
        exenta.setText(String.valueOf(item.getExenta()));
        iva5.setText(String.valueOf(item.getIva5()));
        iva10.setText(String.valueOf(item.getIva10()));
        posicion.setText(String.valueOf(position));

        return convertView;
    }



}
