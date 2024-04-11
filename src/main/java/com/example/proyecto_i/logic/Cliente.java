package com.example.proyecto_i.logic;

import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class Cliente {
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
    @ManyToOne
    @JoinColumn(name = "proveedor", referencedColumnName = "identificacion", nullable = false)
    private Proveedor proveedorByProveedor;
    @OneToMany(mappedBy = "clienteByCliente")
    private Collection<Factura> facturasByIdentificacion;

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

        Cliente cliente = (Cliente) o;

        if (identificacion != null ? !identificacion.equals(cliente.identificacion) : cliente.identificacion != null)
            return false;
        if (nombre != null ? !nombre.equals(cliente.nombre) : cliente.nombre != null) return false;
        if (telefono != null ? !telefono.equals(cliente.telefono) : cliente.telefono != null) return false;
        if (correo != null ? !correo.equals(cliente.correo) : cliente.correo != null) return false;

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

    public Proveedor getProveedorByProveedor() {
        return proveedorByProveedor;
    }

    public void setProveedorByProveedor(Proveedor proveedorByProveedor) {
        this.proveedorByProveedor = proveedorByProveedor;
    }

    public Collection<Factura> getFacturasByIdentificacion() {
        return facturasByIdentificacion;
    }

    public void setFacturasByIdentificacion(Collection<Factura> facturasByIdentificacion) {
        this.facturasByIdentificacion = facturasByIdentificacion;
    }
}
