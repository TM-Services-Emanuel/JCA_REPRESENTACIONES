package com.example.latinodistribuidora.Modelos;

import java.io.Serializable;

public class PuntoEmision implements Serializable {

    public int idemision;
    public int idtimbrado;
    public String establecimiento;
    public String pe;
    public String direccion;
    public String desde;
    public String hasta;
    public String actual;
    public String estado;

    public PuntoEmision(int idemision, int idtimbrado, String establecimiento, String pe, String direccion, String desde, String hasta,String actual, String estado) {
        this.idemision = idemision;
        this.idtimbrado = idtimbrado;
        this.establecimiento = establecimiento;
        this.pe = pe;
        this.direccion = direccion;
        this.desde = desde;
        this.hasta = hasta;
        this.actual = actual;
        this.estado = estado;
    }
    public PuntoEmision(int idemision, String establecimiento, String pe, String direccion, String desde, String hasta,String actual, String estado) {
        this.idemision = idemision;
        this.establecimiento = establecimiento;
        this.pe = pe;
        this.direccion = direccion;
        this.desde = desde;
        this.hasta = hasta;
        this.actual = actual;
        this.estado = estado;
    }

    public PuntoEmision(){

    }

    public int getIdemision() {
        return idemision;
    }

    public void setIdemision(int idemision) {
        this.idemision = idemision;
    }

    public int getIdtimbrado() {
        return idtimbrado;
    }

    public void setIdtimbrado(int idtimbrado) {
        this.idtimbrado = idtimbrado;
    }

    public String getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(String establecimiento) {
        this.establecimiento = establecimiento;
    }

    public String getPe() {
        return pe;
    }

    public void setPe(String pe) {
        this.pe = pe;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
