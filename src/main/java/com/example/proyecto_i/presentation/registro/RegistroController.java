package com.example.proyecto_i.presentation.registro;

import com.example.proyecto_i.logic.Proveedor;
import com.example.proyecto_i.logic.Service;
import com.example.proyecto_i.logic.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registro")
public class RegistroController {

    @Autowired
    private Service service;

    @PostMapping("/registro")
    public ResponseEntity<?> registrarAdministrador(@RequestBody RegistroRequest registroRequest) {
        try {
            // Crear y configurar el objeto Usuario
            Usuario user = new Usuario();
            user.setIdentificacion(registroRequest.getIdentificacion());
            user.setContrasena(registroRequest.getContrasena());
            user.setRol("PRO");

            // Crear y configurar el objeto Proveedor
            Proveedor proveedor = new Proveedor();
            proveedor.setIdentificacion(registroRequest.getIdentificacion());
            proveedor.setNombre(registroRequest.getNombre());
            proveedor.setTelefono(registroRequest.getTelefono());
            proveedor.setCorreo(registroRequest.getCorreo());

            // Registrar el proveedor y el usuario
            service.registrar(proveedor, user);
            return ResponseEntity.ok().body("{\"success\": true}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("{\"success\": false, \"message\": \"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/registroExitoso")
    public String registroExitoso() {
        return "/pages/registro/registroExitoso";
    }

    public static class RegistroRequest {
        private String identificacion;
        private String nombre;
        private String telefono;
        private String correo;
        private String contrasena;

        // Getters y Setters

        public String getIdentificacion() {
            return identificacion;
        }

        public void setIdentificacion(String identificacion) {
            this.identificacion = identificacion;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getTelefono() {
            return telefono;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }

        public String getCorreo() {
            return correo;
        }

        public void setCorreo(String correo) {
            this.correo = correo;
        }

        public String getContrasena() {
            return contrasena;
        }

        public void setContrasena(String contrasena) {
            this.contrasena = contrasena;
        }
    }
}



