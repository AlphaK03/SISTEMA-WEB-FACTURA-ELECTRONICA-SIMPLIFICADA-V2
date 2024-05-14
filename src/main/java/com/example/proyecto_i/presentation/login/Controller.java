package com.example.proyecto_i.presentation.login;

import com.example.proyecto_i.logic.Service;
import com.example.proyecto_i.logic.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/login")
public class Controller {

    @Autowired
    private Service service;

    @PostMapping("/login")
    public Usuario login(@RequestBody Usuario form, HttpServletRequest request) {
        try {
            request.login(form.getIdentificacion(), form.getContrasena());
        } catch (ServletException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        Authentication auth = (Authentication) request.getUserPrincipal();
        Usuario usuario = (Usuario) auth.getPrincipal();
        return new Usuario(usuario.getIdentificacion(), null, usuario.getRol());
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        try {
            request.logout();
        } catch (ServletException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Logout failed");
        }
    }

    @GetMapping("/current-user")
    public Usuario getCurrentUser(@AuthenticationPrincipal Usuario usuario) {
        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return new Usuario(usuario.getIdentificacion(), null, usuario.getRol());
    }

    @GetMapping("/registro")
    public String showRegisterPage() {
        return "pages/registro/registrar"; // Devuelve el nombre de la vista del registro
    }
}
