package com.example.proyecto_i.presentation.producto;

import com.example.proyecto_i.logic.Producto;
import com.example.proyecto_i.logic.Proveedor;
import com.example.proyecto_i.logic.Service;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Controller("producto")

public class Controller {
    @Autowired
    private Service service;
    @PostMapping("/agregarProducto")
    public String registrarProducto(Producto producto, @RequestParam("proveedor") String provID, HttpSession session) {
        try {
            Optional<Proveedor> proveedorID = service.proveedorRead(provID);

            if (proveedorID.isPresent()) {
                Proveedor proveedor = proveedorID.get();
                producto.setProveedorByProveedor(proveedor);
                service.productosCreate(producto);
            }
            session.setAttribute("provID", provID); // Guardar el ID del proveedor en la sesión

            return "redirect:/login"; // Redirigir a la misma página después de agregar el producto
        } catch (Exception e) {
            return "redirect:/error"; // Redirigir a una página de error en caso de excepción
        }
    }



    @GetMapping("/login")
    public String mostrarFormularioAgregarProducto(HttpSession session, Model model) throws Exception {
        String provID = (String) session.getAttribute("provID");

        if (provID == null || provID.isEmpty()) {
            return "redirect:/error";
        }

        List<Producto> productos = service.productosSearchAll(provID);
        List<Proveedor> proveedores = service.proveedorGetAll();

        Optional<Proveedor> proveedorExist = service.proveedorRead(provID);
        if(proveedorExist.isPresent()){
            Proveedor proveedor = proveedorExist.get();
            model.addAttribute("proveedor", proveedor);
        }
        model.addAttribute("productos", productos);
        model.addAttribute("provID", provID);


        return "/presentation/productos/agregarProducto";
    }



}
