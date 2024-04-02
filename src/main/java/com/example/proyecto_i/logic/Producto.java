package com.example.proyecto_i.logic;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Producto {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "codigo")
    private String codigo;
    @Basic
    @Column(name = "nombre")
    private String nombre;
    @Basic
    @Column(name = "precio")
    private String precio;
    @ManyToOne
    @JoinColumn(name = "proveedor", referencedColumnName = "identificacion", nullable = false)
    private Proveedor proveedorByProveedor;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto productos = (Producto) o;
        return Objects.equals(codigo, productos.codigo) && Objects.equals(nombre, productos.nombre) && Objects.equals(precio, productos.precio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, nombre, precio);
    }

    public Proveedor getProveedorByProveedor() {
        return proveedorByProveedor;
    }

    public void setProveedorByProveedor(Proveedor proveedorByProveedor) {
        this.proveedorByProveedor = proveedorByProveedor;
    }
}
