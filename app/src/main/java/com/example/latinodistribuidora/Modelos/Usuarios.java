package com.example.latinodistribuidora.Modelos;

import java.io.Serializable;

public class Usuarios implements Serializable {
    public int idusuario;
    public String nombre;
    public String ci;
    public String direccion;
    public String celular;
    public String usuario;
    public Usuarios(int idusuario,String nombre,String ci, String direccion, String celular, String usuario){
        setIdusuario(idusuario);
        setNombre(nombre);
        setCi(ci);
        setDireccion(direccion);
        setCelular(celular);
        setUsuario(usuario);
    }
    public Usuarios(int idusuario,String nombre, String ci,String usuario) {
        setIdusuario(idusuario);
        setNombre(nombre);
        setCi(ci);
        setUsuario(usuario);
    }

    public int getIdusuario() {return idusuario;}

    public void setIdusuario(int idusuario) {this.idusuario = idusuario;}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
