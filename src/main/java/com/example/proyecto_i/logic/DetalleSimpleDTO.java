package com.example.proyecto_i.logic;


public class DetalleSimpleDTO {
    private int numero;
    private String descripcion;
    private int cantidad;
    private String codigoProducto;

    public DetalleSimpleDTO (int numero, String descripcion, int cantidad, String codigoProducto){
        this.numero=numero;
        this.descripcion = descripcion;
        this.cantidad=cantidad;
        this.codigoProducto=codigoProducto;
    }

    public int getNumero() {
        return numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public int getCantidad() {
        return cantidad;
    }
}
