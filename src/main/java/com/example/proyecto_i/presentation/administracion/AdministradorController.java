package com.example.proyecto_i.presentation.administracion;

import com.example.proyecto_i.logic.Service;
import com.example.proyecto_i.logic.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administracion")
public class AdministradorController {
    @Autowired
    private Service service;

    @GetMapping("/proveedores")
    public List<Usuario> verProveedores() {
        return service.getAll();
    }

    @PostMapping("/actualizarProveedor")
    public void actualizarProveedor(@RequestParam("proveedorId") String proveedorId, @RequestParam("estado") boolean estado) {
        service.actualizarEstadoUsuario(proveedorId, String.valueOf(estado));
    }




}