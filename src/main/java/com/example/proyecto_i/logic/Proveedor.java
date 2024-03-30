package com.example.proyecto_i.logic;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.Objects;

@Entity
public class Proveedor {
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
    @Basic
    @Column(name = "contraseña")
    private String contraseña;
    @OneToMany(mappedBy = "proveedorByProveedor")
    private Collection<Cliente> clientesByIdentificacion;
    @OneToMany(mappedBy = "proveedorByProveedor")
    private Collection<Facturas> facturasByIdentificacion;
    @OneToMany(mappedBy = "proveedorByProveedor")
    private Collection<Productos> productosByIdentificacion;

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

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proveedor proveedor = (Proveedor) o;
        return Objects.equals(identificacion, proveedor.identificacion) && Objects.equals(nombre, proveedor.nombre) && Objects.equals(telefono, proveedor.telefono) && Objects.equals(correo, proveedor.correo) && Objects.equals(contraseña, proveedor.contraseña);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificacion, nombre, telefono, correo, contraseña);
    }

    public Collection<Cliente> getClientesByIdentificacion() {
        return clientesByIdentificacion;
    }

    public void setClientesByIdentificacion(Collection<Cliente> clientesByIdentificacion) {
        this.clientesByIdentificacion = clientesByIdentificacion;
    }

    public Collection<Facturas> getFacturasByIdentificacion() {
        return facturasByIdentificacion;
    }

    public void setFacturasByIdentificacion(Collection<Facturas> facturasByIdentificacion) {
        this.facturasByIdentificacion = facturasByIdentificacion;
    }

    public Collection<Productos> getProductosByIdentificacion() {
        return productosByIdentificacion;
    }

    public void setProductosByIdentificacion(Collection<Productos> productosByIdentificacion) {
        this.productosByIdentificacion = productosByIdentificacion;
    }
}
