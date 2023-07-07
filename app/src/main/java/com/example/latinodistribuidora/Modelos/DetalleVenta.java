package com.example.latinodistribuidora.Modelos;

import java.io.Serializable;

public class DetalleVenta implements Serializable {
    public int idproducto;
    public String codbarra;
    public String producto;
    public String cantidad;
    public String precio;
    public String um;
    public String total;
    public String ivadescripcion;
    public int exenta;
    public int iva5;
    public int iva10;
    public int posicion;

    public DetalleVenta(int idproducto,String codbarra, String producto, String precio, String cantidad,String um, String total, String ivadescripcion, int exenta, int iva5, int iva10) {
        this.idproducto = idproducto;
        this.codbarra = codbarra;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.um = um;
        this.total = total;
        this.ivadescripcion = ivadescripcion;
        this.exenta = exenta;
        this.iva5 = iva5;
        this.iva10 = iva10;
    }
    public DetalleVenta(int posicion){
        this.posicion=posicion;

    }
    public DetalleVenta(){

    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public int getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }

    public String getCodbarra() {
        return codbarra;
    }

    public void setCodbarra(String codbarra) {
        this.codbarra = codbarra;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getIvadescripcion() {
        return ivadescripcion;
    }

    public void setIvadescripcion(String ivadescripcion) {
        this.ivadescripcion = ivadescripcion;
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
}
