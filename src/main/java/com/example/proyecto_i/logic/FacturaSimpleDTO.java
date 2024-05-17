package com.example.proyecto_i.logic;

import java.util.Collection;

public class FacturaSimpleDTO {
    private int numero;
    private String fecha;
    private String proveedorIdentificacion;
    private String clienteIdentificacion;

    // Constructor
    public FacturaSimpleDTO(int numero, String fecha, String proveedorIdentificacion, String clienteIdentificacion) {
        this.numero = numero;
        this.fecha = fecha;
        this.proveedorIdentificacion = proveedorIdentificacion;
        this.clienteIdentificacion = clienteIdentificacion;
    }

    // Getters y Setters
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getProveedorIdentificacion() {
        return proveedorIdentificacion;
    }

    public void setProveedorIdentificacion(String proveedorIdentificacion) {
        this.proveedorIdentificacion = proveedorIdentificacion;
    }

    public String getClienteIdentificacion() {
        return clienteIdentificacion;
    }

    public void setClienteIdentificacion(String clienteIdentificacion) {
        this.clienteIdentificacion = clienteIdentificacion;
    }
}
