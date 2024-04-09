package com.example.proyecto_i.presentation.producto;

import com.example.proyecto_i.logic.Producto;
import com.example.proyecto_i.logic.Proveedor;
import com.example.proyecto_i.logic.Service;
import com.example.proyecto_i.logic.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@org.springframework.stereotype.Controller("producto")

public class Controller {
    @Autowired
    private Service service;
    @PostMapping("/agregarProducto")
    public String registrarProducto(Producto producto, @RequestParam("proveedor") String provID) throws Exception {
        Optional<Proveedor> proveedorID = service.proveedorRead(String.valueOf(provID));

        if (proveedorID.isPresent()) {
            Proveedor proveedor = proveedorID.get();
            producto.setProveedorByProveedor(proveedor);
        }

        service.productosCreate(producto);

        return "redirect:/registroExitoso";
    }

}
