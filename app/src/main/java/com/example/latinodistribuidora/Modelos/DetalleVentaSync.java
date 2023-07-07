package com.example.latinodistribuidora.Modelos;

import java.io.Serializable;

public class DetalleVentaSync implements Serializable {
    public int venta_1_idventa_1;
    public int idemision;
    public int productos_idproductos;
    public String cantidad;
    public int precio;
    public int total;
    public int impuesto_idimpuesto;
    public String um;

    public DetalleVentaSync(int venta_1_idventa_1, int idemision, int productos_idproductos, String cantidad, int precio, int total, int impuesto_idimpuesto, String um) {
        this.venta_1_idventa_1 = venta_1_idventa_1;
        this.idemision = idemision;
        this.productos_idproductos = productos_idproductos;
        this.cantidad = cantidad;
        this.precio = precio;
        this.total = total;
        this.impuesto_idimpuesto = impuesto_idimpuesto;
        this.um = um;
    }

    public int getIdventa() {
        return venta_1_idventa_1;
    }

    public void setIdventa(int idventa) {
        this.venta_1_idventa_1 = idventa;
    }

    public int getIdemision() {
        return idemision;
    }

    public void setIdemision(int idemision) {
        this.idemision = idemision;
    }

    public int getProductos_idproductos() {
        return productos_idproductos;
    }

    public void setProductos_idproductos(int productos_idproductos) {
        this.productos_idproductos = productos_idproductos;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getImpuesto_aplicado() {
        return impuesto_idimpuesto;
    }

    public void setImpuesto_aplicado(int impuesto_aplicado) {
        this.impuesto_idimpuesto = impuesto_aplicado;
    }

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }
}