package com.example.proyecto_i.data;

import com.example.proyecto_i.logic.Administrador;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministradorRepository extends CrudRepository <Administrador, String> {

}
