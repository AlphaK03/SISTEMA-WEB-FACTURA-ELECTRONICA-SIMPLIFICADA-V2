package com.example.proyecto_i.logic;

import com.example.proyecto_i.data.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class Service {
    private final UsuarioRepository usuarioRepository;
    private final ProveedorRepository proveedorRepository;
    private final AdministradorRepository administradorRepository;
    private final ProductosRepository productosRepository;
    private final ClientesRepository clientesRepository;
    private final FacturasRepository facturasRepository;

    @Autowired
    public Service(UsuarioRepository usuarioRepository, ProveedorRepository proveedorRepository,
                   AdministradorRepository administradorRepository, ClientesRepository clientesRepository,
                   ProductosRepository productosRepository, FacturasRepository facturasRepository) {
        this.usuarioRepository = usuarioRepository;
        this.proveedorRepository = proveedorRepository;
        this.administradorRepository = administradorRepository;
        this.facturasRepository = facturasRepository;
        this.clientesRepository = clientesRepository;
        this.productosRepository = productosRepository;
    }

    //------------------USUARIO----------------
    public Usuario usuarioRead(String identification) {

        return usuarioRepository.findByIdentification(identification);
    }

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
            administrador.setContraseña(administradorActualizado.getContraseña());
            return administradorRepository.save(administrador);
        } else {
            throw new RuntimeException("Administrador no encontrado con ID: " + id);
        }
    }

    //------------------PROVEEDOR----------------
    public void proveedorCreate (Proveedor prov) throws  Exception{
        proveedorRepository.save(prov);
    }
    public Proveedor proveedorRead(String identification) {

        return proveedorRepository.findByIdentification(identification);
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
            proveedor.setContraseña(proveedorActualizado.getContraseña());
            return proveedorRepository.save(proveedor);
        } else {
            throw new RuntimeException("Proveedor no encontrado con ID: " + id);
        }
    }

    //------------------PRODUCTOS----------------
    public void productosCreate (Productos product) throws  Exception{
        productosRepository.save(product);
    }

    public List<Productos> productosSearchAll(String id_proveedor) throws Exception{
        //Hay que filtrar cada uno de los productos según el proveedor
        return (List<Productos>) productosRepository.findAll();
    }
    public Optional<Productos> productosSearch(Proveedor prov, String cod)  throws Exception{
        /*Flitrar los productos según el proveedor*/
        return productosRepository.findById(cod);
    }
    public void productosDelete (String id)  throws Exception{
        productosRepository.deleteById(id);
    }

    //------------------CLIENTES----------------
    public void clientesCreate(Clientes cliente) throws Exception {
        clientesRepository.save(cliente);
    }

    public List<Clientes> clientesSearchAll() throws Exception {
        return (List<Clientes>) clientesRepository.findAll();
    }

    public Clientes clientesSearch(String id) throws Exception {
        return clientesRepository.findById(id).orElse(null);
    }

    public void clientesDelete(String id) throws Exception {
        clientesRepository.deleteById(id);
    }

    public Clientes clientesUpdate(String id, Clientes clienteActualizado) {
        Optional<Clientes> clienteOptional = clientesRepository.findById(id);

        if (clienteOptional.isPresent()) {
            Clientes cliente = clienteOptional.get();
            cliente.setNombre(clienteActualizado.getNombre());
            cliente.setTelefono(clienteActualizado.getTelefono());
            cliente.setCorreo(clienteActualizado.getCorreo());
            return (Clientes) clientesRepository.save(cliente);
        } else {
            throw new RuntimeException("Cliente no encontrado con ID: " + id);
        }
    }

    //------------------FACTURAS----------------
    public void facturasCreate(Facturas factura) throws Exception {
        facturasRepository.save(factura);
    }

    public List<Facturas> facturasSearchAll() throws Exception {
        return (List<Facturas>) facturasRepository.findAll();
    }

    public Facturas facturasSearch(String id) throws Exception {
        return facturasRepository.findById(id).orElse(null);
    }

    public void facturasDelete(String id) throws Exception {
        facturasRepository.deleteById(id);
    }

    public Facturas facturasUpdate(String id, Facturas facturaActualizada) {
        Optional<Facturas> facturaOptional = facturasRepository.findById(id);

        if (facturaOptional.isPresent()) {
            Facturas factura = facturaOptional.get();
            factura.setFecha(facturaActualizada.getFecha());
            return (Facturas) facturasRepository.save(factura);
        } else {
            throw new RuntimeException("Factura no encontrada con ID: " + id);
        }
    }


}

