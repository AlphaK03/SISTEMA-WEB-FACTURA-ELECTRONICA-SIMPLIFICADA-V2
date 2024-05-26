package com.example.proyecto_i.logic;


public class DetalleSimpleDTO {
    private int numero;
    private String descripcion;
    private int cantidad;
    private String codigoPorducto;

    public DetalleSimpleDTO (int numero, String descripcion, int cantidad, String codigoPorducto){
        this.numero=numero;
        this.descripcion = descripcion;
        this.cantidad=cantidad;
        this.codigoPorducto=codigoPorducto;
    }

    public int getNumero() {
        return numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCodigoPorducto() {
        return codigoPorducto;
    }

    public int getCantidad() {
        return cantidad;
    }
}
