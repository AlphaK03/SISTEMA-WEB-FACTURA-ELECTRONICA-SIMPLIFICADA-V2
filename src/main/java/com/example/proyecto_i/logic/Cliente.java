package com.example.proyecto_i.logic;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
public class Cliente {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @OneToMany(mappedBy = "clientesByCliente")
    private Collection<Facturas> facturasByIdentificacion;

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
        Cliente clientes = (Cliente) o;
        return Objects.equals(identificacion, clientes.identificacion) && Objects.equals(nombre, clientes.nombre) && Objects.equals(telefono, clientes.telefono) && Objects.equals(correo, clientes.correo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificacion, nombre, telefono, correo);
    }

    public Proveedor getProveedorByProveedor() {
        return proveedorByProveedor;
    }

    public void setProveedorByProveedor(Proveedor proveedorByProveedor) {
        this.proveedorByProveedor = proveedorByProveedor;
    }

    public Collection<Facturas> getFacturasByIdentificacion() {
        return facturasByIdentificacion;
    }

    public void setFacturasByIdentificacion(Collection<Facturas> facturasByIdentificacion) {
        this.facturasByIdentificacion = facturasByIdentificacion;
    }
}
