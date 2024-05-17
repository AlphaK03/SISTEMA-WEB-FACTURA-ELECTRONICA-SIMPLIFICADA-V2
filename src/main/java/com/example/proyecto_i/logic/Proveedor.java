package com.example.proyecto_i.logic;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class Proveedor {
    @Id
    @Column(name = "identificacion")
    private String identificacion;
    @Basic
    @Column(name = "nombre")
    private String nombre;
    @Basic
    @Column(name = "telefono")
    private String telefono;
    @Basic
    @Column(name = "correo")
    private String correo;
    @OneToMany(mappedBy = "proveedorByProveedor")
    private Collection<Cliente> clientesByIdentificacion;
    @OneToMany(mappedBy = "proveedorByProveedor")
    private Collection<Factura> facturasByIdentificacion;
    @OneToMany(mappedBy = "proveedorByProveedor")
    @JsonIgnore
    private Collection<Producto> productosByIdentificacion;

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Proveedor proveedor = (Proveedor) o;

        if (identificacion != null ? !identificacion.equals(proveedor.identificacion) : proveedor.identificacion != null)
            return false;
        if (nombre != null ? !nombre.equals(proveedor.nombre) : proveedor.nombre != null) return false;
        if (telefono != null ? !telefono.equals(proveedor.telefono) : proveedor.telefono != null) return false;
        if (correo != null ? !correo.equals(proveedor.correo) : proveedor.correo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = identificacion != null ? identificacion.hashCode() : 0;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (telefono != null ? telefono.hashCode() : 0);
        result = 31 * result + (correo != null ? correo.hashCode() : 0);
        return result;
    }

    public Collection<Cliente> getClientesByIdentificacion() {
        return clientesByIdentificacion;
    }

    public void setClientesByIdentificacion(Collection<Cliente> clientesByIdentificacion) {
        this.clientesByIdentificacion = clientesByIdentificacion;
    }

    public Collection<Factura> getFacturasByIdentificacion() {
        return facturasByIdentificacion;
    }

    public void setFacturasByIdentificacion(Collection<Factura> facturasByIdentificacion) {
        this.facturasByIdentificacion = facturasByIdentificacion;
    }
   
    public Collection<Producto> getProductosByIdentificacion() {
        return productosByIdentificacion;
    }

    public void setProductosByIdentificacion(Collection<Producto> productosByIdentificacion) {
        this.productosByIdentificacion = productosByIdentificacion;
    }
}
