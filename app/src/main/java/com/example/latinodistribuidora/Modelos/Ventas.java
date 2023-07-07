package com.example.latinodistribuidora.Modelos;


import java.io.Serializable;
import java.util.ArrayList;

public class Ventas extends ArrayList<Ventas> implements Serializable {

    public int id,total,exenta,iva5,iva10,idusuario,idemision;
    public String ruc, descripcion,nrofactura,condicion,fecha,hora,estado;
    public String timbrado, nombreusu;
    public String Est, Emision, sync;

    public Ventas(int id, int idemision, int idusuario, String ruc, String descripcion, String nrofactura,
                  String condicion, String fecha, String hora, int total, int exenta, int iva5, int iva10, String estado, String sync) {
        this.id = id;
        this.total = total;
        this.exenta = exenta;
        this.iva5 = iva5;
        this.iva10 = iva10;
        this.idusuario = idusuario;
        this.idemision = idemision;
        this.ruc = ruc;
        this.descripcion = descripcion;
        this.nrofactura = nrofactura;
        this.condicion = condicion;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.sync = sync;
    }

    public Ventas(int id, int idemision, String Est, String Emision, String nrofactura, String timbrado,
                  String condicion, String fecha, String hora, String ruc, String descripcion, int total, int exenta, int iva5, int iva10,
                  int idusuario, String nombreusu, String estado, String sync) {
        this.id = id;
        this.total = total;
        this.exenta = exenta;
        this.iva5 = iva5;
        this.iva10 = iva10;
        this.idusuario = idusuario;
        this.idemision = idemision;
        this.ruc = ruc;
        this.descripcion = descripcion;
        this.Est = Est;
        this.Emision = Emision;
        this.nrofactura = nrofactura;
        this.condicion = condicion;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.timbrado = timbrado;
        this.nombreusu = nombreusu;
        this.sync = sync;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getExenta() {
        return exenta;
    }

    public void setExenta(int exenta) {
        this.exenta = exenta;
    }

    public int getIva5() {
        return iva5;
    }

    public void setIva5(int iva5) {
        this.iva5 = iva5;
    }

    public int getIva10() {
        return iva10;
    }

    public void setIva10(int iva10) {
        this.iva10 = iva10;
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(int idusuario) {
        this.idusuario = idusuario;
    }

    public int getIdemision() {
        return idemision;
    }

    public void setIdemision(int idemision) {
        this.idemision = idemision;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNrofactura() {
        return nrofactura;
    }

    public void setNrofactura(String nrofactura) {
        this.nrofactura = nrofactura;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTimbrado() {
        return timbrado;
    }

    public void setTimbrado(String timbrado) {
        this.timbrado = timbrado;
    }

    public String getNombreusu() {
        return nombreusu;
    }

    public void setNombreusu(String nombreusu) {
        this.nombreusu = nombreusu;
    }

    public String getEst() {
        return Est;
    }

    public void setEst(String est) {
        Est = est;
    }

    public String getEmision() {
        return Emision;
    }

    public void setEmision(String emision) {
        Emision = emision;
    }

    public String getSync() {
        return sync;
    }

    public void setSync(String sync) {
        this.sync = sync;
    }
}


