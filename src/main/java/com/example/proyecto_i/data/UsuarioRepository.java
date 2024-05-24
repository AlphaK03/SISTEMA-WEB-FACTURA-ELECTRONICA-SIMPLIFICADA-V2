package com.example.proyecto_i.data;

import com.example.proyecto_i.logic.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends CrudRepository <Usuario, String> {
}
