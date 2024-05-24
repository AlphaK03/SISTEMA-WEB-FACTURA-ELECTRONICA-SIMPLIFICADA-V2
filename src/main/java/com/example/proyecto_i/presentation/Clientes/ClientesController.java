package com.example.proyecto_i.presentation.Clientes;

import com.example.proyecto_i.logic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/api/clientes")
public class ClientesController {
    @Autowired
    private Service service;

    @PostMapping("/crearCliente")
    public ResponseEntity<Map<String, Object>> crearCliente(@RequestBody Cliente cliente, Authentication authentication) {
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
                Proveedor proveedor = proveedorOpt.get();
                cliente.setProveedorByProveedor(proveedor);
                service.clientesCreate(cliente);
                response.put("success", true);
                response.put("message", "Cliente agregado exitosamente");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("success", false);
                response.put("message", "Proveedor no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al agregar cliente");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/listar")
    public List<ClienteSimpleDTO> listarClientes(Authentication authentication) throws Exception {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No user authenticated");
        }
        try {
            String username = authentication.getName();
            Optional<Proveedor> proveedorOpt = service.proveedorRead(username);
            if (proveedorOpt.isPresent()) {
                Proveedor proveedor = proveedorOpt.get();
                return service.clientesByProveedor(proveedor.getIdentificacion());
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado");
            }
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Proveedor no encontrado");
        }
    }
    @GetMapping("/registroExitoso")
    public String registroExitoso() {
        return "/pages/registro/registroExitoso";
    }
}