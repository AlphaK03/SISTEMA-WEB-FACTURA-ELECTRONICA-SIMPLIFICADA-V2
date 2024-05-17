package com.example.proyecto_i.logic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Collection;

@Entity
public class Factura {
    @Id
    @Column(name = "numero")
    private int numero;
    @Basic
    @Column(name = "fecha")
    private String fecha;
    @OneToMany(mappedBy = "facturaByNumerofactura")
    private Collection<Detalle> detallesByNumero;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "proveedor", referencedColumnName = "identificacion", nullable = false)
    private Proveedor proveedorByProveedor;
    @ManyToOne
    @JoinColumn(name = "cliente", referencedColumnName = "identificacion", nullable = false)
    private Cliente clienteByCliente;

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
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

        Factura factura = (Factura) o;

        if (numero != factura.numero) return false;
        if (fecha != null ? !fecha.equals(factura.fecha) : factura.fecha != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = numero;
        result = 31 * result + (fecha != null ? fecha.hashCode() : 0);
        return result;
    }

    public Collection<Detalle> getDetallesByNumero() {
        return detallesByNumero;
    }

    public void setDetallesByNumero(Collection<Detalle> detallesByNumero) {
        this.detallesByNumero = detallesByNumero;
    }

    public Proveedor getProveedorByProveedor() {
        return proveedorByProveedor;
    }

    public void setProveedorByProveedor(Proveedor proveedorByProveedor) {
        this.proveedorByProveedor = proveedorByProveedor;
    }

    public Cliente getClienteByCliente() {
        return clienteByCliente;
    }

    public void setClienteByCliente(Cliente clienteByCliente) {
        this.clienteByCliente = clienteByCliente;
    }

    public Factura(int numero, String fecha, Collection<Detalle> detallesByNumero, Proveedor proveedorByProveedor, Cliente clienteByCliente) {
        this.numero = numero;
        this.fecha = fecha;
        this.detallesByNumero = detallesByNumero;
        this.proveedorByProveedor = proveedorByProveedor;
        this.clienteByCliente = clienteByCliente;
    }

    public Factura() {
    }
}
