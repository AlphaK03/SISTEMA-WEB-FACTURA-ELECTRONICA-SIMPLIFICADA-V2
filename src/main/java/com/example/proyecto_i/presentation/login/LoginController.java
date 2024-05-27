package com.example.proyecto_i.presentation.login;

import com.example.proyecto_i.logic.Service;
import com.example.proyecto_i.logic.Usuario;
import com.example.proyecto_i.security.UserDetailsImp;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    private UserDetailsImp userDetails;
    @Autowired
    Service service;

    @PostMapping("/login")
    public Usuario login(@RequestBody Usuario form,  HttpServletRequest request) {
        Optional<Usuario> usuario1 = service.usuarioRead(form.getIdentificacion());
        Usuario usuario = new Usuario();
        if(usuario1.isPresent()){
            usuario = usuario1.get();
        }
        if (!usuario.isActivo()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no válido o no activo");
        }
        try {
            request.login(form.getIdentificacion(), form.getContrasena());
        } catch (ServletException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        Authentication auth = (Authentication) request.getUserPrincipal();
        Usuario user = ((UserDetailsImp) auth.getPrincipal()).getUser();
        return new Usuario(user.getIdentificacion(), null, user.getRol());
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        try {
            request.logout();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Logout failed", e);
        }
    }

    @GetMapping("/current-user")
    public Usuario getCurrentUser(@AuthenticationPrincipal UserDetailsImp user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado");
        }
        return new Usuario(user.getUser().getIdentificacion(), null, user.getUser().getRol());
    }
}
