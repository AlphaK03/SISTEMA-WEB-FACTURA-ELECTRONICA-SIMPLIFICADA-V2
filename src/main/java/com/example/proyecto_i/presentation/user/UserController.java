package com.example.proyecto_i.presentation.user;

import com.example.proyecto_i.logic.Proveedor;
import com.example.proyecto_i.logic.Service;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    Service service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/details")
    public UserDetailsDTO getUserDetails(Authentication authentication) {
        if (authentication != null) {
            String username = authentication.getName();
            String role = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(","));
            return new UserDetailsDTO(username, role);
        }
        return null;
    }

    @GetMapping("/perfil")
    public ResponseEntity<Proveedor> getPerfil(Authentication authentication) {
        if (authentication == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String username = authentication.getName();
        Optional<Proveedor> proveedorOpt = service.proveedorRead(username);
        if (proveedorOpt.isPresent()) {
            return new ResponseEntity<>(proveedorOpt.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/editarPerfil")
    public ResponseEntity<Map<String, Object>> editarPerfil(@RequestBody Proveedor proveedor, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();
        if (authentication == null) {
            response.put("success", false);
            response.put("message", "No user authenticated");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        try {
            String username = authentication.getName();
            Optional<Proveedor> proveedorOpt = service.proveedorRead(username);
            if (proveedorOpt.isPresent()) {
                Proveedor existingProveedor = proveedorOpt.get();
                existingProveedor.setTelefono(proveedor.getTelefono());
                existingProveedor.setCorreo(proveedor.getCorreo());
                service.proveedorUpdate(existingProveedor.getIdentificacion(), existingProveedor);
                response.put("success", true);
                response.put("message", "Perfil actualizado correctamente");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("success", false);
                response.put("message", "Proveedor no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar perfil");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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


