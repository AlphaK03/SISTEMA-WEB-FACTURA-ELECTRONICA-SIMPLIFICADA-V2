package com.example.proyecto_i.presentation.producto;

import com.example.proyecto_i.logic.Producto;
import com.example.proyecto_i.logic.ProductoSimpleDTO;
import com.example.proyecto_i.logic.Proveedor;
import com.example.proyecto_i.logic.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private Service service;

    @PostMapping("/agregar")
    public String registrarProducto(@RequestBody Producto producto, Authentication authentication) {
        try {
            String username = authentication.getName();
            Optional<Proveedor> proveedorOpt = service.proveedorRead(username);
            if (proveedorOpt.isPresent()) {
                Proveedor proveedor = proveedorOpt.get();
                producto.setProveedorByProveedor(proveedor);
                service.productosCreate(producto);
                return "Producto agregado exitosamente";
            } else {
                return "Proveedor no encontrado";
            }
        } catch (Exception e) {
            return "Error al agregar producto";
        }
    }

    @GetMapping("/listar")
    public List<ProductoSimpleDTO> listarProductos(@RequestParam String proveedorId) {
        return service.productosByProveedor(proveedorId);
    }

    @GetMapping("/proveedor")
    public Proveedor obtenerProveedor(Authentication authentication) {
        String username = authentication.getName();
        return service.proveedorRead(username).orElse(null);
    }
}
