package com.example.proyecto_i.logic;
import com.example.proyecto_i.data.*;

import java.util.List;
import java.util.Optional;

import com.example.proyecto_i.data.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service("service")
public class Service {
    @Autowired
    private final ProveedorRepository proveedorRepository;
    @Autowired
    private final AdministradorRepository administradorRepository;
    @Autowired
    private final ProductosRepository productosRepository;
    @Autowired
    private final ClientesRepository clientesRepository;
    @Autowired
    private final FacturasRepository facturasRepository;

    public Service( ProveedorRepository proveedorRepository,
                   AdministradorRepository administradorRepository, ClientesRepository clientesRepository,
                   ProductosRepository productosRepository, FacturasRepository facturasRepository) {

        this.proveedorRepository = proveedorRepository;
        this.administradorRepository = administradorRepository;
        this.facturasRepository = facturasRepository;
        this.clientesRepository = clientesRepository;
        this.productosRepository = productosRepository;
    }


    //------------------USUARIO----------------


    public Object clone(Proveedor proveedor, Object nombre) {
        return nombre;
    }

    //------------------ADMINISTRADOR----------------
    public void administradorCreate (Administrador admin) throws  Exception{
        administradorRepository.save(admin);
    }
    public List<Administrador> administradorSearchAll() throws Exception{
        return (List<Administrador>) administradorRepository.findAll();
    }
    public Administrador administradorSearch(String id)  throws Exception{
        return administradorRepository.findById(id).orElse(null);
    }
    public void administradorDelete (String id)  throws Exception{
        administradorRepository.deleteById(id);
    }
    public Administrador administradorUpdate(String id, Administrador administradorActualizado) {
        Optional<Administrador> AdministradorOptional = administradorRepository.findById(id);

        if (AdministradorOptional.isPresent()) {
            Administrador administrador = AdministradorOptional.get();
            administrador.setNombre(administradorActualizado.getNombre());
            administrador.setTelefono(administradorActualizado.getTelefono());
            administrador.setCorreo(administradorActualizado.getCorreo());
            administrador.setContrasena(administradorActualizado.getContrasena());
            return administradorRepository.save(administrador);
        } else {
            throw new RuntimeException("Administrador no encontrado con ID: " + id);
        }
    }

    //------------------PROVEEDOR----------------
    public void proveedorCreate (Proveedor prov) throws  Exception{
        proveedorRepository.save(prov);
    }

    public Proveedor proveerdorSearch(String id)  throws Exception{
        return proveedorRepository.findById(id).orElse(null);
    }
    public void proveedorDelete (String id)  throws Exception{
        proveedorRepository.deleteById(id);
    }
    public Proveedor proveedorUpdate(String id, Proveedor proveedorActualizado) {
        Optional<Proveedor> proveedorOptional = proveedorRepository.findById(id);

        if (proveedorOptional.isPresent()) {
            Proveedor proveedor = proveedorOptional.get();
            proveedor.setNombre(proveedorActualizado.getNombre());
            proveedor.setTelefono(proveedorActualizado.getTelefono());
            proveedor.setCorreo(proveedorActualizado.getCorreo());
            proveedor.setContrasena(proveedorActualizado.getContrasena());
            return proveedorRepository.save(proveedor);
        } else {
            throw new RuntimeException("Proveedor no encontrado con ID: " + id);
        }
    }

    //------------------PRODUCTOS----------------
    public void productosCreate (Producto product) throws  Exception{
        productosRepository.save(product);
    }

    public List<Producto> productosSearchAll(String id_proveedor) throws Exception{
        //Hay que filtrar cada uno de los productos según el proveedor
        return (List<Producto>) productosRepository.findAll();
    }
    public Optional<Producto> productosSearch(Proveedor prov, String cod)  throws Exception{
        /*Flitrar los productos según el proveedor*/
        return productosRepository.findById(cod);
    }
    public void productosDelete (String id)  throws Exception{
        productosRepository.deleteById(id);
    }

    //------------------CLIENTES----------------
    public void clientesCreate(Cliente cliente) throws Exception {
        clientesRepository.save(cliente);
    }

    public List<Cliente> clientesSearchAll() throws Exception {
        return (List<Cliente>) clientesRepository.findAll();
    }

    public Cliente clientesSearch(String id) throws Exception {
        return clientesRepository.findById(id).orElse(null);
    }

    public void clientesDelete(String id) throws Exception {
        clientesRepository.deleteById(id);
    }

    public Cliente clientesUpdate(String id, Cliente clienteActualizado) {
        Optional<Cliente> clienteOptional = clientesRepository.findById(id);

        if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();
            cliente.setNombre(clienteActualizado.getNombre());
            cliente.setTelefono(clienteActualizado.getTelefono());
            cliente.setCorreo(clienteActualizado.getCorreo());
            return (Cliente) clientesRepository.save(cliente);
        } else {
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        }
    }

    //------------------FACTURAS----------------
    public void facturasCreate(Factura factura) throws Exception {
        facturasRepository.save(factura);
    }

    public List<Factura> facturasSearchAll() throws Exception {
        return (List<Factura>) facturasRepository.findAll();
    }

    public Factura facturasSearch(String id) throws Exception {
        return facturasRepository.findById(id).orElse(null);
    }

    public void facturasDelete(String id) throws Exception {
        facturasRepository.deleteById(id);
    }

    public Factura facturasUpdate(String id, Factura facturaActualizada) {
        Optional<Factura> facturaOptional = facturasRepository.findById(id);

        if (facturaOptional.isPresent()) {
            Factura factura = facturaOptional.get();
            factura.setFecha(facturaActualizada.getFecha());
            return (Factura) facturasRepository.save(factura);
        } else {
            throw new RuntimeException("Factura no encontrada con ID: " + id);
        }
    }


    public void registrar(Administrador administrador) {
        administradorRepository.save(administrador);
    }
}

