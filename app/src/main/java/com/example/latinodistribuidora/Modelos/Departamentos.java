package com.example.latinodistribuidora.Modelos;

import java.io.Serializable;

public class Departamentos implements Serializable {

    public int iddepartamento;
    public String departamento;

    public Departamentos (int iddepartamento, String departamento){
        setIddepartamento(iddepartamento);
        setDepartamento(departamento);
    }
    public Departamentos (){
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
