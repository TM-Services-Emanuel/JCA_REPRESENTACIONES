package com.example.latinodistribuidora.Modelos;

import java.io.Serializable;

public class IVA implements Serializable {

    public int idiva;
    public int impuesto;
    public String descripcion;

    public IVA(int idiva, int impuesto, String descripcion){
        setIdiva(idiva);
        setImpuesto(impuesto);
        setDescripcion(descripcion);
    }
    public IVA(){
    }

    public int getIdiva() {
        return idiva;
    }

    public void setIdiva(int idiva) {
        this.idiva = idiva;
    }

    public int getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(int impuesto) {
        this.impuesto = impuesto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
