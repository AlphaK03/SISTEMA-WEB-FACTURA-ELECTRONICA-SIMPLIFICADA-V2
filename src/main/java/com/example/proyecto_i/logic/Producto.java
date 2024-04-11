package com.example.proyecto_i.logic;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class Producto {
    @Id
    @Column(name = "codigo")
    private int codigo;
    @Basic
    @Column(name = "nombre")
    private String nombre;
    @Basic
    @Column(name = "precio")
    private String precio;
    @OneToMany(mappedBy = "productoByCodigoproducto")
    private Collection<Detalle> detallesByCodigo;
    @ManyToOne
    @JoinColumn(name = "proveedor", referencedColumnName = "identificacion", nullable = false)
    private Proveedor proveedorByProveedor;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
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

        Producto producto = (Producto) o;

        if (codigo != producto.codigo) return false;
        if (nombre != null ? !nombre.equals(producto.nombre) : producto.nombre != null) return false;
        if (precio != null ? !precio.equals(producto.precio) : producto.precio != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = codigo;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (precio != null ? precio.hashCode() : 0);
        return result;
    }

    public Collection<Detalle> getDetallesByCodigo() {
        return detallesByCodigo;
    }

    public void setDetallesByCodigo(Collection<Detalle> detallesByCodigo) {
        this.detallesByCodigo = detallesByCodigo;
    }

    public Proveedor getProveedorByProveedor() {
        return proveedorByProveedor;
    }

    public void setProveedorByProveedor(Proveedor proveedorByProveedor) {
        this.proveedorByProveedor = proveedorByProveedor;
    }
}
