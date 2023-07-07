package com.example.latinodistribuidora.Modelos;

import java.io.Serializable;

public class Clientes implements Serializable {
    public int idcliente;
    public String razon_social;
    public String ruc;
    public String direccion;
    public String celular;
    public int idciudad;
    public String ciudad;

    public Clientes(int idcliente, String razon_social, String ruc, String direccion, String celular, int idciudad, String ciudad) {
        this.idcliente = idcliente;
        this.razon_social = razon_social;
        this.ruc = ruc;
        this.direccion = direccion;
        this.celular = celular;
        this.idciudad = idciudad;
        this.ciudad = ciudad;
    }

    public Clientes(){

    }

    public int getIdcliente() {
        return idcliente;
    }

    public void setIdcliente(int idcliente) {
        this.idcliente = idcliente;
    }

    public String getRazon_social() {
        return razon_social;
    }

    public void setRazon_social(String razon_social) {
        this.razon_social = razon_social;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public int getIdciudad() {
        return idciudad;
    }

    public void setIdciudad(int idciudad) {
        this.idciudad = idciudad;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
}
