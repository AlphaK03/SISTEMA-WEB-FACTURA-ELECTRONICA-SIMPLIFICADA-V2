package com.example.proyecto_i.data;

import org.springframework.stereotype.Component;

@Component
public interface UsuarioRepository {
    Usuario findByIdentification(String identification);
}
