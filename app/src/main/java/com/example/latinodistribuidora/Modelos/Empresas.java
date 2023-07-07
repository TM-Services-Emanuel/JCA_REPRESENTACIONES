package com.example.latinodistribuidora.Modelos;

import java.io.Serializable;

public class Empresas implements Serializable {

    public int idempresa;
    public String razon_social;
    public String ruc;
    public String direccion;
    public String telefono;
    public String estado;
    public int idciudad;
    public String ciudad;


    public Empresas(int idempresa, String razon_social, String ruc, String direccion, String telefono, String ciudad){
        setIdempresa(idempresa);
        setRazon_social(razon_social);
        setRuc(ruc);
        setDireccion(direccion);
        setTelefono(telefono);

        setCiudad(ciudad);
    }
    public Empresas(){

    }

    public int getIdempresa() {
        return idempresa;
    }

    public void setIdempresa(int idempresa) {
        this.idempresa = idempresa;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
