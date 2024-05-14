package com.example.proyecto_i.presentation.administracion;

import com.example.proyecto_i.logic.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@org.springframework.stereotype.Controller("administracion")
public class Controller {
    @Autowired
    private Service service;
    @GetMapping("/presentation/productos/view")
    public String verProductos(@RequestParam("proveedorId") String proveedorId, Model model, HttpSession session) throws Exception {
        List<Producto> productos = service.productosSearchAll(proveedorId);

        String rol = (String) session.getAttribute("rol");
        String provID = (String) session.getAttribute("provID");
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        model.addAttribute("productos", productos);


        return "pages/productos/view";
    }

    @GetMapping("/presentation/administrador")
    public String verProveedores(Model model, HttpSession session) throws Exception {
        List<Usuario> usuarios = service.getAll();
        List<Proveedor> proveedores = service.proveedorGetAll();

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        model.addAttribute("usuario", usuario);

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("proveedores", proveedores);

        return "/pages/otros/administrador";
    }


    @PostMapping("/actualizarProveedores")
    public String actualizarProveedores(@RequestParam("proveedorId") String proveedorId,
                                        @RequestParam("estado") String estado,
                                        Model model, HttpSession session) throws Exception {

        service.actualizarEstadoUsuario(proveedorId, estado);

        return "redirect:/presentation/administrador";
    }



}