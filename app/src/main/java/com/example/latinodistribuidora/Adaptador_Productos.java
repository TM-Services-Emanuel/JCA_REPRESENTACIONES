package com.example.latinodistribuidora;

import static android.view.LayoutInflater.*;
import static com.example.latinodistribuidora.R.layout.listview_item_productos2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.latinodistribuidora.Modelos.Productos;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Adaptador_Productos extends BaseAdapter {
    private final Context context;
    private final ArrayList<Productos> listItems;

    public Adaptador_Productos(Context context, ArrayList<Productos> listItems) {
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

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Productos item = (Productos) getItem(position);


        DecimalFormat nformat = new DecimalFormat("##,###,###");

        convertView = from(context).inflate(listview_item_productos2,null);
        TextView codI = (TextView) convertView.findViewById(R.id.id_interno);
        TextView codB = (TextView) convertView.findViewById(R.id.id_barra);
        TextView descipcion = (TextView) convertView.findViewById(R.id.id_descripcion);
        TextView clasificacion = (TextView) convertView.findViewById(R.id.id_clasificacion);
        TextView precioCosto = (TextView) convertView.findViewById(R.id.id_PrecioCosto);
        TextView precioVenta = (TextView) convertView.findViewById(R.id.id_precio);

        codI.setText(item.getCod_interno());
        codB.setText(item.getCod_barra());
        descipcion.setText(item.getDescripcion());
        clasificacion.setText(item.getDivision());
        precioCosto.setText(nformat.format(Integer.parseInt(item.getPrecioCosto())));
        //precioCosto.setText(item.getPrecioCosto());
        precioVenta.setText(nformat.format(Integer.parseInt(item.getPrecio())).concat("  |  IMPUESTO APLICADO: "+item.getIva()));
        //precioVenta.setText((item.getPrecio()).concat("  |  IMPUESTO APLICADO: "+item.getIva()));
        return convertView;
    }
}
