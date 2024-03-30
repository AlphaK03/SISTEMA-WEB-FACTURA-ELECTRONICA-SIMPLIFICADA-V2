package com.example.proyecto_i.data;

import com.example.proyecto_i.logic.Cliente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientesRepository extends CrudRepository <Cliente, String>{
}
