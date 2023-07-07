package com.example.latinodistribuidora.Modelos;

import java.io.Serializable;

public class Ciudades implements Serializable {

    public int idciudad;
    public String ciudad;
    public String estado;
    public int iddepartamento;
    public String departamento;


    public Ciudades(int idciudad, String ciudad, String departamento){
        setIdciudad(idciudad);
        setCiudad(ciudad);
        setDepartamento(departamento);
    }
    public  Ciudades(){

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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIddepartamento() {
        return iddepartamento;
    }

    public void setIddepartamento(int iddepartamento) {
        this.iddepartamento = iddepartamento;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
}
