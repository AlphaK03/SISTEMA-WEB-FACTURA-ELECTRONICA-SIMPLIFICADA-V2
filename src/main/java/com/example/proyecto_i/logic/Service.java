package com.example.proyecto_i.logic;
import com.example.proyecto_i.data.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.proyecto_i.data.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@org.springframework.stereotype.Service("service")
public class Service {
    @Autowired
    private final ProveedorRepository proveedorRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private final ProductosRepository productosRepository;
    @Autowired
    private final ClientesRepository clientesRepository;
    @Autowired
    private final FacturasRepository facturasRepository;
    @Autowired
    private final DetallesRepository detallesRepository;



    public Service(ProveedorRepository proveedorRepository,
                   UsuarioRepository usuarioRepository, ClientesRepository clientesRepository,
                   ProductosRepository productosRepository, FacturasRepository facturasRepository, DetallesRepository detallesRepository) {

        this.proveedorRepository = proveedorRepository;
        this.usuarioRepository = usuarioRepository;
        this.clientesRepository = clientesRepository;
        this.productosRepository = productosRepository;
        this.facturasRepository = facturasRepository;
        this.detallesRepository = detallesRepository;

    }


    //------------------USUARIO----------------


    public Object clone(Proveedor proveedor, Object nombre) {
        return nombre;
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
            return proveedorRepository.save(proveedor);
        } else {
            throw new RuntimeException("Proveedor no encontrado con ID: " + id);
        }
    }

    //------------------PRODUCTOS----------------

    public List<ProductoSimpleDTO> productosByProveedor(String idProveedor) {
        List<Producto> productos = productosRepository.findByProveedorByProveedorIdentificacion(idProveedor);
        return productos.stream()
                .map(producto -> new ProductoSimpleDTO(producto.getNombre(), producto.getPrecio()))
                .collect(Collectors.toList());
    }

    public void productosCreate (Producto product) throws  Exception{
        productosRepository.save(product);
    }

    public List<Producto> productosSearchAll(String id_proveedor) throws Exception{
        //Hay que filtrar cada uno de los productos según el proveedor
        List<Producto> todos = (List<Producto>) productosRepository.findAll();
        List<Producto> filtradosPorProveedor = new ArrayList<>();

        for (Producto producto : todos) {
            if (producto.getProveedorByProveedor().getIdentificacion().equals(id_proveedor)) {
                producto.setProveedorByProveedor(null);
                filtradosPorProveedor.add(producto);
            }
        }

        return filtradosPorProveedor;
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
    public List<ClienteSimpleDTO> clientesByProveedor(String idProveedor) {
        List<Cliente> clientes = clientesRepository.findByProveedorByProveedorIdentificacion(idProveedor);
        return clientes.stream()
                .map(cliente -> new ClienteSimpleDTO(cliente.getIdentificacion(), cliente.getNombre(), cliente.getTelefono(), cliente.getCorreo()))
                .collect(Collectors.toList());
    }

    public Cliente findClienteByProveedor(String idProveedor, String idCliente){
        return clientesRepository.findClienteByProveedor(idProveedor, idCliente);
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


    public void facturasCreate(Factura factura) throws Exception {
        facturasRepository.save(factura);
    }



    public List<FacturaSimpleDTO> facturasByProveedor(String idProveedor) {
        List<Factura> facturas = new ArrayList<>();

        facturas = facturasGetAll();
        List<Factura> listaFiltrada = new ArrayList<>();

        for(Factura factura : facturas){
            if(Objects.equals(factura.getProveedorByProveedor().getIdentificacion(), proveedorRepository.findById(idProveedor).get().getIdentificacion())){
                listaFiltrada.add(factura);
            }
        }

        return listaFiltrada.stream()
                .map(factura -> convertToDTO(factura))
                .collect(Collectors.toList());
    }

    private FacturaSimpleDTO convertToDTO(Factura factura) {
        return new FacturaSimpleDTO(factura.getNumero(), factura.getFecha(),
                factura.getProveedorByProveedor().getIdentificacion(),
                factura.getClienteByCliente().getIdentificacion());
    }


    public List<Factura> facturasSearchByProveedor(String idProveedor) throws Exception {
        return facturasRepository.findByProveedor(idProveedor);
    }

    public Factura facturasSearchById(int id) throws Exception {
        return facturasRepository.findById(String.valueOf(id)).orElse(null);
    }

    public void facturasDelete(String id) throws Exception {
        facturasRepository.deleteById(id);
    }

    public Factura facturasUpdate(String id, Factura facturaActualizada) {
//        Optional<Factura> facturaOptional = facturasRepository.findById(id);
//
//        if (facturaOptional.isPresent()) {
//            Factura factura = facturaOptional.get();
//            factura.setFecha(facturaActualizada.getFecha());
//            return (Factura) facturasRepository.save(factura);
//        } else {
//            throw new RuntimeException("Factura no encontrada con ID: " + id);
//        }
        return facturaActualizada;
    }
    @Autowired
    private PasswordEncoder passwordEncoder;
    public void registrar(Proveedor administrador, Usuario user) {
        String contrasenaEncriptada = passwordEncoder.encode(user.getContrasena());
        user.setContrasena(contrasenaEncriptada);
        proveedorRepository.save(administrador);
        usuarioRepository.save(user);
    }

    public Optional<Usuario> usuarioRead(String identification) {

        return usuarioRepository.findById(identification);
    }

    public Optional<Proveedor> proveedorRead(String identification) {

        return proveedorRepository.findById(identification);
    }
    public Optional<Cliente> clienteRead(String identificacion){
        return clientesRepository.findById(identificacion);
    }

    public List<Proveedor> proveedorGetAll() {
        return (List<Proveedor>) proveedorRepository.findAll();
    }

    public List<Usuario> getAll() {
        return (List<Usuario>) usuarioRepository.findAll();
    }

    public void actualizarEstadoUsuario(String proveedorIds, String estados) {
        String[] ids = proveedorIds.split(",\\s*");
        String[] estadosArray = estados.split(",\\s*");

        if (ids.length != estadosArray.length) {
            throw new IllegalArgumentException("La cantidad de IDs no coincide con la cantidad de estados.");
        }

        for (int i = 0; i < ids.length; i++) {
            String id = ids[i];
            String estado = estadosArray[i];

            Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
            if (optionalUsuario.isPresent()) {
                Usuario usuario = optionalUsuario.get();
                byte activoByte = (byte) (estado != null && estado.equalsIgnoreCase("activo") ? 1 : 0);
                usuario.setActivo(activoByte);
                usuarioRepository.save(usuario);
            } else {
                throw new RuntimeException("No se encontró el usuario con el ID: " + id);
            }

        }
    }



    public void detalleCreate(Detalle detalle) throws Exception {
        detallesRepository.save(detalle);
    }

    public Optional<Detalle> detalleSearch(int numero) throws Exception {
        return detallesRepository.findById(numero);
    }

    public void detalleDelete(int numero) throws Exception {
        detallesRepository.deleteById(numero);
    }

    public Detalle detalleUpdate(int numero, Detalle detalleActualizado) {
        return new Detalle();
    }

    public List<Factura> facturasGetAll() {
        return (List<Factura>) facturasRepository.findAll();
    }

    public List<Detalle> detallesGetAll() {
        return (List<Detalle>) detallesRepository.findAll();
    }


    @Autowired
    public void UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

}


