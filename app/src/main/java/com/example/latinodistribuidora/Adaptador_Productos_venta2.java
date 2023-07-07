package com.example.latinodistribuidora;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.latinodistribuidora.Modelos.Productos;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Adaptador_Productos_venta2 extends BaseAdapter {
    private final Context context;
    private final ArrayList<Productos> listItems;

    public Adaptador_Productos_venta2(Context context, ArrayList<Productos> listItems) {
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

        DecimalFormat nformat = new DecimalFormat("##,###,###");
        Productos item = (Productos) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_productos_venta, null);
        TextView descipcion = convertView.findViewById(R.id.id_descripcion);
        TextView precio = convertView.findViewById(R.id.id_precio);
        descipcion.setText(item.getDescripcion());
        precio.setText(nformat.format(Integer.valueOf(item.getPrecioCosto())) + "   |   IMPUESTO APLICADO: " + item.getIva());
        return convertView;
    }
}
