package com.example.proyecto_i.logic;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Facturas {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "numero")
    private String numero;
    @Basic
    @Column(name = "fecha")
    private String fecha;
    @ManyToOne
    @JoinColumn(name = "proveedor", referencedColumnName = "identificacion", nullable = false)
    private Proveedor proveedorByProveedor;
    @ManyToOne
    @JoinColumn(name = "cliente", referencedColumnName = "identificacion", nullable = false)
    private Clientes clientesByCliente;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Facturas facturas = (Facturas) o;
        return Objects.equals(numero, facturas.numero) && Objects.equals(fecha, facturas.fecha);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, fecha);
    }

    public Proveedor getProveedorByProveedor() {
        return proveedorByProveedor;
    }

    public void setProveedorByProveedor(Proveedor proveedorByProveedor) {
        this.proveedorByProveedor = proveedorByProveedor;
    }

    public Clientes getClientesByCliente() {
        return clientesByCliente;
    }

    public void setClientesByCliente(Clientes clientesByCliente) {
        this.clientesByCliente = clientesByCliente;
    }
}
