package com.example.latinodistribuidora;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.latinodistribuidora.Modelos.PuntoEmision;

import java.util.ArrayList;

import static android.R.*;

public class Adaptador_PE extends BaseAdapter {
    private Context context;
    private ArrayList<PuntoEmision> listItems;

    public Adaptador_PE(Context context, ArrayList<PuntoEmision> listItems) {
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

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PuntoEmision item = (PuntoEmision) getItem(position);

        convertView = LayoutInflater.from(context).inflate(R.layout.listview_item_pe,null);
        TextView esta_pe = (TextView) convertView.findViewById(R.id.tv_idesta_pe);
        TextView direccion = (TextView) convertView.findViewById(R.id.tv_iddirec_pe);
        TextView desde = (TextView) convertView.findViewById(R.id.tv_iddesde);
        TextView hasta = (TextView) convertView.findViewById(R.id.tv_idhasta);
        TextView actual = (TextView) convertView.findViewById(R.id.tv_idactual);
        TextView resto = (TextView) convertView.findViewById(R.id.tv_idresto);
        TextView estadoA = (TextView) convertView.findViewById(R.id.tv_idestado_a);
        TextView estadoI = (TextView) convertView.findViewById(R.id.tv_idestado_i);

        esta_pe.setText(item.getEstablecimiento()+"-"+item.getPe());
        direccion.setText(item.getDireccion());
        desde.setText(item.getDesde());
        hasta.setText(item.getHasta());
        actual.setText(item.getActual());
        int rt=Integer.parseInt(item.getHasta())-Integer.parseInt(item.getActual());
        resto.setText(String.valueOf(rt));
        if(item.getEstado().equals("Activo")){
            estadoA.setText(item.getEstado());
            estadoI.setText("");
        }else{
            estadoA.setText("");
            estadoI.setText(item.getEstado());
        }
        return convertView;
    }
}
