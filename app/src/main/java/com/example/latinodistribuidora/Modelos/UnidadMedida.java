package com.example.latinodistribuidora.Modelos;

import java.io.Serializable;

public class UnidadMedida implements Serializable {

    public int idunidad;
    public String um;
    public int cant;

    public UnidadMedida(int idunidad, String um, int cant){
        setIdunidad(idunidad);
        setUm(um);
        setCant(cant);
    }
    public UnidadMedida(){
    }

    public int getIdunidad() {
        return idunidad;
    }

    public void setIdunidad(int idunidad) {
        this.idunidad = idunidad;
    }

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    public int getCant() {
        return cant;
    }

    public void setCant(int cant) {
        this.cant = cant;
    }
}
