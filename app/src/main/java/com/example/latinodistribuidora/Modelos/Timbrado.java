package com.example.latinodistribuidora.Modelos;

import java.io.Serializable;

public class Timbrado implements Serializable {

    public int idtimbrado;
    public String timbrado;
    public String desde;
    public String hasta;
    public String estado;

    public Timbrado(int idtimbrado, String timbrado, String desde, String hasta, String estado) {
        this.idtimbrado = idtimbrado;
        this.timbrado = timbrado;
        this.desde = desde;
        this.hasta = hasta;
        this.estado = estado;
    }
    public Timbrado(){

    }

    public int getIdtimbrado() {
        return idtimbrado;
    }

    public void setIdtimbrado(int idtimbrado) {
        this.idtimbrado = idtimbrado;
    }

    public String getTimbrado() {
        return timbrado;
    }

    public void setTimbrado(String timbrado) {
        this.timbrado = timbrado;
    }

    public String getDesde() {
        return desde;
    }

    public void setDesde(String desde) {
        this.desde = desde;
    }

    public String getHasta() {
        return hasta;
    }

    public void setHasta(String hasta) {
        this.hasta = hasta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
