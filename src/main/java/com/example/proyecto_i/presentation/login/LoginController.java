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

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private Service service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public Usuario login(@RequestBody Usuario form, HttpServletRequest request) {
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(form.getIdentificacion(), form.getContrasena());
            Authentication auth = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
            Usuario usuario = (Usuario) auth.getPrincipal();
            return new Usuario(usuario.getIdentificacion(), null, usuario.getRol());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login failed", e);
        }
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
    public Usuario getCurrentUser(@AuthenticationPrincipal UserDetailsImp user) {
        return new Usuario(user.getUser().getIdentificacion(), null, user.getUser().getRol());
    }

    @GetMapping("/registro")
    public String showRegisterPage() {
        return "pages/registro/registrar"; // Devuelve el nombre de la vista del registro
    }
}

class UserDetailsDTO {
    private String username;
    private String role;

    public UserDetailsDTO(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}