package com.example.proyecto_i.presentation.registro;

import com.example.proyecto_i.logic.Proveedor;
import com.example.proyecto_i.logic.Service;
import com.example.proyecto_i.logic.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@org.springframework.stereotype.Controller("registro")
public class Controller {

    @Autowired
    private Service service;
    @PostMapping("/registro")
    public String registrarAdministrador(Proveedor proveedor, Usuario user) {
        user.setRol("PRO");
        service.registrar(proveedor, user);
        return "redirect:/registroExitoso"; // Redireccionar a una página de registro exitoso
    }

    @GetMapping("/registroExitoso")
    public String registroExitoso() {
        return "/presentation/registro/registroExitoso"; // Redireccionar a una página de registro exitoso
    }

}