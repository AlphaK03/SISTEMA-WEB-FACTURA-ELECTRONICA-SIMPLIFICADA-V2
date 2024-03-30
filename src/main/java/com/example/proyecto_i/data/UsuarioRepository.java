package com.example.proyecto_i.data;

import com.example.proyecto_i.logic.Usuario;
import org.springframework.stereotype.Component;

@Component
public interface UsuarioRepository {
    Usuario findByIdentification(String identification);
}
