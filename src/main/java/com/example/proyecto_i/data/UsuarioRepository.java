package com.example.proyecto_i.data;

import com.example.proyecto_i.logic.Usuario;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository {

    Usuario findByIdentification(String identification);
}
