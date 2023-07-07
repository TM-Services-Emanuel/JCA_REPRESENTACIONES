package com.example.latinodistribuidora.Modelos;

import java.io.Serializable;

public class ReimpresionVenta implements Serializable {
    private String EmpresaRZ, EmpresaRUC, EmpresaDireccion, EmpresaTelefono;
    private String establecimiento, puntoemision, factura, timbrado, desde, hasta, condicion, fecha, hora, ClienteRZ, ClienteRUC;
    private String vendedor;
    private String codBarra, producto, cantidad, um;
    private int precio, total, impuesto;
    private String totalfinal,exenta, cinco, diez;

    public ReimpresionVenta(String empresaRZ, String empresaRUC, String empresaDireccion, String empresaTelefono, String establecimiento,
                            String puntoemision, String factura, String timbrado, String desde, String hasta, String condicion, String fecha,
                            String hora, String clienteRZ, String clienteRUC, String codBarra, String producto, String cantidad, String um,
                            int precio, int total, int impuesto, String totalfinal,String exenta,String cinco, String diez, String vendedor) {
        this.EmpresaRZ = empresaRZ;
        this.EmpresaRUC = empresaRUC;
        this.EmpresaDireccion = empresaDireccion;
        this.EmpresaTelefono = empresaTelefono;
        this.establecimiento = establecimiento;
        this.puntoemision = puntoemision;
        this.factura = factura;
        this.timbrado = timbrado;
        this.desde = desde;
        this.hasta = hasta;
        this.condicion = condicion;
        this.fecha = fecha;
        this.hora = hora;
        this.ClienteRZ = clienteRZ;
        this.ClienteRUC = clienteRUC;
        this.codBarra = codBarra;
        this.producto = producto;
        this.cantidad = cantidad;
        this.um = um;
        this.precio = precio;
        this.total = total;
        this.impuesto = impuesto;
        this.totalfinal = totalfinal;
        this.exenta = exenta;
        this.cinco = cinco;
        this.diez = diez;
        this.vendedor = vendedor;
    }

    public String getEmpresaRZ() {
        return EmpresaRZ;
    }

    public void setEmpresaRZ(String empresaRZ) {
        EmpresaRZ = empresaRZ;
    }

    public String getEmpresaRUC() {
        return EmpresaRUC;
    }

    public void setEmpresaRUC(String empresaRUC) {
        EmpresaRUC = empresaRUC;
    }

    public String getEmpresaDireccion() {
        return EmpresaDireccion;
    }

    public void setEmpresaDireccion(String empresaDireccion) {
        EmpresaDireccion = empresaDireccion;
    }

    public String getEmpresaTelefono() {
        return EmpresaTelefono;
    }

    public void setEmpresaTelefono(String empresaTelefono) {
        EmpresaTelefono = empresaTelefono;
    }

    public String getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(String establecimiento) {
        this.establecimiento = establecimiento;
    }

    public String getPuntoemision() {
        return puntoemision;
    }

    public void setPuntoemision(String puntoemision) {
        this.puntoemision = puntoemision;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public String getTimbrado() {
        return timbrado;
    }

    public void setTimbrado(String timbrado) {
        this.timbrado = timbrado;
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

    public String getClienteRZ() {
        return ClienteRZ;
    }

    public void setClienteRZ(String clienteRZ) {
        ClienteRZ = clienteRZ;
    }

    public String getClienteRUC() {
        return ClienteRUC;
    }

    public void setClienteRUC(String clienteRUC) {
        ClienteRUC = clienteRUC;
    }

    public String getCodBarra() {
        return codBarra;
    }

    public void setCodBarra(String codBarra) {
        this.codBarra = codBarra;
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

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
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

    public int getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(int impuesto) {
        this.impuesto = impuesto;
    }

    public String getTotalfinal() {
        return totalfinal;
    }

    public String getExenta() {
        return exenta;
    }

    public void setExenta(String exenta) {
        this.exenta = exenta;
    }

    public void setTotalfinal(String totalfinal) {
        this.totalfinal = totalfinal;
    }

    public String getCinco() {
        return cinco;
    }

    public void setCinco(String cinco) {
        this.cinco = cinco;
    }

    public String getDiez() {
        return diez;
    }

    public void setDiez(String diez) {
        this.diez = diez;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }
}
