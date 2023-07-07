package com.example.latinodistribuidora.Modelos;

import java.io.Serializable;

public class PuntoEmisionSync implements Serializable {

    public int nventa;
    public int actual;

    public PuntoEmisionSync(int actual, int nventa) {
        this.actual = actual;
        this.nventa = nventa;
    }

    public PuntoEmisionSync(){

    }

    public int getNventa() {
        return nventa;
    }

    public void setNventa(int nventa) {
        this.nventa = nventa;
    }

    public int getActual() {
        return actual;
    }

    public void setActual(int actual) {
        this.actual = actual;
    }
}
