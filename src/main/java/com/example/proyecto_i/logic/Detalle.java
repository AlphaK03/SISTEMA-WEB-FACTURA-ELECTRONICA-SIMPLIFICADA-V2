package com.example.proyecto_i.logic;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Detalle {
    @Id
    @Column(name = "numero")
    private int numero;

    @Basic
    @Column(name = "descripcion")
    private String descripcion;

    @Basic
    @Column(name = "cantidad")
    private int cantidad;

    @ManyToOne
    @JoinColumn(name = "numerofactura", referencedColumnName = "numero", nullable = false)
    @JsonBackReference
    private Factura facturaByNumerofactura;

    @ManyToOne
    @JoinColumn(name = "codigoproducto", referencedColumnName = "codigo", nullable = false)
    private Producto productoByCodigoproducto;
    public int getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Detalle detalle = (Detalle) o;

        if (numero != detalle.numero) return false;
        if (cantidad != detalle.cantidad) return false;
        if (descripcion != null ? !descripcion.equals(detalle.descripcion) : detalle.descripcion != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = numero;
        result = 31 * result + (descripcion != null ? descripcion.hashCode() : 0);
        result = 31 * result + cantidad;
        return result;
    }

    public Factura getFacturaByNumerofactura() {
        return facturaByNumerofactura;
    }

    public void setFacturaByNumerofactura(Factura facturaByNumerofactura) {
        this.facturaByNumerofactura = facturaByNumerofactura;
    }

    public Producto getProductoByCodigoproducto() {
        return productoByCodigoproducto;
    }

    public void setProductoByCodigoproducto(Producto productoByCodigoproducto) {
        this.productoByCodigoproducto = productoByCodigoproducto;
    }
}
